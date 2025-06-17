/**
 * Nombre del archivo: InicioSesionUsuarioDAO.java
 * Autor: Rodirgo Santa Bárbara Murrieta
 * Fecha: 08/06/25
 * Descripción: Clase DAO responsable de verificar las credenciales de inicio de sesión
 * de los usuarios en el sistema. Accede a la base de datos para autenticar
 * a un usuario mediante su nombre de usuario y contraseña.
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;

/**
 * Clase DAO que permite verificar las credenciales de un usuario al momento de iniciar sesión.
 * Consulta la base de datos y retorna los datos del usuario si las credenciales son válidas.
 */
public class InicioSesionUsuarioDAO {
    
    /**
     * Verifica las credenciales ingresadas por un usuario.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña del usuario.
     * @return Objeto Usuario con los datos recuperados si las credenciales son válidas;
     *         null si no coinciden.
     * @throws SQLException Si ocurre un error de conexión o durante la consulta a la base de datos.
     */
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
    
    /**
     * Convierte un registro de la tabla usuario en un objeto Usuario.
     *
     * @param resultado ResultSet que contiene los datos del usuario.
     * @return Objeto Usuario con los datos extraídos del ResultSet.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
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
