/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import com.mysql.cj.protocol.Resultset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author reino
 */
public class ProyectoDAO {

    public static ResultadoOperacion registrarProyecto(Proyecto proyecto) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
            String consulta = "INSERT INTO proyecto (nombre, idOrganizacionVinculada, idResponsableProyecto, fechaInicio, fechaFin, horaEntrada, horaSalida, cantidadEstudiantesParticipantes, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement prepararSentencia= conexionBD.prepareStatement(consulta);
            prepararSentencia.setString(1, proyecto.getNombre());
            prepararSentencia.setInt(2, proyecto.getIdOrganizacionVinculada());
            prepararSentencia.setInt(3, proyecto.getIdResponsableProyecto());
            prepararSentencia.setString(4, proyecto.getFechaInicio());
            prepararSentencia.setString(5, proyecto.getFechaFin());
            prepararSentencia.setString(6, proyecto.getHoraEntrada());
            prepararSentencia.setString(7, proyecto.getHoraSalida());
            prepararSentencia.setInt(8, proyecto.getCantidadEstudiantesParticipantes());
            prepararSentencia.setString(9, proyecto.getDescripcion());
            
            int filasAfectadas = prepararSentencia.executeUpdate();
            if (filasAfectadas == 1){
                resultado.setError(false);
                resultado.setMensaje("Proyecto, registrado correctamente");
            }
            else{
                resultado.setError(true);
                resultado.setMensaje("Lo sentimos :( por el momento no se puede registrar la información del proyecto");
            }
            prepararSentencia.close();
            conexionBD.close();
        } else{
            throw new SQLException("Sin conexion con la base de datos");
        }
        return resultado;
    }
    
    public static ArrayList<Proyecto> buscarProyectoPorNombre(String filtroNombre) throws SQLException {
        ArrayList<Proyecto> proyectos = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT p.idProyecto, p.nombre, p.descripcion, p.fechaInicio, p.fechaFin, "
                            + "p.horaEntrada, p.horaSalida, p.idOrganizacionVinculada, p.idResponsableProyecto, "
                            + "p.cantidadEstudiantesParticipantes, "
                            + "rp.nombre AS nombreResponsable, ov.nombre AS nombreOrganizacion, "
                            + "IFNULL(a.estudiantesAsignados, 0) AS estudiantesAsignados "
                            + "FROM proyecto p "
                            + "JOIN responsableProyecto rp ON p.idResponsableProyecto = rp.idResponsableProyecto "
                            + "JOIN organizacionVinculada ov ON p.idOrganizacionVinculada = ov.idOrganizacionVinculada "
                            + "LEFT JOIN ( "
                            + "    SELECT idProyecto, COUNT(*) AS estudiantesAsignados "
                            + "    FROM asignacion "
                            + "    GROUP BY idProyecto "
                            + ") a ON p.idProyecto = a.idProyecto "
                            + "WHERE p.nombre LIKE ?";

            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, "%" + filtroNombre + "%");
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                proyectos.add(convertirRegistroProyecto(resultado));
            }

            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }

        return proyectos;
    }

    public static boolean asignarProyectoAEstudiante(String matriculaEstudiante, int idProyecto) throws SQLException {
    Connection conexion = Conexion.abrirConexion();

    if (conexion != null) {
        try {
            String consultaMaxParticipantes = "SELECT cantidadEstudiantesParticipantes FROM proyecto WHERE idProyecto = " + idProyecto;
            PreparedStatement psMaxParticipantes = conexion.prepareStatement(consultaMaxParticipantes);
            ResultSet rsMaxParticipantes = psMaxParticipantes.executeQuery();

            int maxParticipantes = 0;
            if (rsMaxParticipantes.next()) {
                maxParticipantes = rsMaxParticipantes.getInt("cantidadEstudiantesParticipantes");
            } else {
                rsMaxParticipantes.close();
                psMaxParticipantes.close();
                conexion.close();
                return false;
            }
            rsMaxParticipantes.close();
            psMaxParticipantes.close();

            String consultaCantidadAsignados = "SELECT COUNT(*) AS cantidadAsignados FROM asignacion WHERE idProyecto = " + idProyecto;
            PreparedStatement psCantidadAsignados = conexion.prepareStatement(consultaCantidadAsignados);
            ResultSet rsCantidadAsignados = psCantidadAsignados.executeQuery();

            int cantidadAsignados = 0;
            if (rsCantidadAsignados.next()) {
                cantidadAsignados = rsCantidadAsignados.getInt("cantidadAsignados");
            }
            rsCantidadAsignados.close();
            psCantidadAsignados.close();

            if (cantidadAsignados >= maxParticipantes) {
                conexion.close();
                return false; // No hay cupo disponible
            }

            String consultaInsertarAsignacion = "INSERT INTO asignacion (idProyecto, idEstudiante) "
                    + "SELECT " + idProyecto + ", e.idEstudiante FROM estudiante e "
                    + "WHERE e.matricula = '" + matriculaEstudiante + "' "
                    + "AND e.idEstudiante NOT IN (SELECT idEstudiante FROM asignacion WHERE idProyecto = " + idProyecto + ")";

            PreparedStatement psInsertarAsignacion = conexion.prepareStatement(consultaInsertarAsignacion);
            int filasInsertadas = psInsertarAsignacion.executeUpdate();
            psInsertarAsignacion.close();
            conexion.close();

            return filasInsertadas > 0;

        } catch (SQLException e) {
            conexion.close();
            throw e;
        }
    } else {
        throw new SQLException("Sin conexión con la base de datos");
    }
}
        
    public static ArrayList<Proyecto> obtenerProyectos() throws SQLException{
        ArrayList<Proyecto> proyectos= new ArrayList<>();
        Connection conexionBD= Conexion.abrirConexion();
        if (conexionBD!= null){
            String consulta= "SELECT p.idProyecto, p.nombre, p.descripcion, " +
                            "p.fechaInicio, p.fechaFin, p.horaEntrada, p.horaSalida, " +
                            "p.cantidadEstudiantesParticipantes, p.idOrganizacionVinculada, " +
                            "p.idResponsableProyecto, ov.nombre AS nombreOrganizacion, " +
                            "rp.nombre AS nombreResponsable, " +
                            "IFNULL(a.estudiantesAsignados, 0) AS estudiantesAsignados " +
                            "FROM proyecto p " +
                            "JOIN organizacionVinculada ov ON p.idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                            "JOIN responsableProyecto rp ON p.idResponsableProyecto = rp.idResponsableProyecto " +
                            "LEFT JOIN ( " +
                            "    SELECT idProyecto, COUNT(*) AS estudiantesAsignados " +
                            "    FROM asignacion " +
                            "    GROUP BY idProyecto " +
                            ") a ON p.idProyecto = a.idProyecto";
            PreparedStatement sentencia= conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                proyectos.add(convertirRegistroProyecto(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexion con la base de datos");
        }
        return proyectos;
    }
     private static Proyecto convertirRegistroProyecto(ResultSet resultado) throws SQLException{
        Proyecto proyecto= new Proyecto();
        proyecto.setIdProyecto(resultado.getInt("idProyecto"));
        proyecto.setNombre(resultado.getString("nombre"));
        proyecto.setDescripcion(resultado.getString("descripcion"));
        proyecto.setFechaInicio(resultado.getString("fechaInicio"));
        proyecto.setFechaFin(resultado.getString("fechaFin"));
        proyecto.setCantidadEstudiantesParticipantes(resultado.getInt("cantidadEstudiantesParticipantes"));
        proyecto.setHoraEntrada(resultado.getString("horaEntrada"));
        proyecto.setHoraSalida(resultado.getString("horaSalida"));
        proyecto.setIdOrganizacionVinculada(resultado.getInt("idOrganizacionVinculada"));
        proyecto.setIdResponsableProyecto(resultado.getInt("idResponsableProyecto"));
        proyecto.setNombreOrganizacion(resultado.getString("nombreOrganizacion"));
        proyecto.setNombreResponsable(resultado.getString("nombreResponsable"));
        proyecto.setEstudiantesAsignados(resultado.getInt("estudiantesAsignados"));
        return proyecto;
    }
     public static boolean actualizarResponsableDeProyecto(int idProyecto, int idNuevoResponsable) throws SQLException {
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
            String consulta = "UPDATE proyecto SET idResponsableProyecto = ? WHERE idProyecto = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idNuevoResponsable);
            sentencia.setInt(2, idProyecto);

            int filasModificadas = sentencia.executeUpdate();
            sentencia.close();
            conexionBD.close();

            return filasModificadas > 0;
        } else {
            throw new SQLException("No hay conexión con la base de datos");
        }
    }

}

