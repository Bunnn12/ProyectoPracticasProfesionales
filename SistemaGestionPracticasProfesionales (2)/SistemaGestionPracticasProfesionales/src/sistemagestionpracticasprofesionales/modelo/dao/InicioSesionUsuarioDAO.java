package sistemagestionpracticasprofesionales.modelo.dao;

import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;

/**
 *
 * @author rodri
 */
public class InicioSesionUsuarioDAO {
public static Usuario verificarCredenciales(String username, String password) throws SQLException{
        Usuario usuarioSesion = null;
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null){
            String consulta= "SELECT idUsuario, nombre, apellidoPaterno, apellidoMaterno, usuario, rol FROM usuario WHERE usuario = ? AND contrasenia = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, username);
            sentencia.setString(2, password);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()){
                usuarioSesion= convertirRegistroUsuario(resultado);
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        }else {
            throw new SQLException("Error: Sin conexión a la base de datos");
        }
        return usuarioSesion;
    }
    
    private static Usuario convertirRegistroUsuario (ResultSet resultado) throws SQLException{
        Usuario usuarioSesion = new Usuario();
        usuarioSesion.setIdUsuario(resultado.getInt("idUsuario"));
        usuarioSesion.setNombre(resultado.getString("nombre"));
        usuarioSesion.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        usuarioSesion.setApellidoMaterno(resultado.getString("apellidoMaterno")!=null ? resultado.getString("apellidoMaterno") : " ");
        usuarioSesion.setUsuario(resultado.getString("usuario"));
        usuarioSesion.setRol(resultado.getString("rol"));
        return usuarioSesion;
    }
}
