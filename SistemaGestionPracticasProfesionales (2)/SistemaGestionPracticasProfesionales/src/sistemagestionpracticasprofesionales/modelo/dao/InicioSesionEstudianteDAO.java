/**
 * Nombre del archivo: InicioSesionEstudianteDAO.java
 * Autor: Juan Pablo Silva Miranda
 * Fecha: [fecha actual o la que desees documentar]
 * Descripción: Clase DAO responsable de verificar las credenciales de inicio de sesión
 *              de los estudiantes en el sistema. Accede a la base de datos para autenticar
 *              a un estudiante mediante su matrícula y contraseña.
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase DAO que permite verificar las credenciales de un estudiante al momento de iniciar sesión.
 * Consulta la base de datos y retorna los datos del estudiante si las credenciales son válidas.
 */
public class InicioSesionEstudianteDAO {
    
    /**
     * Verifica las credenciales ingresadas por un estudiante.
     *
     * @param matricula Matrícula del estudiante.
     * @param password  Contraseña del estudiante.
     * @return Objeto Estudiante con los datos recuperados si las credenciales son válidas;
     *         null si no coinciden.
     * @throws SQLException Si ocurre un error de conexión o durante la consulta a la base de datos.
     */
    public static Estudiante verificarCredenciales(String matricula, String password) throws SQLException{
        Estudiante usuarioSesion = null;
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null){
            String consulta= "SELECT idEstudiante, nombre, apellidoPaterno, apellidoMaterno, matricula FROM estudiante WHERE matricula = ? AND contrasenia = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            sentencia.setString(2, password);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()){
                usuarioSesion= convertirRegistroEstudiante(resultado);
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
     * Convierte un registro de la tabla estudiante en un objeto Estudiante.
     *
     * @param resultado ResultSet que contiene los datos del estudiante.
     * @return Objeto Estudiante con los datos extraídos del ResultSet.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
    private static Estudiante convertirRegistroEstudiante (ResultSet resultado) throws SQLException{
        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
        estudiante.setNombre(resultado.getString("nombre"));
        estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno")!=null ? resultado.getString("apellidoMaterno") : " ");
        estudiante.setMatricula(resultado.getString("matricula"));
        return estudiante;
    }
}
