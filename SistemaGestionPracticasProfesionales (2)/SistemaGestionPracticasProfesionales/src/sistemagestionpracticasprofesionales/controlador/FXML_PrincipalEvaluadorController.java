/**
 * Nombre del archivo: FXML_PrincipalEvaluadorController.java
 * Autor: Rodrigo Santa Bárbara Murrieta
 * Fecha: 08/06/2025
 * Descripción: Controlador para la ventana principal del evaluador.
 * Permite mostrar un saludo personalizado con el nombre del usuario en sesión,
 * gestionar el cierre de sesión y preparar la interfaz para evaluar estudiantes.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.pojo.Usuario;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la vista principal del evaluador.
 * 
 * Esta clase gestiona la sesión activa del evaluador y controla la navegación
 * entre las diferentes vistas disponibles en la interfaz principal del evaluador.
 * 
 */

public class FXML_PrincipalEvaluadorController implements Initializable {
    Usuario usuarioSesion;
    @FXML
    private Label lbSaludoUsuario;
    
    /**
     * Inicializa el controlador de la vista.
     * Este método se ejecuta automáticamente al cargar la interfaz.
     * 
     * @param url URL utilizado para localizar los recursos.
     * @param rb Recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
        
    /**
     * Método para inicializar la información del usuario que inició sesión.
     * Se debe llamar desde la clase que carga esta ventana para pasar el usuario activo.
     * 
     * @param usuarioSesion Usuario que ha iniciado sesión.
     */
    public void inicializarInformacion(Usuario usuarioSesion){
        this.usuarioSesion = usuarioSesion;
        cargarInformacionUsuario();
    }
    
    /**
     * Carga y muestra la información del usuario en la interfaz.
     * En este caso, establece un saludo personalizado en la etiqueta lbSaludoUsuario.
     */
    private void cargarInformacionUsuario(){
        if (usuarioSesion != null){
            lbSaludoUsuario.setText("Hola, " + usuarioSesion.getNombre());
        }
    }

    /**
     * Evento que se ejecuta al hacer clic en el botón de cerrar sesión.
     * Solicita confirmación para cerrar sesión y, si se confirma, cierra la ventana actual
     * y abre la ventana de inicio de sesión.
     * 
     * @param event Evento generado por la acción del botón.
     */
    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Confirmar Cerrar sesión", "¿Estás seguro de que quieres cerrar sesión?");
        
        if (confirmado) {
        Utilidad.cerrarVentanaActual(lbSaludoUsuario);
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
    private void clickEvaluarEstudiante(ActionEvent event) {
    }
}
