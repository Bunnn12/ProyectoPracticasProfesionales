/**
 * Nombre del archivo: FXML_PrincipalProfesorEEController.java
 * Autor: Juan Pablo Silva Miranda
 * Fecha: 08/06/2025
 * Descripción: Controlador para la vista principal del profesor EE.
 * Permite gestionar la sesión del profesor en curso, mostrar un saludo personalizado,
 * y controlar la navegación hacia las vistas de validación de documentos, consulta y actualización de expedientes.
 * También incluye la funcionalidad para cerrar sesión con confirmación.
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.SistemaGestionPracticasProfesionales;
import sistemagestionpracticasprofesionales.modelo.pojo.Usuario;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la vista principal del profesor EE.
 * Permite gestionar la sesión del profesor en curso, mostrar un saludo personalizado,
 * y controlar la navegación hacia las vistas de validación de documentos, consulta y actualización de expedientes.
 * También incluye la funcionalidad para cerrar sesión con confirmación.
 */
public class FXML_PrincipalProfesorEEController implements Initializable {
    Usuario usuarioSesion;
    @FXML
    private Label lbSaludoUsuario;
    
    /**
     * Método que se ejecuta al inicializar el controlador.
     * Aquí se puede colocar código para inicializar elementos de la vista.
     * 
     * @param url URL de localización (no utilizado)
     * @param rb  ResourceBundle para internacionalización (no utilizado)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    /**
     * Inicializa la información del usuario en sesión.
     * 
     * @param usuarioSesion Usuario actualmente logueado
     */
    public void inicializarInformacion(Usuario usuarioSesion){
        this.usuarioSesion = usuarioSesion;
        cargarInformacionUsuario();
    }
    
    /**
     * Carga y muestra la información del usuario en la vista,
     * específicamente el saludo con el nombre del usuario.
     */
    private void cargarInformacionUsuario(){
        if (usuarioSesion != null){
            lbSaludoUsuario.setText("Hola, " + usuarioSesion.getNombre());
        }
    }    

    /**
     * Evento que se activa al hacer clic en el botón "Validar Documentos".
     * Abre una nueva ventana modal para validar documentos de estudiantes.
     * 
     * @param event Evento de acción del botón
     */
    @FXML
    private void clickValidarDocumentos(ActionEvent event) {
        try{
        Stage escenario = new Stage();
        Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_BusquedaEstudianteValidarDocumento.fxml"));
        Scene escena= new Scene(vista);
        escenario.setScene(escena);
        escenario.setTitle("Validar documentos");
        escenario.initModality(Modality.APPLICATION_MODAL);
        escenario.showAndWait();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Evento que se activa al hacer click en el botón "Consultar expediente"
     * Abre una nueva ventana modal para ver la lista de estudiantes con expediente.
     * 
     * @param event Evento de acción del botón
     */
    @FXML
    private void clickConsultarExpediente(ActionEvent event) {
        try {
            Stage escenario = new Stage();
            Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_ConsultarExpediente.fxml"));
            Scene escena = new Scene(vista);
            escenario.setScene(escena);
            escenario.setTitle("Consular expediente");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Evento que se activa al hacer clic en el botón "Actualizar Expediente".
     * Abre una ventana modal para buscar y actualizar expedientes de estudiantes.
     * 
     * @param event Evento de acción del botón
     */
    @FXML
    private void clickActualizarExpediente(ActionEvent event) {
        try {
            Stage escenario = new Stage();
            Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_BusquedaExpediente.fxml"));
            Scene escena = new Scene(vista);
            escenario.setScene(escena);
            escenario.setTitle("Actualizar expediente");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Evento que se activa al hacer clic en el botón "Cerrar Sesión".
     * Solicita confirmación al usuario para cerrar sesión y, si se confirma,
     * cierra la ventana actual y abre la ventana de inicio de sesión.
     * 
     * @param event Evento de acción del botón
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
    
}
