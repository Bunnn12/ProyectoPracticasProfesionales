    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package sistemagestionpracticasprofesionales.modelo.dao;

    import java.io.File;
    import java.io.FileInputStream;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import sistemagestionpracticasprofesionales.modelo.Conexion;

    /**
     *
     * @author reino
     */
    public class OficioAsignacionDAO {

        public static boolean guardarOficio(int idEstudiante, int idProyecto, byte[] documentoBytes) throws SQLException {
            try (Connection conexion = Conexion.abrirConexion();
                 PreparedStatement ps = conexion.prepareStatement("INSERT INTO oficioAsignacion (idEstudiante, idProyecto, documento) VALUES (?, ?, ?)")) {

                if (conexion == null) {
                    throw new SQLException("Sin conexión con la base de datos");
                }

                ps.setInt(1, idEstudiante);
                ps.setInt(2, idProyecto);
                ps.setBytes(3, documentoBytes);

                int filas = ps.executeUpdate();
                return filas > 0;
            }
        }
    }
