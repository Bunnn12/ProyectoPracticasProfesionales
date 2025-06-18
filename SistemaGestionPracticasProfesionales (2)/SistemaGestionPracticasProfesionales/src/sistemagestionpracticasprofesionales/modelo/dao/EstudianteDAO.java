/**
 * Nombre del archivo: EstudianteDAO.java  
 * Autor: Astrid Azucena Torres Lagunes  
 * Fecha: 08/06/2025  
 * Descripción: Esta clase proporciona métodos para acceder, consultar y manipular los datos 
 * de los estudiantes en la base de datos del sistema de gestión de prácticas profesionales.
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
import java.time.LocalDate;
import sistemagestionpracticasprofesionales.modelo.pojo.ExpedienteCompleto;

/**
 * Clase que permite realizar operaciones de acceso a datos relacionadas con los estudiantes.
 */
public class EstudianteDAO {
    /**
     * Convierte un registro de resultado de base de datos en un objeto Estudiante.
     * 
     * @param resultado ResultSet obtenido de la base de datos.
     * @return Objeto Estudiante con los datos del registro.
     * @throws SQLException si ocurre un error al obtener los datos.
     */
    public static Estudiante convertirRegistroEstudiante(ResultSet resultado) throws SQLException {
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

    /**
     * Busca estudiantes que no tengan proyecto asignado actualmente y filtra por matrícula.
     * 
     * @param textoBusqueda Texto para filtrar matrícula (puede estar vacío para no filtrar).
     * @return Lista de estudiantes sin proyecto asignado que coinciden con el filtro.
     * @throws SQLException en caso de error con la base de datos.
     */
    public static ArrayList<Estudiante> buscarEstudiantesSinProyectoPorMatricula(String textoBusqueda) throws SQLException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        Connection conexion = Conexion.abrirConexion();

        String consulta = "SELECT e.idEstudiante, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.correo, e.matricula "
                        + "FROM estudiante e "
                        + "JOIN grupo g ON e.idGrupo = g.idGrupo "
                        + "JOIN periodo p ON g.idPeriodo = p.idPeriodo "
                        + "WHERE CURDATE() BETWEEN p.fechaInicio AND p.fechaFin "
                        + "AND e.idEstudiante NOT IN (SELECT idEstudiante FROM asignacion) "
                        + "AND (? = '' OR e.matricula LIKE ?)";

        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, textoBusqueda);
        sentencia.setString(2, "%" + textoBusqueda + "%");

        ResultSet resultado = sentencia.executeQuery();

        while (resultado.next()) {
            Estudiante estudiante = new Estudiante();
            estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
            estudiante.setNombre(resultado.getString("nombre"));
            estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
            estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
            estudiante.setCorreo(resultado.getString("correo"));
            estudiante.setMatricula(resultado.getString("matricula"));
            estudiantes.add(estudiante);
        }

        conexion.close();
        return estudiantes;
    }

    /**
     * Obtiene la lista de estudiantes del periodo actual que ya cuentan con proyecto asignado.
     * 
     * @return Lista de estudiantes con proyecto en periodo actual.
     * @throws SQLException en caso de error con la base de datos o sin conexión.
     */
    public static ArrayList<Estudiante> obtenerEstudiantesPeriodoActualConProyecto() throws SQLException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT DISTINCT e.idEstudiante, e.nombre, e.apellidoPaterno, e.apellidoMaterno, " +
                              "e.matricula, e.correo, g.idGrupo, CONCAT(g.bloque, '-', g.seccion) AS grupo " +
                              "FROM estudiante e " +
                              "JOIN grupo g ON e.idGrupo = g.idGrupo " +
                              "JOIN periodo p ON g.idPeriodo = p.idPeriodo " +
                              "JOIN asignacion a ON e.idEstudiante = a.idEstudiante " +
                              "WHERE CURRENT_DATE BETWEEN p.fechaInicio AND p.fechaFin";

            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                estudiantes.add(convertirRegistroEstudiante(resultado));
            }

            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }

        return estudiantes;
    }

    /**
     * Obtiene los datos necesarios para la generación del documento de asignación.
     * 
     * @return Lista con los datos para documentos de asignación.
     * @throws SQLException en caso de error con la base de datos o sin conexión.
     */
    public static ArrayList<DatosDocumentoAsignacion> obtenerDatosDocumentosAsignacion() throws SQLException {
        ArrayList<DatosDocumentoAsignacion> listaDatos = new ArrayList<>();
        Connection conexion = Conexion.abrirConexion();

        if (conexion != null) {
            String consulta = "SELECT e.idEstudiante, p.idProyecto, " +
                    "CONCAT(e.nombre, ' ', e.apellidoPaterno, ' ', IFNULL(e.apellidoMaterno, '')) AS nombreCompleto, " +
                    "e.matricula, p.nombre AS nombreProyecto, p.fechaInicio, p.fechaFin, " +
                    "TIME_FORMAT(p.horaEntrada, '%H:%i') AS horaEntrada, " +
                    "TIME_FORMAT(p.horaSalida, '%H:%i') AS horaSalida, " +
                    "ov.nombre AS nombreOrganizacion, " +
                    "rp.nombre AS nombreResponsable, rp.correo AS correoResponsable, rp.telefono AS telefonoResponsable " +
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
                if (matriculasUnicas.contains(matricula)) continue;

                DatosDocumentoAsignacion datos = new DatosDocumentoAsignacion();
                datos.setIdEstudiante(resultado.getInt("idEstudiante"));
                datos.setIdProyecto(resultado.getInt("idProyecto"));
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

        public static ArrayList<Estudiante> obtenerEstudiantesJuntoConSuProyectoYOrganizacionVinculada() throws SQLException{
            ArrayList<Estudiante> estudiantes = new ArrayList<>();
            Connection conexionBD = Conexion.abrirConexion();

            if (conexionBD != null) {
                String consulta = "SELECT e.*," +
                                "p.nombre AS nombreProyecto, " +
                                "ov.nombre AS nombreOV " +
                                "FROM estudiante e " +
                                "JOIN grupo g ON e.idGrupo = g.idGrupo " +
                                "JOIN periodo per ON g.idPeriodo = per.idPeriodo " +
                                "JOIN asignacion a ON e.idEstudiante = a.idEstudiante " +
                                "JOIN proyecto p ON a.idProyecto = p.idProyecto " +
                                "JOIN organizacionvinculada ov ON p.idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                                "WHERE CURRENT_DATE BETWEEN per.fechaInicio AND per.fechaFin;";
            
                PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                    estudiante.setNombre(resultado.getString("nombre"));
                    estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                    estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                    estudiante.setMatricula(resultado.getString("matricula"));
                    estudiante.setNombreProyecto(resultado.getString("nombreProyecto"));
                    estudiante.setNombreOV(resultado.getString("nombreOV"));
                    estudiantes.add(estudiante);
                }
                sentencia.close();
                resultado.close();
                conexionBD.close();
            } else {
                throw new SQLException("Sin conexion con la base de datos");
            }
            
            return estudiantes;
        }


    /**
     * Verifica si existe un estudiante con el ID especificado.
     * 
     * @param idEstudiante ID del estudiante a verificar.
     * @return true si el estudiante existe, false en caso contrario.
     * @throws SQLException en caso de error con la base de datos.
     */
    public static boolean existeEstudiante(int idEstudiante) throws SQLException {
        Connection conexion = Conexion.abrirConexion();
        String sql = "SELECT 1 FROM estudiante WHERE idEstudiante = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idEstudiante);
        ResultSet rs = ps.executeQuery();
        boolean existe = rs.next();
        rs.close();
        ps.close();
        conexion.close();
        return existe;
    }

    /**
     * Busca estudiantes que hayan entregado documentos de cierto tipo durante el periodo actual,
     * filtrando por matrícula si se proporciona.
     * 
     * @param textoBusqueda Texto para filtrar matrícula (puede estar vacío para no filtrar).
     * @param tipoDocumento Tipo de documento a buscar.
     * @return Lista de estudiantes que cumplan con los criterios.
     * @throws SQLException en caso de error con la base de datos o sin conexión.
     */
    public static ArrayList<Estudiante> buscarEstudiantesConDocumentosEntregadosPorTipoYMatricula(String textoBusqueda, String tipoDocumento) throws SQLException {
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        Connection conexion = Conexion.abrirConexion();

        if (conexion != null) {
            String consulta =
                "SELECT DISTINCT e.idEstudiante, e.nombre, e.apellidoPaterno, e.apellidoMaterno, " +
                "e.correo, e.matricula, g.idGrupo, CONCAT(g.bloque, '-', g.seccion) AS grupo " +
                "FROM estudiante e " +
                "JOIN grupo g ON e.idGrupo = g.idGrupo " +
                "JOIN periodo p ON g.idPeriodo = p.idPeriodo " +
                "JOIN expediente ex ON e.idEstudiante = ex.idEstudiante " +
                "JOIN documento d ON ex.idExpediente = d.idExpediente " +
                "WHERE CURRENT_DATE BETWEEN p.fechaInicio AND p.fechaFin " +
                "AND d.tipo = ? " +
                "AND (? = '' OR e.matricula LIKE ?)";

            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, tipoDocumento);
            sentencia.setString(2, textoBusqueda);
            sentencia.setString(3, "%" + textoBusqueda + "%");

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

    /**
     * Obtiene el nombre completo de un estudiante a partir de su ID.
     * 
     * @param idEstudiante ID del estudiante.
     * @return Nombre completo (nombre y apellidos) o cadena vacía si no se encuentra.
     */
    public static String obtenerNombreEstudiantePorId(int idEstudiante) {
        String nombreCompleto = "";
        String consulta = "SELECT nombre, apellidoPaterno, apellidoMaterno FROM estudiante WHERE idEstudiante = ?";
        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(consulta)) {
            ps.setInt(1, idEstudiante);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellidoPaterno") + " " + rs.getString("apellidoMaterno");
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return nombreCompleto;
    }
    
    /**
    * Obtiene una lista de estudiantes que aún no han sido evaluados en su presentación
    * final, dentro del periodo actual.
    * 
    * Este método filtra a los estudiantes que:
    * <ul>
    *   <li>Están asignados a un grupo dentro del periodo activo (fecha actual entre inicio y fin del periodo).</li>
    *   <li>Ya han realizado su presentación (la fecha de presentación es menor o igual a la actual).</li>
    *   <li>No han sido evaluados (el campo {@code evaluacionpresentacion} es distinto de 'evaluado').</li>
    * </ul>
    * 
    * Además, incluye el nombre del proyecto asignado y el nombre de la organización vinculada.
    * 
    * @return Lista de estudiantes pendientes por evaluación en el periodo activo.
    * @throws SQLException en caso de error con la base de datos o si no hay conexión.
    */
       public static ArrayList<Estudiante> obtenerEstudiantesPorEvaluar() throws SQLException {
            ArrayList<Estudiante> estudiantes = new ArrayList<>();
            Connection conexionBD = Conexion.abrirConexion();

            if (conexionBD != null) {
                String consulta = "SELECT e.*," +
                                "p.nombre AS nombreProyecto, " +
                                "ov.nombre AS nombreOV " +
                                "FROM estudiante e " +
                                "JOIN grupo g ON e.idGrupo = g.idGrupo " +
                                "JOIN periodo per ON g.idPeriodo = per.idPeriodo " +
                                "JOIN asignacion a ON e.idEstudiante = a.idEstudiante " +
                                "JOIN proyecto p ON a.idProyecto = p.idProyecto " +
                                "JOIN organizacionvinculada ov ON p.idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                                "JOIN presentacion pre ON e.idEstudiante = pre.idEstudiante " +
                                "JOIN expediente ex ON e.idEstudiante = ex.idEstudiante " +
                                "WHERE CURRENT_DATE BETWEEN per.fechaInicio AND per.fechaFin " +
                                "AND pre.fechaPresentacion <= CURRENT_DATE " +
                                "AND evaluacionpresentacion != 'evaluado';";
            
                PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
                    estudiante.setNombre(resultado.getString("nombre"));
                    estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                    estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                    estudiante.setMatricula(resultado.getString("matricula"));
                    estudiante.setNombreProyecto(resultado.getString("nombreProyecto"));
                    estudiante.setNombreOV(resultado.getString("nombreOV"));
                    estudiantes.add(estudiante);
                }
                sentencia.close();
                resultado.close();
                conexionBD.close();
            } else {
                throw new SQLException("Sin conexion con la base de datos");
            }
            
            return estudiantes;
        }
       
       /**
        * Verifica si un estudiante tiene un proyecto asignado dentro del periodo actual.
        * 
        * Este método consulta si el estudiante, identificado por su ID, está asignado a un proyecto
        * que corresponde a un grupo cuyo periodo está vigente en la fecha actual.
        * 
        * @param idEstudiante ID del estudiante a verificar.
        * @return {@code true} si el estudiante tiene un proyecto asignado en el periodo actual; {@code false} en caso contrario.
        */
       public static boolean tieneProyectoAsignadoPeriodoActual(int idEstudiante) {
            boolean tieneProyecto = false;

            String sql = "SELECT COUNT(*) " +
                         "FROM estudiante e " +
                         "JOIN grupo g ON e.idGrupo = g.idGrupo " +
                         "JOIN periodo p ON g.idPeriodo = p.idPeriodo " +
                         "JOIN asignacion a ON e.idEstudiante = a.idEstudiante " +
                         "WHERE e.idEstudiante = ? AND CURRENT_DATE BETWEEN p.fechaInicio AND p.fechaFin";

            try (Connection conn = Conexion.abrirConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idEstudiante);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        tieneProyecto = rs.getInt(1) > 0;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return tieneProyecto;
        }
       public static ExpedienteCompleto obtenerDatosExpedienteCompletoPorIdEstudiante(int idEstudiante) throws SQLException {
            ExpedienteCompleto datos = null;
            Connection conexion = Conexion.abrirConexion();

            if (conexion != null) {
                String consulta = "SELECT " +
                "CONCAT(e.nombre, ' ', e.apellidoPaterno, ' ', IFNULL(e.apellidoMaterno, '')) AS nombreEstudiante, " +
                "p.nombre AS nombreProyecto, " +
                "ov.nombre AS nombreOV, " +
                "ex.fechaInicio, ex.fechaFin, ex.horasAcumuladas, ex.estado, " +
                "ex.evaluacionpresentacion, " +
                "IFNULL(ep.puntajeTotalObtenido, 0) AS puntajeObtenido, " +
                "ex.evaluacionov " +
                "FROM estudiante e " +
                "INNER JOIN expediente ex ON e.idEstudiante = ex.idEstudiante " +
                "INNER JOIN asignacion a ON e.idEstudiante = a.idEstudiante " +
                "INNER JOIN proyecto p ON a.idProyecto = p.idProyecto " +
                "INNER JOIN organizacionvinculada ov ON p.idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                "LEFT JOIN evaluacionpresentacion ep ON ex.idExpediente = ep.idExpediente " +
                "WHERE e.idEstudiante = ?";

                PreparedStatement ps = conexion.prepareStatement(consulta);
                ps.setInt(1, idEstudiante);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    datos = new ExpedienteCompleto();
                    datos.setNombreEstudiante(rs.getString("nombreEstudiante"));
                    datos.setNombreProyecto(rs.getString("nombreProyecto"));
                    datos.setNombreOV(rs.getString("nombreOV"));
                    datos.setFechaInicio(LocalDate.parse(rs.getString("fechaInicio")));
                    datos.setFechaFin(LocalDate.parse(rs.getString("fechaFin")));
                    datos.setHorasAcumuladas(rs.getInt("horasAcumuladas"));
                    datos.setEstado(rs.getString("estado"));
                    datos.setEvaluacionPresentacion(rs.getString("evaluacionpresentacion"));
                    datos.setPuntajeObtenido(rs.getDouble("puntajeObtenido"));
                    datos.setEvaluacionOV(rs.getString("evaluacionov"));
                }

                rs.close();
                ps.close();
                conexion.close();
            }

            return datos;
        }
       
       public static ArrayList<Estudiante> obtenerEstudiantesJuntoConSuProyectoYOrganizacionVinculadaYExpediente() throws SQLException {
    ArrayList<Estudiante> estudiantes = new ArrayList<>();
    Connection conexionBD = Conexion.abrirConexion();

    if (conexionBD != null) {
        String consulta = "SELECT e.*, " +
                          "p.nombre AS nombreProyecto, " +
                          "ov.nombre AS nombreOV " +
                          "FROM estudiante e " +
                          "JOIN grupo g ON e.idGrupo = g.idGrupo " +
                          "JOIN periodo per ON g.idPeriodo = per.idPeriodo " +
                          "JOIN asignacion a ON e.idEstudiante = a.idEstudiante " +
                          "JOIN proyecto p ON a.idProyecto = p.idProyecto " +
                          "JOIN organizacionvinculada ov ON p.idOrganizacionVinculada = ov.idOrganizacionVinculada " +
                          "JOIN expediente ex ON e.idEstudiante = ex.idEstudiante " +  // <-- Se une con expediente para asegurarse que exista
                          "WHERE CURRENT_DATE BETWEEN per.fechaInicio AND per.fechaFin;";

        PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
        ResultSet resultado = sentencia.executeQuery();

        while (resultado.next()) {
            Estudiante estudiante = new Estudiante();
            estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
            estudiante.setNombre(resultado.getString("nombre"));
            estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
            estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
            estudiante.setMatricula(resultado.getString("matricula"));
            estudiante.setNombreProyecto(resultado.getString("nombreProyecto"));
            estudiante.setNombreOV(resultado.getString("nombreOV"));
            estudiantes.add(estudiante);
        }

        sentencia.close();
        resultado.close();
        conexionBD.close();
    } else {
        throw new SQLException("Sin conexion con la base de datos");
    }

    return estudiantes;
}



}

