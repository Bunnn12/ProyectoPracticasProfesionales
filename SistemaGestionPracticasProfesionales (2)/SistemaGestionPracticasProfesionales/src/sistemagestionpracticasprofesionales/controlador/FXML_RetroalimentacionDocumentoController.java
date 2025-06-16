/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_RetroalimentacionDocumentoController implements Initializable {

    @FXML
    private Label lbNombreDocumento;
    @FXML
    private Label lbEstudianteDocumento;
    private int idDocumento;
    @FXML
    private TextArea taRetroalimentacionDocumento;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(lbNombreDocumento);
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
            String retroalimentacion = taRetroalimentacionDocumento.getText().trim();
        try {
            boolean actualizado = ExpedienteDAO.actualizarRetroalimentacionDocumento(idDocumento, retroalimentacion);
            if (actualizado) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Retroalimentación guardada correctamente");
                Utilidad.cerrarVentanaActual(lbEstudianteDocumento);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo guardar");
            }
        } catch (Exception e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
            e.printStackTrace();
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(lbEstudianteDocumento);
        } 
    }
    public void setDatosDocumento(String nombreDocumento, String nombreEstudiante, int idDocumento) {
        lbNombreDocumento.setText(nombreDocumento);
        lbEstudianteDocumento.setText(nombreEstudiante);
        this.idDocumento = idDocumento;
    }
}
