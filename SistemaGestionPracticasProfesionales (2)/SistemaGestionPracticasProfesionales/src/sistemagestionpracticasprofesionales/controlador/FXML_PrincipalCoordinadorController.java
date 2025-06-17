/**
 * Nombre del archivo: FXML_PrincipalCoordinadorController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 08/06/2025
 * Descripción: Controlador para la vista principal del coordinador. Permite gestionar la sesión del usuario coordinador y abrir ventanas modales para asignar proyectos, registrar proyectos, organizaciones vinculadas, responsables y actualizar responsables.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.IOException;
import sistemagestionpracticasprofesionales.modelo.pojo.Usuario;
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
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author rodri
 */
public class FXML_PrincipalCoordinadorController implements Initializable {

    private Usuario usuarioSesion;
    @FXML
    private Label lbSaludoUsuario;
    
    /**
     * Inicializa el controlador.
     * @param url URL de localización del archivo FXML.
     * @param rb ResourceBundle con recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    /**
     * Inicializa la información del usuario coordinador en sesión.
     * @param usuarioSesion Usuario coordinador que inició sesión.
     */
    public void inicializarInformacion(Usuario usuarioSesion){
        this.usuarioSesion = usuarioSesion;
        cargarInformacionUsuario();
    }
    
    /**
     * Carga la información del usuario coordinador en la vista.
     */
    private void cargarInformacionUsuario(){
        if (usuarioSesion != null){
            lbSaludoUsuario.setText("Hola, " + usuarioSesion.getNombre());
        }
    }
    
    /**
     * Maneja el cierre de sesión, mostrando alerta de confirmación y regresando a la ventana de inicio de sesión.
     * @param event Evento de acción del botón cerrar sesión.
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

    /**
     * Abre la ventana modal para asignar un proyecto.
     * @param event Evento del botón asignar proyecto.
     */
    @FXML
    private void clickAsignarProyecto(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_AsignarProyecto.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Asignar proyecto");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana modal para generar los oficios de asignación.
     * @param event Evento del botón generar documento de asignación.
     */
    @FXML
    private void clickGenerarDocAsignacion(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_GeneracionOficioAsignacion.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Generar oficio de asignación");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Abre la ventana modal para registrar un nuevo proyecto.
     * @param event Evento del botón registrar proyecto.
     */
    @FXML
    private void clickRegistrarProyecto(ActionEvent event) {
        try {
            Stage escenario = new Stage();
            Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_RegistrarProyecto.fxml"));
            Scene escena = new Scene(vista);
            escenario.setScene(escena);
            escenario.setTitle("Registrar proyecto");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana modal para registrar una organización vinculada.
     * @param event Evento del botón registrar organización vinculada.
     */
    @FXML
    private void clickRegistrarOV(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_RegistrarOV.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Registrar organización vinculada");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana modal para registrar un responsable.
     * @param event Evento del botón registrar responsable.
     */
    @FXML
    private void clickRegistrarResponsable(ActionEvent event) {
        try{
        Stage escenario = new Stage();
        Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_RegistrarResponsable.fxml"));
        Scene escena= new Scene(vista);
        escenario.setScene(escena);
        escenario.setTitle("Registrar responsable");
        escenario.initModality(Modality.APPLICATION_MODAL);
        escenario.showAndWait();
        }catch(IOException e){
            e.printStackTrace();
        }
     
    }

    /**
     * Abre la ventana modal para actualizar responsable de un proyecto
     * @param event Evento del botón actualizar responsable.
     */
    @FXML
    private void clickActualizarResponsable(ActionEvent event) {
        try{
        Stage escenario = new Stage();
        Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_BusquedaProyecto.fxml"));
        Scene escena= new Scene(vista);
        escenario.setScene(escena);
        escenario.setTitle("Busqueda proyecto");
        escenario.initModality(Modality.APPLICATION_MODAL);
        escenario.showAndWait();
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
}
