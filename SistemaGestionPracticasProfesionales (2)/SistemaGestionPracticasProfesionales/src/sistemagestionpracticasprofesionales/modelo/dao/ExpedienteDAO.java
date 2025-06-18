/**
 * Nombre del archivo: ExpedienteDAO.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 12/06/25
 * Descripción: Clase DAO encargada de gestionar el acceso a los datos relacionados con los expedientes, reportes y documentos anexos en la base de datos.
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sistemagestionpracticasprofesionales.modelo.Conexion;
import sistemagestionpracticasprofesionales.modelo.pojo.Reporte;
import sistemagestionpracticasprofesionales.modelo.pojo.DocumentoAnexo;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Expediente;
import sistemagestionpracticasprofesionales.modelo.pojo.ExpedienteCompleto;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;


/**
 * Clase DAO que permite realizar operaciones de lectura y escritura sobre los expedientes,
 * documentos anexos y reportes en la base de datos del sistema.
 */
public class ExpedienteDAO {
    /**
     * Obtiene la lista de reportes entregados por un estudiante específico.
     * 
     * @param idEstudiante ID del estudiante.
     * @return Lista de objetos Reporte asociados al estudiante.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public static ArrayList<Reporte> obtenerReportesPorEstudiante(int idEstudiante) throws SQLException {
        ArrayList<Reporte> reportes = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT r.idReporte, r.fechaEntrega, r.numeroHorasTrabajadas, r.idExpediente, r.nombre, r.archivo, r.estado " +
                              "FROM reporte r " +
                              "JOIN expediente e ON r.idExpediente = e.idExpediente " +
                              "WHERE e.idEstudiante = ?";

            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idEstudiante);

            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Reporte reporte = convertirRegistroReporte(resultado);
                reportes.add(reporte);
            }

            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }

        return reportes;
    }
    
    /**
     * Convierte un registro de ResultSet en un objeto Reporte.
     *
     * @param rs ResultSet con los datos del reporte.
     * @return Objeto Reporte con los datos extraídos del ResultSet.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
    private static Reporte convertirRegistroReporte(ResultSet rs) throws SQLException {
        Reporte reporte = new Reporte();
        reporte.setIdReporte(rs.getInt("idReporte"));
        reporte.setFechaEntrega(rs.getDate("fechaEntrega"));
        reporte.setNumeroHorasTrabajadas(rs.getInt("numeroHorasTrabajadas"));
        reporte.setIdExpediente(rs.getInt("idExpediente"));
        reporte.setNombre(rs.getString("nombre"));
        reporte.setArchivo(rs.getBytes("archivo"));
        reporte.setEstado(rs.getString("estado"));
        return reporte;
    }
    
    /**
     * Obtiene documentos anexos de un expediente por tipo.
     * 
     * @param idExpediente ID del expediente.
     * @param tipo Tipo de documento a filtrar.
     * @return Lista de documentos anexos que coinciden con el tipo dado.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public static ArrayList<DocumentoAnexo> obtenerDocumentosPorTipo(int idExpediente, String tipo) throws SQLException {
        ArrayList<DocumentoAnexo> documentos = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT idDocumentoAnexo, nombre, fechaElaboracion, tipo, idExpediente, archivo, estado " +
                              "FROM documento " +
                              "WHERE idExpediente = ? AND tipo = ?";

            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idExpediente);
            sentencia.setString(2, tipo);

            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                DocumentoAnexo doc = convertirRegistroDocumento(resultado);
                documentos.add(doc);
            }

            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }

        return documentos;
    }

    
    /**
     * Convierte un registro de ResultSet en un objeto DocumentoAnexo.
     * 
     * @param rs ResultSet con los datos del documento.
     * @return Objeto DocumentoAnexo con los datos extraídos del ResultSet.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
    private static DocumentoAnexo convertirRegistroDocumento(ResultSet rs) throws SQLException {
        DocumentoAnexo doc = new DocumentoAnexo();
        doc.setIdDocumentoAnexo(rs.getInt("idDocumentoAnexo"));
        doc.setNombre(rs.getString("nombre"));
        doc.setFechaElaboracion(rs.getDate("fechaElaboracion"));
        doc.setTipo(rs.getString("tipo"));
        doc.setIdExpediente(rs.getInt("idExpediente"));
        doc.setArchivo(rs.getBytes("archivo"));
        doc.setEstado(rs.getString("estado"));
        return doc;
    }
    
    /**
     * Actualiza la retroalimentación de un documento.
     * 
     * @param idDocumento ID del documento a actualizar.
     * @param retroalimentacion Texto con la retroalimentación a establecer; puede ser null.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al actualizar en la base de datos.
     */
    public static boolean actualizarRetroalimentacionDocumento(int idDocumento, String retroalimentacion) throws SQLException {
        String consulta = "UPDATE documento SET retroalimentacion = ? WHERE idDocumentoAnexo = ?";
        try (Connection conexion = Conexion.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(consulta)) {
            if (retroalimentacion != null) {
                ps.setString(1, retroalimentacion);
            } else {
                ps.setNull(1, java.sql.Types.VARCHAR);
            }
            ps.setInt(2, idDocumento);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Actualiza la retroalimentación de un reporte.
     * 
     * @param idReporte ID del reporte a actualizar.
     * @param retroalimentacion Texto con la retroalimentación a establecer; puede ser null.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al actualizar en la base de datos.
     */
    public static boolean actualizarRetroalimentacionReporte(int idReporte, String retroalimentacion) throws SQLException {
        String consulta = "UPDATE reporte SET retroalimentacion = ? WHERE idReporte = ?";
        try (Connection conexion = Conexion.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(consulta)) {
            if (retroalimentacion != null) {
                ps.setString(1, retroalimentacion);
            } else {
                ps.setNull(1, java.sql.Types.VARCHAR);
            }
            ps.setInt(2, idReporte);

            return ps.executeUpdate() > 0;
        }
    }
    
    /**
     * Actualiza el estado de un documento.
     * 
     * @param idDocumento ID del documento a actualizar.
     * @param estado Nuevo estado a establecer.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al actualizar en la base de datos.
     */
    public static boolean actualizarEstadoDocumento(int idDocumento, String estado) throws SQLException {
        String consulta = "UPDATE documento SET estado = ? WHERE idDocumentoAnexo = ?";
        try (Connection conexion = Conexion.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, estado);
            ps.setInt(2, idDocumento);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Actualiza el estado de un reporte.
     * 
     * @param idReporte ID del reporte a actualizar.
     * @param estado Nuevo estado a establecer.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al actualizar en la base de datos.
     */
    public static boolean actualizarEstadoReporte(int idReporte, String estado) throws SQLException {
        String consulta = "UPDATE reporte SET estado = ? WHERE idReporte = ?";
        try (Connection conexion = Conexion.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, estado);
            ps.setInt(2, idReporte);

            return ps.executeUpdate() > 0;
        }
    }
    /**
     * Obtiene un expediente a partir del ID del estudiante.
     * 
     * @param idEstudiante ID del estudiante.
     * @return Objeto Expediente si se encuentra uno asociado al estudiante, null en caso contrario.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public static Expediente obtenerExpedientePorIdEstudiante(int idEstudiante) throws SQLException {
        Expediente expediente = null;
        String consulta = "SELECT * FROM expediente WHERE idEstudiante = ?";
        try (Connection conexion = Conexion.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    expediente = new Expediente();
                    expediente.setIdExpediente(rs.getInt("idExpediente"));
                    expediente.setFechaInicio(rs.getDate("fechaInicio"));
                    expediente.setFechaFin(rs.getDate("fechaFin"));
                    expediente.setHorasAcumuladas(rs.getInt("horasAcumuladas"));
                    expediente.setEstado(rs.getString("estado"));
                    expediente.setIdEstudiante(rs.getInt("idEstudiante"));
                }
            }
        }
        return expediente;
    }
    
    /**
     * Cuenta la cantidad de documentos de un tipo específico asociados a un expediente.
     * 
     * @param idExpediente ID del expediente.
     * @param tipo Tipo de documento a contar.
     * @return Número total de documentos que coinciden con el tipo dado.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public static int contarDocumentosPorTipo(int idExpediente, String tipo) throws SQLException {
        int total = 0;
        String query = "SELECT COUNT(*) FROM documento WHERE idExpediente = ? AND tipo = ?";
        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idExpediente);
            ps.setString(2, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        }
        return total;
    }

    /**
     * Cuenta la cantidad total de reportes asociados a un expediente.
     * 
     * @param idExpediente ID del expediente.
     * @return Número total de reportes asociados al expediente.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public static int contarReportes(int idExpediente) throws SQLException {
        int total = 0;
        String query = "SELECT COUNT(*) FROM reporte WHERE idExpediente = ?";
        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idExpediente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        }
        return total;
    }
    
     /**
     * Busca el ID del expediente a partir del nombre o matrícula del estudiante.
     * 
     * @param texto Texto para buscar, puede ser parte del nombre o matrícula.
     * @return ID del expediente encontrado o null si no existe.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public static Integer buscarIdExpedientePorNombreOMatricula(String texto) throws SQLException {
        Integer idExpediente = null;
        String sql = "SELECT e.idExpediente " +
                     "FROM expediente e " +
                     "JOIN estudiante es ON e.idEstudiante = es.idEstudiante " +
                     "WHERE es.nombre LIKE CONCAT('%', ?, '%') " +
                     "OR es.matricula LIKE CONCAT('%', ?, '%')";

        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, texto);
            ps.setString(2, texto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idExpediente = rs.getInt("idExpediente");
                }
            }
        }
        return idExpediente;
    }

    /**
     * Busca el ID del expediente a partir de la matrícula del estudiante.
     * 
     * @param matricula Matrícula exacta del estudiante.
     * @return ID del expediente encontrado o null si no existe.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public static Integer buscarIdExpedientePorMatricula(String matricula) throws SQLException {
        Integer idExpediente = null;
        String sql = "SELECT e.idExpediente " +
                     "FROM expediente e " +
                     "JOIN estudiante es ON e.idEstudiante = es.idEstudiante " +
                     "WHERE es.matricula = ?";

        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idExpediente = rs.getInt("idExpediente");
                }
            }
        }
        return idExpediente;
    }

  /**
 * Obtiene un expediente con los datos del estudiante asociado y el nombre del proyecto asignado.
 *
 * @param idExpediente ID del expediente.
 * @return Objeto Expediente con información completa, incluido el estudiante y el nombre del proyecto, o null si no se encuentra.
 * @throws SQLException Si ocurre un error al acceder a la base de datos.
 */
    public static Expediente obtenerExpedienteConEstudianteYProyecto(int idExpediente) throws SQLException {
        Expediente expediente = null;
        String consulta = "SELECT " +
                          "e.idExpediente, e.fechaInicio, e.fechaFin, e.horasAcumuladas, e.estado, e.idEstudiante AS idEstudianteExp, " +
                          "es.idEstudiante AS idEstudianteEst, es.nombre, es.apellidoPaterno, es.apellidoMaterno, es.matricula, " +
                          "p.idProyecto, p.nombre AS nombreProyecto " +
                          "FROM expediente e " +
                          "JOIN estudiante es ON e.idEstudiante = es.idEstudiante " +
                          "JOIN asignacion a ON es.idEstudiante = a.idEstudiante " +
                          "JOIN proyecto p ON a.idProyecto = p.idProyecto " +
                          "WHERE e.idExpediente = ?";

        try (Connection conexion = Conexion.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(consulta)) {

            ps.setInt(1, idExpediente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    expediente = new Expediente();
                    expediente.setIdExpediente(rs.getInt("idExpediente"));
                    expediente.setFechaInicio(rs.getDate("fechaInicio"));
                    expediente.setFechaFin(rs.getDate("fechaFin"));
                    expediente.setHorasAcumuladas(rs.getInt("horasAcumuladas"));
                    expediente.setEstado(rs.getString("estado"));
                    expediente.setIdEstudiante(rs.getInt("idEstudianteExp"));

                    Estudiante estudiante = new Estudiante();
                    estudiante.setIdEstudiante(rs.getInt("idEstudianteEst"));
                    estudiante.setNombre(rs.getString("nombre"));
                    estudiante.setApellidoPaterno(rs.getString("apellidoPaterno"));
                    estudiante.setApellidoMaterno(rs.getString("apellidoMaterno"));
                    estudiante.setMatricula(rs.getString("matricula"));
                    expediente.setEstudiante(estudiante);

                    Proyecto proyecto = new Proyecto();
                    proyecto.setIdProyecto(rs.getInt("idProyecto"));
                    proyecto.setNombre(rs.getString("nombreProyecto"));
                    expediente.setProyecto(proyecto);
                }
            }
        }

        return expediente;
    }



    /**
     * Actualiza las horas acumuladas de un expediente.
     * 
     * @param idExpediente ID del expediente a actualizar.
     * @param nuevasHoras Nuevo número de horas acumuladas.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al actualizar en la base de datos.
     */
    public static boolean actualizarHorasExpediente(int idExpediente, int nuevasHoras) throws SQLException {
        String sql = "UPDATE expediente SET horasAcumuladas = ? WHERE idExpediente = ?";
        try (Connection conexion = Conexion.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, nuevasHoras);
            ps.setInt(2, idExpediente);
            return ps.executeUpdate() > 0;
        }
    }
    public static boolean actualizarEvaluacionesYEstado(int idExpediente, String evalPres, String evalOV, String estado) throws SQLException {
    String sql = "UPDATE expediente SET evaluacionPresentacion = ?, evaluacionOV = ?, estado = ? WHERE idExpediente = ?";
    try (Connection conn = Conexion.abrirConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, evalPres);
        ps.setString(2, evalOV);
        ps.setString(3, estado);
        ps.setInt(4, idExpediente);
        return ps.executeUpdate() > 0;
    }
}
/**
 * Obtiene un objeto ExpedienteCompleto a partir del id del expediente.
 * Realiza una consulta SQL para obtener los datos del expediente junto con
 * la información básica del estudiante asociado.
 * 
 * @param idExpediente Identificador del expediente a buscar.
 * @return Un objeto ExpedienteCompleto con la información cargada, o null si no existe.
 * @throws SQLException En caso de error con la base de datos.
 */
public static ExpedienteCompleto obtenerExpedienteCompleto(int idExpediente) throws SQLException {
    ExpedienteCompleto expediente = null;

    // Consulta para obtener el expediente y los datos básicos del estudiante relacionado
    String consulta = "SELECT e.*, es.nombre, es.apellidoPaterno, es.apellidoMaterno, es.matricula " +
                      "FROM expediente e " +
                      "JOIN estudiante es ON e.idEstudiante = es.idEstudiante " +
                      "WHERE e.idExpediente = ?";

    // Uso de try-with-resources para asegurar cierre automático de recursos
    try (Connection conexion = Conexion.abrirConexion();
         PreparedStatement ps = conexion.prepareStatement(consulta)) {
        
        // Establecer parámetro para la consulta preparada
        ps.setInt(1, idExpediente);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Crear nueva instancia de ExpedienteCompleto para almacenar resultados
                expediente = new ExpedienteCompleto();

                // Asignar campos básicos del expediente
                expediente.setIdExpediente(rs.getInt("idExpediente"));

                // Obtener fechas desde java.sql.Date y convertir a java.time.LocalDate
                java.sql.Date fechaInicioSQL = rs.getDate("fechaInicio");
                java.sql.Date fechaFinSQL = rs.getDate("fechaFin");

                if (fechaInicioSQL != null) {
                    expediente.setFechaInicio(fechaInicioSQL.toLocalDate());
                }
                if (fechaFinSQL != null) {
                    expediente.setFechaFin(fechaFinSQL.toLocalDate());
                }

                // Asignar horas acumuladas, estado y evaluaciones del expediente
                expediente.setHorasAcumuladas(rs.getInt("horasAcumuladas"));
                expediente.setEstado(rs.getString("estado"));
                expediente.setEvaluacionPresentacion(rs.getString("evaluacionPresentacion"));
                expediente.setEvaluacionOV(rs.getString("evaluacionOV"));

                // Crear y asignar objeto Estudiante con la información obtenida
                Estudiante estudiante = new Estudiante();
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setApellidoPaterno(rs.getString("apellidoPaterno"));
                estudiante.setApellidoMaterno(rs.getString("apellidoMaterno"));
                estudiante.setMatricula(rs.getString("matricula"));

                expediente.setEstudiante(estudiante);
            }
        }
    }

    // Retorna el expediente completo o null si no se encontró
    return expediente;
    }
}
