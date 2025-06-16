/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author PABLO
 */
public class FXML_BusquedaExpedienteController implements Initializable {

    @FXML
    private TextField tfBuscar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
private void clickBuscar(ActionEvent event) {
    String texto = tfBuscar.getText().trim();

    if (texto.isEmpty()) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campo vacío", "Por favor escribe un nombre o matrícula.");
        return;
    }

    try {
        Integer idExpediente = ExpedienteDAO.buscarIdExpedientePorNombreOMatricula(texto);

        if (idExpediente != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_ActualizarExpediente.fxml"));
            Parent root = loader.load();

            FXML_ActualizarExpedienteController controlador = loader.getController();
            controlador.recibirIdExpediente(idExpediente);

            Stage stage = new Stage();
            stage.setTitle("Actualizar Expediente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "No encontrado", "No se encontró ningún expediente con ese nombre o matrícula.");
        }
    } catch (SQLException | IOException e) {
        e.printStackTrace();
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Ocurrió un error al buscar el expediente.");
    }
}

}
