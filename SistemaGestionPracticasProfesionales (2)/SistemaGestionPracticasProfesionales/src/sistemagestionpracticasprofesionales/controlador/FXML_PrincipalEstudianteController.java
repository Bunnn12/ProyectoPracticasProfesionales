package sistemagestionpracticasprofesionales.controlador;

import java.io.IOException;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author rodri
 */
public class FXML_PrincipalEstudianteController implements Initializable {

    private Estudiante estudianteSesion;
    @FXML
    private Label lblSaludoEstudiante;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void inicializarInformacion(Estudiante estudianteSesion){
        this.estudianteSesion = estudianteSesion;
        cargarInformacionUsuario();
    }
    
    private void cargarInformacionUsuario(){
        if (estudianteSesion != null){
            lblSaludoEstudiante.setText("Hola, " + estudianteSesion.getNombre());
        }
    }
    
    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Confirmar Cancelar", "¿Estás seguro de que quieres cancelar?");
        
        if (confirmado) {
        Utilidad.cerrarVentanaActual(lblSaludoEstudiante);
        try {
            System.out.println("Cargando FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/sistemagestionpracticasprofesionales/vista/FXML_InicioSesionUsuario.fxml"));
            Parent vista = loader.load();

            Stage nuevaVentana = new Stage();
            Scene escenaInicioSesion = new Scene(vista);
            nuevaVentana.setScene(escenaInicioSesion);
            nuevaVentana.setTitle("Inicio de Sesión");
            nuevaVentana.centerOnScreen();
            nuevaVentana.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana de inicio de sesión: " + e.getMessage());
            }
        }
    }

    @FXML
    private void clickSubirDocIniciales(ActionEvent event) {
    }

    @FXML
    private void clickConsultarAvance(ActionEvent event) {
    }

    @FXML
    private void clickEvaluarOV(ActionEvent event) {
    }

    
}
