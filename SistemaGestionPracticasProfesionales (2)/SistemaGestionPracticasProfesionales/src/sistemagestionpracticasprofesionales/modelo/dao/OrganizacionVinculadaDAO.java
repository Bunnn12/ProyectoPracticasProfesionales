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
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author reino
 */
public class OrganizacionVinculadaDAO {
    public static ResultadoOperacion registrarOV(OrganizacionVinculada ov) throws SQLException{
        ResultadoOperacion resultado= new ResultadoOperacion();
        Connection conexionBD= Conexion.abrirConexion();
        if(conexionBD!= null){
            String consulta= "INSERT INTO organizacionVinculada (nombre, direccion, correo, telefono) VALUES (?, ?, ?, ?);";
            PreparedStatement prepararSentencia= conexionBD.prepareStatement(consulta);
            prepararSentencia.setString(1, ov.getNombre());
            prepararSentencia.setString(2, ov.getDireccion());
            prepararSentencia.setString(3, ov.getCorreo());
            prepararSentencia.setString(4, ov.getTelefono());
            int filasAfctadas= prepararSentencia.executeUpdate();
            if (filasAfctadas== 1){
                resultado.setError(false);
                resultado.setMensaje("Organización Vinculada, registrada correctamente");
            }
            else{
                resultado.setError(true);
                resultado.setMensaje("Lo sentimos :( por el momento no se puede registrar la información de la organizacion Vinculada");
            }
            prepararSentencia.close();
            conexionBD.close();
        } else{
            throw new SQLException("Sin conexion con la base de datos");
        }
        return resultado;
    }
    
    private static ArrayList<OrganizacionVinculada> obtenerOrganizacionesVinculadas() throws SQLException{
        ArrayList<OrganizacionVinculada> organizacionesVinculadas= new ArrayList<>();
        Connection conexionBD= Conexion.abrirConexion();
        if (conexionBD!= null){
            String consulta= "SELECT idOrganizacionVinculada, nombre, direccion, correo, telefono FROM organizacionVinculada";
            PreparedStatement sentencia= conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                organizacionesVinculadas.add(convertirRegistroOV(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexion con la base de datos");
        }
        return organizacionesVinculadas;
    }
        
    private static OrganizacionVinculada convertirRegistroOV(ResultSet resultado) throws SQLException{
        OrganizacionVinculada ov = new OrganizacionVinculada();
        ov.setIdOrganizacionVinculada(resultado.getInt("idOrganizacionVinculada"));
        ov.setNombre(resultado.getString("nombre"));
        ov.setDireccion(resultado.getString("direccion"));
        ov.setCorreo(resultado.getString("correo"));
        ov.setTelefono(resultado.getString("telefono"));
        return ov;
    }
}
