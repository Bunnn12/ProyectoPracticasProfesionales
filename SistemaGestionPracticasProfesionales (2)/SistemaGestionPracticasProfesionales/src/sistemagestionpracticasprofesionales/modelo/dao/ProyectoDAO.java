/**
 * Nombre del archivo: ProyectoDAO.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 10/06/25
 * Descripción: Clase DAO encargada de gestionar el acceso a los datos relacionados con los proyectos
 * en la base de datos del sistema de gestión de prácticas profesionales.
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 * Clase DAO que permite realizar operaciones de lectura, escritura y actualización
 * sobre los proyectos registrados en la base de datos del sistema.
 */
public class ProyectoDAO {

    /**
     * Registra un nuevo proyecto en la base de datos.
     * 
     * @param proyecto Objeto Proyecto con los datos a registrar.
     * @return ResultadoOperacion indicando éxito o error de la operación.
     * @throws SQLException Si ocurre un error en la conexión o ejecución SQL.
     */
    public static ResultadoOperacion registrarProyecto(Proyecto proyecto) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
            String consulta = "INSERT INTO proyecto (nombre, idOrganizacionVinculada, idResponsableProyecto, fechaInicio, fechaFin, cantidadEstudiantesParticipantes, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
            prepararSentencia.setString(1, proyecto.getNombre());
            prepararSentencia.setInt(2, proyecto.getIdOrganizacionVinculada());
            prepararSentencia.setInt(3, proyecto.getIdResponsableProyecto());
            prepararSentencia.setString(4, proyecto.getFechaInicio());
            prepararSentencia.setString(5, proyecto.getFechaFin());
            prepararSentencia.setInt(6, proyecto.getCantidadEstudiantesParticipantes());
            prepararSentencia.setString(7, proyecto.getDescripcion());
            
            int filasAfectadas = prepararSentencia.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = prepararSentencia.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    proyecto.setIdProyecto(idGenerado); // ← ¡Importante!
                    return new ResultadoOperacion(false, "Proyecto registrado con ID " + idGenerado);
                }
            }
        }
        return new ResultadoOperacion(true, "No se pudo obtener el ID generado");
    }
    
    public static ResultadoOperacion eliminarProyecto(int idProyecto) throws SQLException {
        String consulta = "DELETE FROM proyecto WHERE idProyecto = ?";
    
        try (Connection conexionBD = Conexion.abrirConexion();
             PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta)) {

            prepararSentencia.setInt(1, idProyecto);
            int filas = prepararSentencia.executeUpdate();

            if (filas > 0) {
                return new ResultadoOperacion(false, "Proyecto eliminado correctamente");
            } else {
                return new ResultadoOperacion(true, "No se encontró el proyecto para eliminar");
            }
        }
    }
    
    
    public static ResultadoOperacion registrarHorario(Proyecto proyecto, String dia, String entrada, String salida) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
            String consulta = "INSERT INTO horario (dia, entrada, salida, idProyecto) VALUES (?, ?, ?, ?);";
            PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
            prepararSentencia.setString(1, dia);
            prepararSentencia.setString(2, entrada);
            prepararSentencia.setString(3, salida);
            prepararSentencia.setInt(4, proyecto.getIdProyecto());
            
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
    
    
    
    
    /**
     * Busca proyectos cuyo nombre contenga un filtro dado (búsqueda parcial).
     * 
     * @param filtroNombre Cadena para filtrar proyectos por nombre (LIKE '%filtro%').
     * @return Lista de proyectos que cumplen con el filtro.
     * @throws SQLException Si hay error en la conexión o consulta SQL.
     */
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

    /**
     * Asigna un proyecto a un estudiante dado su número de matrícula.
     * Valida que no se exceda el número máximo de estudiantes permitidos en el proyecto.
     * 
     * @param matriculaEstudiante Matrícula del estudiante a asignar.
     * @param idProyecto Identificador del proyecto.
     * @return true si la asignación fue exitosa, false si no hay cupo o ya está asignado.
     * @throws SQLException Si hay errores en la conexión o ejecución SQL.
     */
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
       
    /**
     * Obtiene la lista completa de proyectos registrados, incluyendo información
     * de responsables, organizaciones vinculadas y cantidad de estudiantes asignados.
     * 
     * @return Lista de todos los proyectos con detalles.
     * @throws SQLException Si ocurre un error en la consulta o conexión.
     */
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
    
    /**
     * Convierte un registro obtenido de la consulta SQL en un objeto Proyecto.
     * 
     * @param resultado ResultSet apuntando al registro actual.
     * @return Proyecto con los datos del registro.
     * @throws SQLException Si ocurre un error al leer los datos del ResultSet.
     */
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
     
     /**
     * Actualiza el responsable asignado a un proyecto específico.
     * 
     * @param idProyecto Identificador del proyecto.
     * @param idNuevoResponsable Identificador del nuevo responsable.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si hay problemas con la conexión o ejecución SQL.
     */
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

    /**
    * Verifica si un proyecto específico está asignado a un estudiante determinado.
    * 
    * Este método consulta la tabla {@code asignacion} para determinar si existe un registro
    * que relacione al estudiante con el proyecto indicado.
    * 
    * @param idEstudiante ID del estudiante a verificar.
    * @param idProyecto ID del proyecto que se desea comprobar.
    * @return {@code true} si el proyecto está asignado al estudiante; {@code false} en caso contrario.
    * @throws SQLException Si ocurre un error al conectar o consultar la base de datos.
    */
    public static boolean esProyectoDeEstudiante(int idEstudiante, int idProyecto) throws SQLException {
        Connection conexion = Conexion.abrirConexion();
        boolean pertenece = false;

        if (conexion != null) {
            String sql = "SELECT 1 FROM asignacion WHERE idEstudiante = ? AND idProyecto = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idProyecto);
            ResultSet rs = ps.executeQuery();
            pertenece = rs.next();

            rs.close();
            ps.close();
            conexion.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }

        return pertenece;
    }
    public static ArrayList<Proyecto> obtenerProyectosPorEstudiante(int idEstudiante) throws SQLException {
    ArrayList<Proyecto> proyectos = new ArrayList<>();
    Connection conexionBD = Conexion.abrirConexion();

    if (conexionBD != null) {
        String consulta = "SELECT p.idProyecto, p.nombre, p.descripcion, p.fechaInicio, p.fechaFin, " +
                          "p.horaEntrada, p.horaSalida, p.idOrganizacionVinculada, p.idResponsableProyecto, " +
                          "p.cantidadEstudiantesParticipantes, " +
                          "rp.nombre AS nombreResponsable, ov.nombre AS nombreOrganizacion, " +
                          "IFNULL(a.estudiantesAsignados, 0) AS estudiantesAsignados " +
                          "FROM proyecto p " +
                          "JOIN asignacion asi ON p.idProyecto = asi.idProyecto " +
                          "JOIN responsableProyecto rp ON p.idResponsableProyecto = rp.idResponsableProyecto " +
                          "JOIN organizacionVinculada ov ON p.idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                          "LEFT JOIN ( " +
                          "    SELECT idProyecto, COUNT(*) AS estudiantesAsignados " +
                          "    FROM asignacion " +
                          "    GROUP BY idProyecto " +
                          ") a ON p.idProyecto = a.idProyecto " +
                          "WHERE asi.idEstudiante = ?";

        PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
        sentencia.setInt(1, idEstudiante);
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
/**
 * Obtiene el proyecto asignado a un estudiante, incluyendo información 
 * de la organización vinculada y el responsable.
 * 
 * @param idEstudiante ID del estudiante autenticado en sesión.
 * @return Proyecto asignado al estudiante o {@code null} si no se encontró.
 * @throws SQLException Si hay problemas al conectar o consultar la base de datos.
 */
public static Proyecto obtenerProyectoPorEstudiante(int idEstudiante) throws SQLException {
    Proyecto proyecto = null;
    Connection conexionBD = Conexion.abrirConexion();

    if (conexionBD != null) {
        String consulta = "SELECT p.idProyecto, p.nombre, p.descripcion, p.fechaInicio, p.fechaFin, " +
                          "p.horaEntrada, p.horaSalida, p.idOrganizacionVinculada, p.idResponsableProyecto, " +
                          "p.cantidadEstudiantesParticipantes, " +
                          "ov.nombre AS nombreOrganizacion, rp.nombre AS nombreResponsable, " +
                          "IFNULL(a.estudiantesAsignados, 0) AS estudiantesAsignados " +
                          "FROM proyecto p " +
                          "JOIN asignacion asi ON p.idProyecto = asi.idProyecto " +
                          "JOIN organizacionVinculada ov ON p.idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                          "JOIN responsableProyecto rp ON p.idResponsableProyecto = rp.idResponsableProyecto " +
                          "LEFT JOIN ( " +
                          "    SELECT idProyecto, COUNT(*) AS estudiantesAsignados " +
                          "    FROM asignacion " +
                          "    GROUP BY idProyecto " +
                          ") a ON p.idProyecto = a.idProyecto " +
                          "WHERE asi.idEstudiante = ? LIMIT 1";

        PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
        sentencia.setInt(1, idEstudiante);
        ResultSet resultado = sentencia.executeQuery();

        if (resultado.next()) {
            proyecto = convertirRegistroProyecto(resultado);
        }

        resultado.close();
        sentencia.close();
        conexionBD.close();
    } else {
        throw new SQLException("Sin conexión con la base de datos");
    }

    return proyecto;
}


}

