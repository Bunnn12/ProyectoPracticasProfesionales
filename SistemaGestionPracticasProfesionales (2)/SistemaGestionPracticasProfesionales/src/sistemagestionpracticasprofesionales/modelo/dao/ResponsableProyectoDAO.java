/**
 * Nombre del archivo: ResponsableProyectoDAO.java
 * Autor: Juan Pablo Silva Miranda
 * Fecha: 10/06/25
 * Descripción: Clase DAO encargada de gestionar el acceso a los datos relacionados con los responsables de proyecto
 * en la base de datos del sistema de gestión de prácticas profesionales.
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 * Clase DAO que permite registrar responsables de proyecto, así como obtener listas
 * de responsables con o sin asignación a proyectos, filtrando también por organización vinculada.
 */
public class ResponsableProyectoDAO {
    
    /**
     * Registra un nuevo responsable de proyecto en la base de datos.
     * 
     * @param responsableProyecto Objeto con los datos del responsable de proyecto a registrar.
     * @return Objeto ResultadoOperacion con el estado de éxito o error de la operación.
     * @throws SQLException Si ocurre un error al acceder a la base de datos o no hay conexión.
     */
     public static ResultadoOperacion registrarResponsableProyecto(ResponsableProyecto responsableProyecto) throws SQLException{
        ResultadoOperacion resultado= new ResultadoOperacion();
        Connection conexionBD= Conexion.abrirConexion();
        if(conexionBD!= null){
            String consulta= "INSERT INTO responsableProyecto (nombre, correo, telefono, idOrganizacionVinculada) VALUES (?, ?, ?, ?);";
            PreparedStatement prepararSentencia= conexionBD.prepareStatement(consulta);
            prepararSentencia.setString(1, responsableProyecto.getNombre());
            prepararSentencia.setString(2, responsableProyecto.getCorreo());
            prepararSentencia.setString(3, responsableProyecto.getTelefono());
            prepararSentencia.setInt(4, responsableProyecto.getIdOrganizacionVinculada());
            int filasAfctadas= prepararSentencia.executeUpdate();
            if (filasAfctadas== 1){
                resultado.setError(false);
                resultado.setMensaje("Responsable de proyecto, registrado correctamente");
            }
            else{
                resultado.setError(true);
                resultado.setMensaje("Lo sentimos :( por el momento no se puede registrar la información del responsable de proyecto");
            }
            prepararSentencia.close();
            conexionBD.close();
        } else{
            throw new SQLException("Sin conexion con la base de datos");
        }
        return resultado;
    }
     
    /**
     * Obtiene la lista completa de responsables de proyecto registrados, incluyendo el nombre
     * de la organización vinculada a la que pertenecen.
     * 
     * @return Lista de objetos ResponsableProyecto.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    private static ArrayList<ResponsableProyecto> obtenerResponsablesProyecto() throws SQLException{
        ArrayList<ResponsableProyecto> responsablesProyecto= new ArrayList<>();
        Connection conexionBD= Conexion.abrirConexion();
        if (conexionBD!= null){
            String consulta= "SELECT rp.idResponsableProyecto, rp.idOrganizacionVinculada, rp.nombre, rp.correo, rp.telefono, "
                            + "ov.nombre AS 'organizacionVinculada' "
                            + "FROM responsableProyecto rp "
                            + "JOIN organizacionVinculada ov ON rp.idOrganizacionVinculada = ov.idOrganizacionVinculada;";
            PreparedStatement sentencia= conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                responsablesProyecto.add(convertirRegistroResponsable(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexion con la base de datos");
        }
        return responsablesProyecto;
    }
    
    /**
     * Obtiene la lista de responsables de proyecto que no están asignados a ningún proyecto.
     * 
     * @return Lista de objetos ResponsableProyecto sin proyecto asignado.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public static ArrayList<ResponsableProyecto> obtenerResponsablesSinProyecto() throws SQLException {
        ArrayList<ResponsableProyecto> responsablesProyecto = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT rp.idResponsableProyecto, rp.idOrganizacionVinculada, " +
                              "rp.nombre, rp.correo, rp.telefono, " +
                              "ov.nombre AS 'organizacionVinculada' " +
                              "FROM responsableProyecto rp " +
                              "JOIN organizacionVinculada ov ON rp.idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                              "WHERE rp.idResponsableProyecto NOT IN ( " +
                              "    SELECT DISTINCT idResponsableProyecto FROM proyecto" +
                              ");";

            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                responsablesProyecto.add(convertirRegistroResponsable(resultado));
            }

            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexion con la base de datos");
        }

        return responsablesProyecto;
    }

    /**
     * Obtiene la lista de responsables de proyecto que no están asignados a ningún proyecto
     * y pertenecen a una organización vinculada específica.
     * 
     * @param idOrganizacionVinculada ID de la organización vinculada.
     * @return Lista de objetos ResponsableProyecto sin proyecto asignado de esa organización.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public static ArrayList<ResponsableProyecto> obtenerResponsableSinProyectoDeUnaOV(int idOrganizacionVinculada) throws SQLException {
        ArrayList<ResponsableProyecto> responsablesProyecto = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT rp.idResponsableProyecto, rp.idOrganizacionVinculada, " +
                              "rp.nombre, rp.correo, rp.telefono, " +
                              "ov.nombre AS 'organizacionVinculada' " +
                              "FROM responsableProyecto rp " +
                              "JOIN organizacionVinculada ov ON rp.idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                              "WHERE rp.idResponsableProyecto NOT IN ( " +
                              "    SELECT DISTINCT idResponsableProyecto FROM proyecto" +
                              ")" +
                              "AND rp.idOrganizacionVinculada = ?;";
            
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idOrganizacionVinculada);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                responsablesProyecto.add(convertirRegistroResponsable(resultado));
            }

            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexion con la base de datos");
        }

        return responsablesProyecto;
    }
      
    /**
     * Convierte un registro de la base de datos en un objeto ResponsableProyecto.
     * 
     * @param resultado ResultSet con los datos de un responsable de proyecto.
     * @return Objeto ResponsableProyecto con los datos mapeados.
     * @throws SQLException Si ocurre un error al obtener los datos del ResultSet.
     */
    private static ResponsableProyecto convertirRegistroResponsable(ResultSet resultado) throws SQLException{
        ResponsableProyecto responsableProyecto= new ResponsableProyecto();
        responsableProyecto.setIdResponsable(resultado.getInt("idResponsableProyecto"));
        responsableProyecto.setIdOrganizacionVinculada(resultado.getInt("idOrganizacionVinculada"));
        responsableProyecto.setNombre(resultado.getString("nombre"));
        responsableProyecto.setCorreo(resultado.getString("correo"));
        responsableProyecto.setTelefono(resultado.getString("telefono"));
        responsableProyecto.setNombreOrganizacionVinculada(resultado.getString("organizacionVinculada"));
        return responsableProyecto;
    }
}
