/*
 * Nombre del archivo: PeriodoDAO.java
 * Autor: Rodrigo Santa Bárbara Murrieta
 * Fecha: 16/06/2025
 * Descripción: Clase DAO que permite acceder a la información de los períodos académicos
 * almacenados en la base de datos del sistema.
 * Proporciona métodos para obtener el identificador del período actual,
 * definido como aquel cuyo rango de fechas incluye la fecha actual.
 * Esta clase es utilizada por otras clases DAO para validar y relacionar
 * operaciones con el período vigente.
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistemagestionpracticasprofesionales.modelo.Conexion;

/**
 * Clase DAO para manejar operaciones relacionadas con la tabla 'periodo' en la base de datos.
 */
public class PeriodoDAO {
    /**
     * Obtiene el identificador del período actual, definido como aquel
     * cuyo rango de fechas (fechaInicio a fechaFin) incluye la fecha actual.
     *
     * @return el id del período actual si existe, o null si no se encuentra ningún período vigente.
     */
    public static Integer obtenerIdPeriodoActual() {
        Integer idPeriodo = null;
        String consulta = "SELECT idPeriodo FROM periodo WHERE CURRENT_DATE BETWEEN fechaInicio AND fechaFin";

        try (Connection conexion = Conexion.abrirConexion();
             PreparedStatement pstmt = conexion.prepareStatement(consulta);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                idPeriodo = rs.getInt("idPeriodo");
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el periodo actual: " + e.getMessage());
        }

        return idPeriodo;
    }
}
