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
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;

/**
 *
 * @author reino
 */
public class ProyectoDAO {
    
        public static ArrayList<Proyecto> buscarProyectoPorNombre(String filtroNombre) throws SQLException {
        ArrayList<Proyecto> proyectos = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT p.idProyecto, p.nombre, p.descripcion, p.fechaInicio, p.fechaFin, "
                    + "p.idOrganizacionVinculada, p.idResponsableProyecto, p.cantidadEstudiantesParticipantes, "
                    + "rp.nombre AS nombreResponsable, ov.nombre AS nombreOrganizacion "
                    + "FROM proyecto p "
                    + "JOIN responsableProyecto rp ON p.idResponsableProyecto = rp.idResponsableProyecto "
                    + "JOIN organizacionVinculada ov ON p.idOrganizacionVinculada = ov.idOrganizacionVinculada "
                    + "LEFT JOIN ( "
                    + "    SELECT idProyecto, COUNT(*) AS estudiantesAsignados "
                    + "    FROM asignacion "
                    + "    GROUP BY idProyecto "
                    + ") a ON p.idProyecto = a.idProyecto "
                    + "WHERE IFNULL(a.estudiantesAsignados, 0) < p.cantidadEstudiantesParticipantes "
                    + "AND p.nombre LIKE ?";

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

    private static Proyecto convertirRegistroProyecto(ResultSet resultado) throws SQLException {
        Proyecto proyecto = new Proyecto();
        proyecto.setIdProyecto(resultado.getInt("idProyecto"));
        proyecto.setNombre(resultado.getString("nombre"));
        proyecto.setDescripcion(resultado.getString("descripcion"));
        proyecto.setFechaInicio(resultado.getString("fechaInicio"));
        proyecto.setFechaFin(resultado.getString("fechaFin"));
        proyecto.setIdOrganizacionVinculada(resultado.getInt("idOrganizacionVinculada"));
        proyecto.setIdResponsableProyecto(resultado.getInt("idResponsableProyecto"));
        proyecto.setCantidadEstudiantesParticipantes(resultado.getInt("cantidadEstudiantesParticipantes"));
        proyecto.setNombreResponsable(resultado.getString("nombreResponsable"));
        proyecto.setNombreOrganizacion(resultado.getString("nombreOrganizacion"));
        return proyecto;
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

}

