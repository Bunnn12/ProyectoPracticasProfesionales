/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Expediente;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author PABLO
 */
public class FXML_ActualizarExpedienteController implements Initializable {

    @FXML
    private Label lbEstudiante;
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbProyecto;
    @FXML
    private Label lbHoras;
    @FXML
    private Label lbHorasAgregar;
    @FXML
    private TextField tfAgregarHoras;

     private Expediente expedienteActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Solo permitir números
        tfAgregarHoras.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfAgregarHoras.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        // Establecer textos por defecto
    lbEstudiante.setText("Nombre:");
    lbMatricula.setText("Matrícula:");
    lbProyecto.setText("Proyecto:");
    lbHoras.setText("Horas:");
    lbHorasAgregar.setText("Agregar Horas:");
}
    public void recibirIdExpediente(Integer idExpediente) {
        try {
            expedienteActual = ExpedienteDAO.obtenerExpedienteConEstudiante(idExpediente);
            if (expedienteActual != null) {
                Estudiante estudiante = expedienteActual.getEstudiante();
                lbEstudiante.setText(estudiante.getNombre() + " " +
                                     estudiante.getApellidoPaterno() + " " +
                                     estudiante.getApellidoMaterno());
                lbMatricula.setText(estudiante.getMatricula());
                lbHoras.setText(String.valueOf(expedienteActual.getHorasAcumuladas()));
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se encontró el expediente.");
                cerrarVentana();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "No se pudo cargar el expediente.");
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
        if (expedienteActual == null) return;

        String horasTexto = tfAgregarHoras.getText().trim();
        if (horasTexto.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campo vacío", "Ingresa las horas a agregar.");
            return;
        }

        int horasAgregar = Integer.parseInt(horasTexto);
        int nuevasHoras = expedienteActual.getHorasAcumuladas() + horasAgregar;

        try {
            boolean actualizado = ExpedienteDAO.actualizarHorasExpediente(expedienteActual.getIdExpediente(), nuevasHoras);
            if (actualizado) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Horas actualizadas correctamente.");
                cerrarVentana();
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudieron actualizar las horas.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "Ocurrió un error al actualizar las horas.");
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) tfAgregarHoras.getScene().getWindow();
        stage.close();
    }
}