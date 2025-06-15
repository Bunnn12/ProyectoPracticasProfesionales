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
import javafx.scene.control.Label;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_EleccionTipoDocumentoController implements Initializable {

    @FXML
    private Label lbEstudianteSeleccionado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Estudiante estudiante = Sesion.getEstudianteSeleccionado();
        if (estudiante != null) {
            lbEstudianteSeleccionado.setText(estudiante.getNombreCompleto());
        } else {
            lbEstudianteSeleccionado.setText("Ningún estudiante seleccionado");
        }
    }    

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(lbEstudianteSeleccionado);
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
        Utilidad.cerrarVentanaActual(lbEstudianteSeleccionado);
        } 
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
    }

    @FXML
    private void clickValidarDocIniciales(ActionEvent event) {
    }

    @FXML
    private void clickValidarDocFinales(ActionEvent event) {
    }

    @FXML
    private void clickValidarDocReportes(ActionEvent event) {
    }

    @FXML
    private void clickValidarDocIntermedios(ActionEvent event) {
    }
    
}
