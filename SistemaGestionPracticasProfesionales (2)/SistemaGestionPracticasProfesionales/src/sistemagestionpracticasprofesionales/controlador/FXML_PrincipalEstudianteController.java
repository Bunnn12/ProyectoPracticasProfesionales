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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.SistemaGestionPracticasProfesionales;
import sistemagestionpracticasprofesionales.modelo.dao.EntregaDocumentoDAO;
import sistemagestionpracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemagestionpracticasprofesionales.modelo.dao.EvaluacionOvDAO;
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

    
    /**
    * Maneja el evento al hacer clic en el botón para subir documentos iniciales.
    * Verifica que la fecha actual esté dentro del periodo permitido para subir
    * documentos iniciales y que el estudiante tenga un proyecto asignado en el periodo actual.
    * Si ambas condiciones se cumplen, abre la ventana para subir documentos iniciales.
    * 
    * @param event El evento de acción generado por el clic en el botón.
    */
    @FXML
    private void clickSubirDocIniciales(ActionEvent event) {
        if (!EntregaDocumentoDAO.estaDentroDeRangoEntregaInicial()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Fuera de plazo", 
                "La fecha actual no está dentro del periodo permitido para subir documentos iniciales");
            return; 
        }
        boolean tieneProyecto = EstudianteDAO.tieneProyectoAsignadoPeriodoActual(estudianteSesion.getIdEstudiante());
        if (!tieneProyecto) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Proyecto no asignado", 
                "No puedes subir documentos iniciales porque aún no tienes un proyecto asignado");
            return;
        }
        try {
            Stage escenario = new Stage();
            Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_EntregaDocIniciales.fxml"));
            Scene escena = new Scene(vista);
            escenario.setScene(escena);
            escenario.setTitle("Subir documentos iniciales");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    /**
    * Maneja el evento al hacer clic en el botón para evaluar la organización vinculada (OV).
    * Verifica el estado actual de la evaluación para evitar evaluaciones duplicadas o si no
    * existe expediente asociado al estudiante. Si está permitido, abre la ventana para realizar la evaluación.
    * 
    * @param event El evento de acción generado por el clic en el botón.
    */
    @FXML
    private void clickEvaluarOV(ActionEvent event) {
        String estadoEvaluacion = EvaluacionOvDAO.obtenerEstadoEvaluacionOV(estudianteSesion.getIdEstudiante());

        if (estadoEvaluacion == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Si no tienes tu expediente creado, no puedes implementar esta opción, lo sentimos :(");
            return;
        }

        if (!estadoEvaluacion.equalsIgnoreCase("sin realizar")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Evaluación ya realizada",
                "Ya has realizado la evaluación de la organización vinculada y no puedes volver a evaluarla");
            return;
        }
        try {
            Stage escenario = new Stage();
            Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_BusquedaOV.fxml"));
            Scene escena = new Scene(vista);
            escenario.setScene(escena);
            escenario.setTitle("Evaluar ov- Busqueda OV");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    
}
