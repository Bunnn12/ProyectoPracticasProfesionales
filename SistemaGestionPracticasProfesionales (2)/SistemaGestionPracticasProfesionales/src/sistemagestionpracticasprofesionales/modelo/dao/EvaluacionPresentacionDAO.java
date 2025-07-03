/**
    * Nombre del archivo: EvaluacionPresentacionDAO.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 16/06/25
    * Descripción: Clase DAO encargada de gestionar el acceso a los datos relacionados con las evaluaciones de las presentaciones de los estudiantes
    * en la base de datos del sistema de gestión de prácticas profesionales.
*/
package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.EvaluacionPresentacion;
import sistemagestionpracticasprofesionales.modelo.pojo.CriterioEvaluacionFila;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * Clase DAO que permite registrar las evaluaciones de las presentaciones de los estudiantes.
 */
public class EvaluacionPresentacionDAO {
    /**
     * Registra la evaluación de presentación de un estudiante en la base de datos.
     * Este método realiza una transacción que incluye:
     * - Obtener el expediente del estudiante en base a su ID.
     * - Insertar los datos de la evaluación de la presentación.
     * - Actualizar el estado del expediente para marcarlo como evaluado.
     * Si alguna de las operaciones falla, se realiza un rollback para evitar cambios parciales.
     * 
     * @param evaluacion Objeto que contiene los datos de la evaluación de la presentación.
     * @param idEstudiante ID del estudiante al que se le registra la evaluación.
     * @return Objeto ResultadoOperacion que que indica si la operación fue exitosa o no, y un mensaje descriptivo.
     * @throws SQLException Si ocurre un error de conexión o durante alguna de las operaciones con la base de datos.
     */
    public static ResultadoOperacion registrarEvaluacionPresentacion(EvaluacionPresentacion evaluacion, int idEstudiante) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
            try {
                conexionBD.setAutoCommit(false); // Inicia transacción para asegurar que todas las operaciones ocurran o ninguna
                
                // Obtener idExpediente
                String consultaExpediente = "SELECT idExpediente FROM expediente WHERE idEstudiante = ?";
                PreparedStatement prepararSentencia1 = conexionBD.prepareStatement(consultaExpediente);
                prepararSentencia1.setInt(1, idEstudiante);
                ResultSet rs = prepararSentencia1.executeQuery();
                if (rs.next()) {
                    int idExpediente = rs.getInt("idExpediente");
                    evaluacion.setIdExpediente(idExpediente);
                } else {
                    resultado.setError(true);
                    resultado.setMensaje("No se encontró expediente del estudiante.");
                    return resultado;
                }
                
                // Insertar evaluación
                String consulta = "INSERT INTO evaluacionpresentacion (puntajeTotalObtenido, retroalimentacion, idExpediente) VALUES (?, ?, ?);";
                PreparedStatement prepararSentencia2 = conexionBD.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia2.setDouble(1, evaluacion.getPuntajeTotalObtenido());
                prepararSentencia2.setString(2, evaluacion.getRetroalimentacion());
                prepararSentencia2.setInt(3, evaluacion.getIdExpediente());
                int filasInsertadas = prepararSentencia2.executeUpdate();
                ResultSet generatedKeys = prepararSentencia2.getGeneratedKeys();
                if (generatedKeys.next()) {
                    evaluacion.setIdEvaluacionPresentacion(generatedKeys.getInt(1)); // Asigna el ID a tu objeto
                }
                
                // Actualizar expediente
                String actualizarExpediente = "UPDATE expediente SET evaluacionpresentacion = 'evaluado' WHERE idExpediente = ?;";
                PreparedStatement prepararSentencia3 = conexionBD.prepareStatement(actualizarExpediente);
                prepararSentencia3.setInt(1, evaluacion.getIdExpediente());
                int filasActualizadas = prepararSentencia3.executeUpdate();
                
                if (filasInsertadas == 1 && filasActualizadas == 1) {
                    conexionBD.commit();
                    resultado.setError(false);
                    resultado.setMensaje("Evaluación registrada correctamente.");
                } else {
                    conexionBD.rollback();
                    resultado.setError(true);
                    resultado.setMensaje("No se pudo registrar la evaluación.");
                }
                
                rs.close();
                prepararSentencia1.close();
                prepararSentencia2.close();
                prepararSentencia3.close();
                conexionBD.close();
            } catch (SQLException e) {
                conexionBD.rollback(); // En caso de error, revertir cambios
                throw e;
            }
        } else{
            throw new SQLException("Sin conexion con la base de datos");
        }
        return resultado;
    }
    
    public static void registrarDetallesEvaluacion(int idEvaluacionPresentacion, List<CriterioEvaluacionFila> criterios) throws SQLException {
        Connection conexionBD = Conexion.abrirConexion();
        String consulta = "INSERT INTO detalleevaluacionpresentacion (idEvaluacionPresentacion, idCriterio, puntajeObtenido) VALUES (?, ?, ?)";
        PreparedStatement ps = conexionBD.prepareStatement(consulta);

        for (CriterioEvaluacionFila fila : criterios) {
            ps.setInt(1, idEvaluacionPresentacion);
            ps.setInt(2, fila.getIdCriterio());
            ps.setDouble(3, fila.getCalificacionSeleccionada());
            ps.addBatch();
        }

        ps.executeBatch();
        ps.close();
        conexionBD.close();
    }
}