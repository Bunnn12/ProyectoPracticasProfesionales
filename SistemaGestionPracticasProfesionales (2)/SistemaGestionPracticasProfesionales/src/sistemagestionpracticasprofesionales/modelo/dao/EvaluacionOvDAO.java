package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;

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

    public static ResultadoOperacion registrarEvaluacionOV(double puntajeMax, double puntajeMinAprob, double puntajeTotal,
                                                           String retroalimentacion, int idEstudiante,
                                                           Map<Integer, Integer> criteriosPuntaje) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = Conexion.abrirConexion();

        if (conexionBD != null) {
            try {
                conexionBD.setAutoCommit(false);

                // Obtener idExpediente del estudiante
                String sqlExpediente = "SELECT idExpediente FROM expediente WHERE idEstudiante = ?";
                int idExpediente = -1;
                try (PreparedStatement psExp = conexionBD.prepareStatement(sqlExpediente)) {
                    psExp.setInt(1, idEstudiante);
                    ResultSet rs = psExp.executeQuery();
                    if (rs.next()) {
                        idExpediente = rs.getInt("idExpediente");
                    } else {
                        resultado.setError(true);
                        resultado.setMensaje("No se encontró expediente para el estudiante.");
                        return resultado;
                    }
                }

                // Insertar evaluación principal
                String sqlEval = "INSERT INTO evaluacionov (puntajeMaximo, puntajeMinimoAprobatorio, puntajeTotalObtenido, retroalimentacion, idExpediente) VALUES (?, ?, ?, ?, ?)";
                int idEvaluacionOV = -1;
                try (PreparedStatement psEval = conexionBD.prepareStatement(sqlEval, Statement.RETURN_GENERATED_KEYS)) {
                    psEval.setDouble(1, puntajeMax);
                    psEval.setDouble(2, puntajeMinAprob);
                    psEval.setDouble(3, puntajeTotal);
                    psEval.setString(4, retroalimentacion);
                    psEval.setInt(5, idExpediente);

                    int filasInsertadas = psEval.executeUpdate();
                    if (filasInsertadas == 1) {
                        ResultSet generatedKeys = psEval.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            idEvaluacionOV = generatedKeys.getInt(1);
                        } else {
                            conexionBD.rollback();
                            resultado.setError(true);
                            resultado.setMensaje("No se pudo obtener el ID generado de la evaluación OV.");
                            return resultado;
                        }
                    } else {
                        conexionBD.rollback();
                        resultado.setError(true);
                        resultado.setMensaje("No se pudo insertar la evaluación OV.");
                        return resultado;
                    }
                }

                // Insertar detalles por criterio
                String sqlDetalle = "INSERT INTO detallecriterioevaluacionov (idEvaluacionOV, idCriterio, puntajeObtenido) VALUES (?, ?, ?)";
                try (PreparedStatement psDetalle = conexionBD.prepareStatement(sqlDetalle)) {

                    if (criteriosPuntaje == null || criteriosPuntaje.isEmpty()) {
                        conexionBD.rollback();
                        resultado.setError(true);
                        resultado.setMensaje("No se proporcionaron criterios para evaluar.");
                        return resultado;
                    }

                    System.out.println("Insertando detalles de criterios:");
                    for (Map.Entry<Integer, Integer> entry : criteriosPuntaje.entrySet()) {
                        System.out.println("Criterio ID: " + entry.getKey() + ", Puntaje: " + entry.getValue());
                        psDetalle.setInt(1, idEvaluacionOV);
                        psDetalle.setInt(2, entry.getKey());
                        psDetalle.setInt(3, entry.getValue());
                        psDetalle.addBatch();
                    }

                    int[] resultados = psDetalle.executeBatch();
                    System.out.println("Filas insertadas en detallecriterioevaluacionov: " + resultados.length);
                }

                // Actualizar estado en expediente
                String sqlActualizar = "UPDATE expediente SET evaluacionov = 'realizada' WHERE idExpediente = ?";
                try (PreparedStatement psUpdate = conexionBD.prepareStatement(sqlActualizar)) {
                    psUpdate.setInt(1, idExpediente);
                    int filas = psUpdate.executeUpdate();
                    if (filas != 1) {
                        conexionBD.rollback();
                        resultado.setError(true);
                        resultado.setMensaje("No se pudo actualizar el estado del expediente.");
                        return resultado;
                    }
                }

                conexionBD.commit();
                resultado.setError(false);
                resultado.setMensaje("Evaluación registrada correctamente.");

            } catch (SQLException ex) {
                if (conexionBD != null) conexionBD.rollback();
                throw ex;
            } finally {
                if (conexionBD != null) {
                    conexionBD.setAutoCommit(true);
                    conexionBD.close();
                }
            }
        } else {
            resultado.setError(true);
            resultado.setMensaje("Sin conexión a la base de datos.");
        }

        return resultado;
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

    /**
     * Cambia el estado de la evaluación OV a "sin realizar" para un expediente específico.
     *
     * @param idExpediente el ID del expediente a modificar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public static boolean marcarEvaluacionOVComoNoRealizada(int idExpediente) {
        String sql = "UPDATE expediente SET evaluacionov = 'sin realizar' WHERE idExpediente = ?";

        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idExpediente);
            int filas = ps.executeUpdate();
            return filas == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
