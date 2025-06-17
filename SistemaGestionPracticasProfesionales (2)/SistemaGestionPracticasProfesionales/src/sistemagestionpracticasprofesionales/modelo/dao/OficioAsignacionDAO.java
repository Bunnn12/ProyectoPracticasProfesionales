/**
 * Nombre del archivo: OficioAsignacionDAO.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 14/06/25
 * Descripción: Clase DAO encargada de guardar el oficio de asignación de un estudiante a un proyecto
 * en la base de datos, almacenando el documento asociado como un arreglo de bytes.
 */
    package sistemagestionpracticasprofesionales.modelo.dao;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;
    import sistemagestionpracticasprofesionales.modelo.Conexion;

    /**
    * Clase DAO para la gestión del oficio de asignación.
    */
    public class OficioAsignacionDAO {

    /**
     * Guarda el oficio de asignación en la base de datos.
     *
     * @param idEstudiante   Identificador del estudiante.
     * @param idProyecto     Identificador del proyecto.
     * @param documentoBytes Documento del oficio en formato byte array.
     * @return true si el oficio se guardó correctamente, false en caso contrario.
     * @throws SQLException Si ocurre un error con la base de datos o la conexión.
     */
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
