/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistemagestionpracticasprofesionales.modelo.Conexion;

/**
 *
 * @author reino
 */
public class PeriodoDAO {
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
