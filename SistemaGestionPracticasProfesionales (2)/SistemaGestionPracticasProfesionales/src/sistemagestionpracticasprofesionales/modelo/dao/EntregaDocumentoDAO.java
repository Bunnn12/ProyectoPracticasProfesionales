/**
 * Nombre del archivo: EntregaDocumentoDAO.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 18/06/2025
 * Descripción: Clase DAO que maneja las operaciones relacionadas con la entrega de documentos iniciales.
 * Incluye la obtención de fechas límite, validación de rangos de entrega, 
 * almacenamiento, actualización, eliminación y consulta de documentos en la base de datos.
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.DocumentoAnexo;

/**
 * Clase DAO que permite realizar operaciones de lectura y escritura sobre las entregas de documentos,
 * específicamente documentos iniciales, en la base de datos del sistema.
 * Incluye métodos para obtener fechas límite, validar rangos de entrega, 
 * guardar, actualizar, eliminar y consultar documentos asociados a expedientes.
 */
    public class EntregaDocumentoDAO {
        
        /**
        * Obtiene la fecha máxima de entrega para documentos de tipo "inicial" en el periodo actual.
        * 
        * @return La fecha máxima de entrega como LocalDate, o null si no se encuentra el periodo o hay error.
        */
        public static LocalDate obtenerFechaMaximaEntregaInicial() {
            Integer idPeriodo = PeriodoDAO.obtenerIdPeriodoActual();
            if (idPeriodo == null) return null;

            LocalDate fechaMaxima = null;
            String consulta = "SELECT fechaFin FROM entrega_documento "
                            + "WHERE tipo = 'inicial' AND idPeriodo = ?";

            try (Connection conexion = Conexion.abrirConexion();
                 PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
                pstmt.setInt(1, idPeriodo);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        fechaMaxima = rs.getDate("fechaFin").toLocalDate();
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al obtener fecha máxima de entrega: " + e.getMessage());
            }

            return fechaMaxima;
        }

        /**
        * Verifica si la fecha actual se encuentra dentro del rango permitido para entrega de documentos iniciales
        * en el periodo actual.
        * 
        * @return true si la fecha actual está dentro del rango, false en caso contrario o error.
        */
        public static boolean estaDentroDeRangoEntregaInicial() {
            Integer idPeriodo = PeriodoDAO.obtenerIdPeriodoActual();
            if (idPeriodo == null) return false;

            String consulta = "SELECT 1 FROM entrega_documento "
                            + "WHERE tipo = 'inicial' "
                            + "AND idPeriodo = ? "
                            + "AND CURRENT_DATE BETWEEN fechaInicio AND fechaFin";

            try (Connection conexion = Conexion.abrirConexion();
                 PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
                pstmt.setInt(1, idPeriodo);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                System.err.println("Error al validar fechas de entrega: " + e.getMessage());
            }

            return false;
        }
        
         /**
        * Guarda un documento inicial en la base de datos, asociándolo a un expediente.
        * 
        * @param archivo El archivo físico que se va a guardar.
        * @param idExpediente El identificador del expediente al que pertenece el documento.
        * @return true si el guardado fue exitoso, false en caso contrario.
        */
        public static boolean guardarDocumentoInicial(File archivo, int idExpediente) {
            String sql = "INSERT INTO documento (nombre, fechaElaboracion, tipo, idExpediente, archivo, estado) "
                       + "VALUES (?, CURDATE(), 'inicial', ?, ?, 'no_validado')";

            try (Connection conn = Conexion.abrirConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, archivo.getName());
                ps.setInt(2, idExpediente);
                ps.setBytes(3, Files.readAllBytes(archivo.toPath()));

                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
        * Obtiene una lista de documentos iniciales asociados a un expediente, excluyendo los eliminados.
        * 
        * @param idExpediente El identificador del expediente.
        * @return Lista de objetos DocumentoAnexo con información básica de los documentos.
        */
        public static List<DocumentoAnexo> obtenerDocumentosInicialesPorExpediente(int idExpediente) {
            List<DocumentoAnexo> documentos = new ArrayList<>();
            String sql = "SELECT idDocumentoAnexo, nombre FROM documento WHERE tipo = 'inicial' AND idExpediente = ? AND estado != 'eliminado' ORDER BY idDocumentoAnexo ASC";

            try (Connection conn = Conexion.abrirConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idExpediente);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        DocumentoAnexo doc = new DocumentoAnexo();
                        doc.setIdDocumentoAnexo(rs.getInt("idDocumentoAnexo"));
                        doc.setNombre(rs.getString("nombre"));
                        documentos.add(doc);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return documentos;
        }

         /**
        * Elimina un documento de la base de datos por su identificador.
        * 
        * @param idDocumento El identificador del documento a eliminar.
        * @return true si la eliminación fue exitosa, false en caso contrario.
        */
        public static boolean eliminarDocumentoPorId(int idDocumento) {
            String sql = "DELETE FROM documento WHERE idDocumentoAnexo = ?";
            try (Connection conn = Conexion.abrirConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idDocumento);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
        * Actualiza un documento inicial existente con un nuevo archivo y actualiza su estado a "no_validado".
        * 
        * @param idDocumento El identificador del documento a actualizar.
        * @param archivo El nuevo archivo que reemplazará al anterior.
        * @return true si la actualización fue exitosa, false en caso contrario.
        */
        public static boolean actualizarDocumentoInicial(int idDocumento, File archivo) {
            String sql = "UPDATE documento SET nombre = ?, fechaElaboracion = CURDATE(), archivo = ?, estado = 'no_validado' WHERE idDocumentoAnexo = ?";

            try (Connection conn = Conexion.abrirConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, archivo.getName());
                ps.setBytes(2, Files.readAllBytes(archivo.toPath()));
                ps.setInt(3, idDocumento);

                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
}
