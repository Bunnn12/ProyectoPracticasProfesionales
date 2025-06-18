/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
 *
 * @author reino
 */
    public class EntregaDocumentoDAO {
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
