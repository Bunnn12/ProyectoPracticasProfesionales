/**
 * Nombre del archivo: EvaluacionOvDAO.java
 * Autor: 
 * Fecha: 17/06/25
 * Descripción: Clase DAO encargada de gestionar el acceso a los datos relacionados con las evaluaciones
 * de las organizaciones vinculadas (OV) dentro del sistema de gestión de prácticas profesionales.
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistemagestionpracticasprofesionales.modelo.Conexion;

/**
 * Clase DAO que permite guardar y consultar las evaluaciones realizadas a las organizaciones vinculadas
 * (OV) asociadas a los expedientes de los estudiantes.
 */
public class EvaluacionOvDAO {
    
     /**
     * Guarda una evaluación de la organización vinculada en la base de datos.
     * 
     * Este método inserta un nuevo registro en la tabla {@code evaluacionov} con los detalles de la evaluación:
     * - Puntaje máximo posible.
     * - Puntaje mínimo aprobado.
     * - Puntaje total obtenido por el estudiante.
     * - Retroalimentación textual.
     * - Identificador del expediente del estudiante evaluado.
     * 
     * @param puntajeMax Puntaje máximo posible de la evaluación.
     * @param puntajeMinAprob Puntaje mínimo necesario para aprobar.
     * @param puntajeTotal Puntaje total obtenido en la evaluación.
     * @param retroalimentacion Comentarios o retroalimentación de la evaluación.
     * @param idExpediente Identificador del expediente del estudiante.
     * @return {@code true} si la inserción fue exitosa, {@code false} en caso contrario.
     * @throws SQLException Si ocurre un error durante la conexión o la ejecución del comando SQL.
     */
    public static boolean guardarEvaluacionOV(double puntajeMax, double puntajeMinAprob, double puntajeTotal,
                                              String retroalimentacion, int idExpediente) throws SQLException {
        String sql = "INSERT INTO evaluacionov (puntajeMaximo, puntajeMinimoAprobatorio, puntajeTotalObtenido, retroalimentacion, idExpediente) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, puntajeMax);
            ps.setDouble(2, puntajeMinAprob);
            ps.setDouble(3, puntajeTotal);
            ps.setString(4, retroalimentacion);
            ps.setInt(5, idExpediente);

            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }
    
    /**
     * Obtiene el estado de evaluacionov del expediente de un estudiante.
     * 
     * @param idEstudiante id del estudiante.
     * @return estado de evaluacionov ("realizada" o "sin realizar"), o null si no se encuentra.
     */
    public static String obtenerEstadoEvaluacionOV(int idEstudiante) {
        String estado = null;
        String sql = "SELECT evaluacionov FROM expediente WHERE idEstudiante = ?";

        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estado = rs.getString("evaluacionov");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estado;
    }
}
