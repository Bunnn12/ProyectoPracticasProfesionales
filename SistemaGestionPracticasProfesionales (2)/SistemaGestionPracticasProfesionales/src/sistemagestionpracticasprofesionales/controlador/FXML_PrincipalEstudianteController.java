/**
 * Nombre del archivo: FXML_PrincipalEstudianteController.java
 * Autor: Rodrigo Santa Bárbara Murrieta
 * Fecha: 08/06/2025
 * Descripción: Controlador para la vista principal del estudiante.
 * Permite gestionar la sesión del usuario estudiante y controlar la navegación
 * a las funcionalidades disponibles, como subir documentos iniciales,
 * consultar avances y evaluar oficios de vinculación.
 */
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
 * Controlador para la vista principal del estudiante.
 * Permite gestionar la sesión del estudiante en curso y controlar
 * la navegación a otras vistas desde la interfaz principal.
 * 
 */
public class FXML_PrincipalEstudianteController implements Initializable {

    private Estudiante estudianteSesion;
    @FXML
    private Label lblSaludoEstudiante;
    
    /**
     * Inicializa el controlador.
     * 
     * @param url URL de localización del archivo FXML.
     * @param rb  ResourceBundle con recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    /**
     * Inicializa la información del estudiante en sesión.
     * 
     * @param estudianteSesion Estudiante que ha iniciado sesión.
     */
    public void inicializarInformacion(Estudiante estudianteSesion){
        this.estudianteSesion = estudianteSesion;
        cargarInformacionUsuario();
    }
    
    /**
     * Carga la información del estudiante en la vista.
     */
    private void cargarInformacionUsuario(){
        if (estudianteSesion != null){
            lblSaludoEstudiante.setText("Hola, " + estudianteSesion.getNombre());
        }
    }
    
    /**
     * Maneja el cierre de sesión, mostrando alerta de confirmación y
     * regresando a la ventana de inicio de sesión si se confirma.
     * 
     * @param event Evento generado por el botón cerrar sesión.
     */
    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Confirmar Cerrar sesión", "¿Estás seguro de que quieres cerrar sesión?");
        
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
