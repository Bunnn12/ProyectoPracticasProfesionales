/**
 * Nombre del archivo: Conexion.java  
 * Autor: Rodrigo Santa Bárbara Murrieta  
 * Fecha: 06/06/2025  
 * Descripción: Clase que permite establecer una conexión con la base de datos
 * MySQL utilizada en el sistema de gestión de prácticas profesionales.
 */
package sistemagestionpracticasprofesionales.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Clase encargada de gestionar la conexión a la base de datos MySQL.
 */
public class Conexion {
    private static final String IP = "localhost";
    private static final String PUERTO = "3306";
    private static final String NOMBRE_BD = "practicasprofesionalesbd";
    private static final String USUARIO = "root";
    private static final String PASS = "Hola123";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    /**
     * Abre una conexión con la base de datos MySQL utilizando los parámetros configurados.
     *
     * @return Objeto Connection si la conexión fue exitosa, null en caso contrario.
     */
    public static Connection abrirConexion() {
        Connection conexionBD = null;
        String urlConexion = String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                IP, PUERTO, NOMBRE_BD);
        try {
            Class.forName(DRIVER);
            conexionBD = DriverManager.getConnection(urlConexion, USUARIO, PASS);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            System.err.println("Eror clase no encontrada");
        } catch (SQLException s){
            s.printStackTrace();
            System.err.println("Error en la conexión: "+s.getMessage());
        }
        return conexionBD;
    }
}
