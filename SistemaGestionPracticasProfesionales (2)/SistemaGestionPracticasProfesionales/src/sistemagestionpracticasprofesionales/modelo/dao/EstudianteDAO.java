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
import sistemagestionpracticasprofesionales.modelo.pojo.DatosDocumentoAsignacion;
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
            estudiante.setIdGrupo(resultado.getInt("idGrupo"));
            estudiante.setGrupo(resultado.getString("grupo"));
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
        public static ArrayList<Estudiante> obtenerEstudiantesPeriodoActualConProyecto() throws SQLException{
            ArrayList<Estudiante> estudiantes= new ArrayList<>();
            Connection conexionBD= Conexion.abrirConexion();
            if (conexionBD!= null){
                String consulta=  "SELECT DISTINCT e.idEstudiante, e.nombre, e.apellidoPaterno, e.apellidoMaterno, " +
                                "e.matricula, e.correo, g.idGrupo, CONCAT(g.bloque, '-', g.seccion) AS grupo " +
                                "FROM estudiante e " +
                                "JOIN grupo g ON e.idGrupo = g.idGrupo " +
                                "JOIN periodo p ON g.idPeriodo = p.idPeriodo " +
                                "JOIN asignacion a ON e.idEstudiante = a.idEstudiante " +
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
        
        public static ArrayList<DatosDocumentoAsignacion> obtenerDatosDocumentosAsignacion() throws SQLException {
            ArrayList<DatosDocumentoAsignacion> listaDatos = new ArrayList<>();
            Connection conexion = Conexion.abrirConexion();

            if (conexion != null) {
                String consulta = "SELECT " +
                        "CONCAT(e.nombre, ' ', e.apellidoPaterno, ' ', IFNULL(e.apellidoMaterno, '')) AS nombreCompleto, " +
                        "e.matricula, " +
                        "p.nombre AS nombreProyecto, " +
                        "p.fechaInicio, " +
                        "p.fechaFin, " +
                        "TIME_FORMAT(p.horaEntrada, '%H:%i') AS horaEntrada, " +
                        "TIME_FORMAT(p.horaSalida, '%H:%i') AS horaSalida, " +
                        "ov.nombre AS nombreOrganizacion, " +
                        "rp.nombre AS nombreResponsable, " +
                        "rp.correo AS correoResponsable, " +
                        "rp.telefono AS telefonoResponsable " +
                        "FROM estudiante e " +
                        "JOIN asignacion a ON e.idEstudiante = a.idEstudiante " +
                        "JOIN proyecto p ON a.idProyecto = p.idProyecto " +
                        "JOIN grupo g ON e.idGrupo = g.idGrupo " +
                        "JOIN periodo per ON g.idPeriodo = per.idPeriodo " +
                        "JOIN organizacionVinculada ov ON p.idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                        "JOIN responsableProyecto rp ON p.idResponsableProyecto = rp.idResponsableProyecto " +
                        "WHERE CURRENT_DATE BETWEEN per.fechaInicio AND per.fechaFin";

                PreparedStatement sentencia = conexion.prepareStatement(consulta);
                ResultSet resultado = sentencia.executeQuery();

                java.util.HashSet<String> matriculasUnicas = new java.util.HashSet<>();
                while (resultado.next()) {
                    String matricula = resultado.getString("matricula");
                    if (matriculasUnicas.contains(matricula)) {
                        continue; // evitar duplicado
                    }

                    DatosDocumentoAsignacion datos = new DatosDocumentoAsignacion();
                    datos.setNombreCompleto(resultado.getString("nombreCompleto"));
                    datos.setMatricula(matricula);
                    datos.setNombreProyecto(resultado.getString("nombreProyecto"));
                    datos.setFechaInicio(resultado.getString("fechaInicio"));
                    datos.setFechaFin(resultado.getString("fechaFin"));
                    datos.setHoraEntrada(resultado.getString("horaEntrada"));
                    datos.setHoraSalida(resultado.getString("horaSalida"));
                    datos.setNombreOrganizacion(resultado.getString("nombreOrganizacion"));
                    datos.setNombreResponsable(resultado.getString("nombreResponsable"));
                    datos.setCorreoResponsable(resultado.getString("correoResponsable"));
                    datos.setTelefonoResponsable(resultado.getString("telefonoResponsable"));

                    listaDatos.add(datos);
                    matriculasUnicas.add(matricula);
                }

                resultado.close();
                sentencia.close();
                conexion.close();
            } else {
                throw new SQLException("Sin conexión con la base de datos");
            }

            return listaDatos;
        }


}

