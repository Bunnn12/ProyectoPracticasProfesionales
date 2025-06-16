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
import sistemagestionpracticasprofesionales.modelo.pojo.Reporte;
import sistemagestionpracticasprofesionales.modelo.pojo.DocumentoAnexo;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Expediente;


/**
 *
 * @author reino
 */
public class ExpedienteDAO {
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

    public static boolean actualizarEstadoDocumento(int idDocumento, String estado) throws SQLException {
        String consulta = "UPDATE documento SET estado = ? WHERE idDocumentoAnexo = ?";
        try (Connection conexion = Conexion.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, estado);
            ps.setInt(2, idDocumento);

            return ps.executeUpdate() > 0;
        }
    }

    public static boolean actualizarEstadoReporte(int idReporte, String estado) throws SQLException {
        String consulta = "UPDATE reporte SET estado = ? WHERE idReporte = ?";
        try (Connection conexion = Conexion.abrirConexion();
             PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, estado);
            ps.setInt(2, idReporte);

            return ps.executeUpdate() > 0;
        }
    }

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
    
    public static Expediente obtenerExpedienteConEstudiante(int idExpediente) throws SQLException {
    Expediente expediente = null;
    String consulta = "SELECT e.*, es.idEstudiante, es.nombre, es.apellidoPaterno, es.apellidoMaterno, es.matricula " +
                      "FROM expediente e " +
                      "JOIN estudiante es ON e.idEstudiante = es.idEstudiante " +
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
                expediente.setIdEstudiante(rs.getInt("idEstudiante"));

                Estudiante estudiante = new Estudiante();
                estudiante.setIdEstudiante(rs.getInt("idEstudiante"));
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setApellidoPaterno(rs.getString("apellidoPaterno"));
                estudiante.setApellidoMaterno(rs.getString("apellidoMaterno"));
                estudiante.setMatricula(rs.getString("matricula"));

                expediente.setEstudiante(estudiante);
            }
        }
    }

    return expediente;
}

public static boolean actualizarHorasExpediente(int idExpediente, int nuevasHoras) throws SQLException {
    String sql = "UPDATE expediente SET horasAcumuladas = ? WHERE idExpediente = ?";
    try (Connection conexion = Conexion.abrirConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setInt(1, nuevasHoras);
        ps.setInt(2, idExpediente);
        return ps.executeUpdate() > 0;
    }
}
    
    



}
