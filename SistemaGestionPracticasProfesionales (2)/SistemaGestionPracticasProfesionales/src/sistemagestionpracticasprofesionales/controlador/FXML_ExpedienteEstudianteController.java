/**
    * Nombre del archivo: FXML_ExpedienteEstudianteController.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 16/06/2025
    * Descripción: Controlador que permite ver el expediente del estudiante.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import sistemagestionpracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.ExpedienteCompleto;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * Controlador que permite ver el expediente del estudiante.
 */
public class FXML_ExpedienteEstudianteController implements Initializable {

    @FXML
    private Label lbEstudiante;
    @FXML
    private Label lbProyecto;
    @FXML
    private Label lbOV;
    @FXML
    private Label lbFechaInicio;
    @FXML
    private Label lbFechaFin;
    @FXML
    private Label lbHorasAcumuladas;
    @FXML
    private Label lbEstatus;
    @FXML
    private Label lbEvaluacionPresentación;
    @FXML
    private Label lbPuntajeObtenido;
    @FXML
    private Label lbEvaluacionOV;

    private int idEstudiante;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * Inicializa los datos del expediente del estudiante
     */
    public void inicializarDatos(Estudiante estudiante) {
        try {
            ExpedienteCompleto datos = EstudianteDAO.obtenerDatosExpedienteCompletoPorIdEstudiante(estudiante.getIdEstudiante());

            if (datos != null) {
                lbEstudiante.setText(datos.getNombreEstudiante());
                lbProyecto.setText(datos.getNombreProyecto());
                lbOV.setText(datos.getNombreOV());
                lbFechaInicio.setText(datos.getFechaInicio().toString());
                lbFechaFin.setText(datos.getFechaFin().toString());
                lbHorasAcumuladas.setText(String.valueOf(datos.getHorasAcumuladas()));
                lbEstatus.setText(datos.getEstado());
                lbEvaluacionPresentación.setText(datos.getEvaluacionPresentacion());
                if (datos.getPuntajeObtenido() == 0) {
                lbPuntajeObtenido.setText("sin calificar");
            } else {
                lbPuntajeObtenido.setText(String.valueOf(datos.getPuntajeObtenido()));
            }
                lbEvaluacionOV.setText(datos.getEvaluacionOV());
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Sin expediente", "El estudiante seleccionado no tiene expediente registrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXML_ExpedienteEstudianteController.class.getName()).log(Level.SEVERE, null, ex);
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar el expediente del estudiante.");
        }
    }
    
    /**
     * Cierra la ventana actual
     * @param event Evento acción del botón aceptar
     */
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(lbEstudiante);
    }

    /**
     * Cierra la ventana actual
     * @param event Evento acción del botón aceptar
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(lbEstudiante);
    }
}
