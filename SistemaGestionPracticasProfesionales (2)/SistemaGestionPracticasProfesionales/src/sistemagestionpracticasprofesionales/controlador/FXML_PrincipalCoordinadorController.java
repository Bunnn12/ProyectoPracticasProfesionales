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
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void inicializarInformacion(Usuario usuarioSesion){
        this.usuarioSesion = usuarioSesion;
        cargarInformacionUsuario();
    }
    
    private void cargarInformacionUsuario(){
        if (usuarioSesion != null){
            lbSaludoUsuario.setText("Hola, " + usuarioSesion.getNombre());
        }
    }
    
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


    @FXML
    private void clickRegistrarProyecto(ActionEvent event) {
    }

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
