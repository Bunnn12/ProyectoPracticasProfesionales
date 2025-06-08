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
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;

/**
 *
 * @author reino
 */
public class EstudianteDAO {
        public static Estudiante convertirRegistroEstudiante(ResultSet resultado) throws SQLException{
            Estudiante estudiante = new Estudiante();
            estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
            estudiante.setNombre(resultado.getString("nombre"));
            estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
            estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
            estudiante.setCorreo(resultado.getString("correo"));
            estudiante.setMatricula(resultado.getString("matricula"));
            estudiante.setIdGrupo(resultado.getInt("grupo"));
            return estudiante;
        }
        
        public static ArrayList<Estudiante> buscarEstudiantesSinProyectoPorMatricula(String matriculaBusqueda) throws SQLException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        Connection conexion = Conexion.abrirConexion();
        if (conexion != null) {
            String consulta = "SELECT e.idEstudiante, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.correo, e.matricula "
                    + "FROM estudiante e "
                    + "WHERE e.idEstudiante NOT IN (SELECT a.idEstudiante FROM asignacion a) "
                    + "AND e.matricula LIKE ?";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, "%" + matriculaBusqueda + "%");
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                estudiantes.add(convertirRegistroEstudiante(resultado));
            }
            resultado.close();
            sentencia.close();
            conexion.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }
        return estudiantes;
    }
        public static ArrayList<Estudiante> obtenerEstudiantesPeriodoActual() throws SQLException{
        ArrayList<Estudiante> estudiantes= new ArrayList<>();
        Connection conexionBD= Conexion.abrirConexion();
        if (conexionBD!= null){
            String consulta= "SELECT e.idEstudiante, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.matricula, e.correo, e.idGrupo AS 'grupo' " +
             "FROM estudiante e " +
             "JOIN grupo g ON e.idGrupo = g.idGrupo " +
             "JOIN periodo p ON g.idPeriodo = p.idPeriodo " +
             "WHERE CURRENT_DATE BETWEEN p.fechaInicio AND p.fechaFin";
            PreparedStatement sentencia= conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                estudiantes.add(convertirRegistroEstudiante(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexion con la base de datos");
        }
        return estudiantes;
        }


}

