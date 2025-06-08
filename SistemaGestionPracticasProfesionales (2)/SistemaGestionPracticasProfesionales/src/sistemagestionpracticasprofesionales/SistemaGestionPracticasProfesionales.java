package sistemagestionpracticasprofesionales;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rodri
 */
public class SistemaGestionPracticasProfesionales extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            System.out.println("Cargando FXML...");
            Parent vista = FXMLLoader.load(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_InicioSesionUsuario.fxml"));

            Scene escenaInicioSesion = new Scene(vista);
            primaryStage.setScene(escenaInicioSesion);
            primaryStage.setTitle("Inicio de Sesión");
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
