package sistemagestionpracticasprofesionales.modelo.dao;

import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author rodri
 */
public class InicioSesionEstudianteDAO {
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
