package sistemagestionpracticasprofesionales.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author rodri
 */
public class Conexion {
    private static final String IP = "localhost";
    private static final String PUERTO = "3306";
    private static final String NOMBRE_BD = "practicasprofesionales";
    private static final String USUARIO = "root";
    private static final String PASS = "arbol123";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
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
