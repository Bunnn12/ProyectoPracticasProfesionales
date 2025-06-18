/**
    * Nombre del archivo: CriterioEvaluacionDAO.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 16/06/25
    * Descripción: Clase DAO encargada de gestionar el acceso a los datos relacionados con los criterios de evaluación
    * en la base de datos del sistema de gestión de prácticas profesionales.
*/
package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.CriterioEvaluacion;

/**
 *
 * Clase DAO que permite obtener las listas de criterios de evaluación
 * para evaluar presentaciones y para evaluar organizaciones vinculadas.
 */
public class CriterioEvaluacionDAO {
    /**
     * Convierte un registro de cualquiera de las tablas de criterios de evaluación en un objeto CriterioEvaluacion.
     * @param resultado Que contiene los datos del criterio de evaluación obtenidos de la base de datos.
     * @return Objeto CriterioEvaluacion con los datos extraídos del ResultSet.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
    public static CriterioEvaluacion convertirRegistroCriterioEvaluacion(ResultSet resultado) throws SQLException{
        CriterioEvaluacion criterio = new CriterioEvaluacion();
        criterio.setIdCriterio(resultado.getInt("idCriterio"));
        criterio.setNombreCriterio(resultado.getString("nombreCriterio"));
        criterio.setDescripcion(resultado.getString("descripcion"));
        return criterio;
    }
    
    /**
     * Recupera todos los criterios de evaluación para evaluar presentaciones de la base de datos.
     * @return Lista de objetos CriterioEvaluacion correspondientes a los criterios almacenados.
     * @throws SQLException Si no se puede establecer conexión con la base de datos o si ocurre un error durante la consulta.
     */
    public static ArrayList<CriterioEvaluacion> obtenerCriteriosEvaluacionPresentacion() throws SQLException {
        ArrayList<CriterioEvaluacion> criteriosEvaluacionPresentacion = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD!= null){
            String consulta = "SELECT * FROM criterioevaluacionpresentacion;";
        PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
        ResultSet resultado = sentencia.executeQuery();
        while(resultado.next()){
            criteriosEvaluacionPresentacion.add(convertirRegistroCriterioEvaluacion(resultado));
        }
        sentencia.close();
        resultado.close();
        conexionBD.close();
        } else {
            throw new SQLException("Sin conexion con la base de datos");
        }
        return criteriosEvaluacionPresentacion;
    }
    
    /**
     * Recupera todos los criterios de evaluación para evaluar organizaciones vinculadas de la base de datos.
     * @return Lista de objetos {@code CriterioEvaluacion} correspondientes a los criterios almacenados.
     */
    public static List<CriterioEvaluacion> obtenerCriterios() {
        List<CriterioEvaluacion> lista = new ArrayList<>();
        String sql = "SELECT idCriterio, nombreCriterio, descripcion FROM criterioEvaluacionOV";

        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(convertirRegistroCriterioEvaluacion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
