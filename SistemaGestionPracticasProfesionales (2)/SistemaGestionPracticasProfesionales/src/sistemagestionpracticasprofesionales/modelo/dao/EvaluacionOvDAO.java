/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistemagestionpracticasprofesionales.modelo.Conexion;

/**
 *
 * @author reino
 */
public class EvaluacionOvDAO {
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
