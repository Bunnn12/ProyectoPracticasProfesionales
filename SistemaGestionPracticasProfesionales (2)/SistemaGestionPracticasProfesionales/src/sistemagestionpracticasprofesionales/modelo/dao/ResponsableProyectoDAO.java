/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author reino
 */
public class ResponsableProyectoDAO {
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
