/**
    * Nombre del archivo: FXML_BusquedaExpedienteController.java
    * Autor: Juan Pablo Silva Miranda
    * Fecha: 13/06/2025
    * Descripción: Controlador que permite buscar el expediente del estudiante si es que existe.
*/
package sistemagestionpracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
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
    }    

    @FXML
    private void clickBuscar(ActionEvent event) {
        String matricula = tfBuscar.getText().trim();

        // **Validación 1: Campo de matrícula vacío**
        // Si el campo de texto está vacío, muestra una alerta y no hace nada más.
        if (matricula.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campo vacío", "Por favor escribe una matrícula para buscar.");
            return; // Sale del método
        }
        // **Validación 2: Formato de matrícula inválido**
        // Verifica que la matrícula cumpla con el formato "S" seguido de 8 dígitos.
        if (!matricula.matches("S\\d{8}")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Formato inválido", "La matrícula debe iniciar con 'S' mayúscula seguida de 8 dígitos numéricos (ejemplo: S12345678).");
            return; // Sale del método
        }

        try {
            // **Validación 3: Búsqueda en la base de datos**
            // Intenta buscar el ID del expediente usando la matrícula.
            Integer idExpediente = ExpedienteDAO.buscarIdExpedientePorMatricula(matricula);

            if (idExpediente != null) {
                abrirActualizarExpediente(idExpediente);
            } else {
                // Si el ID es nulo, significa que no se encontró ningún expediente con esa matrícula.
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "No encontrado", "No se encontró ningún expediente con esa matrícula.");
            }
        } catch (SQLException e) {
           
            e.printStackTrace(); 
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Base de Datos", "Ocurrió un error al intentar buscar el expediente en la base de datos.");
        } catch (IOException e) {
           
            e.printStackTrace(); 
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Carga", "Ocurrió un error al cargar la ventana para actualizar el expediente.");
        }
    }

    /**
     * Abre la ventana de actualización del expediente.
     * @param idExpediente El ID del expediente a actualizar.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    private void abrirActualizarExpediente(int idExpediente) throws IOException {
        // Carga el archivo FXML de la vista de ActualizarExpediente.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_ActualizarExpediente.fxml"));
        Parent root = loader.load();

        // Obtiene el controlador de la vista cargada y le pasa el ID del expediente.
        FXML_ActualizarExpedienteController controlador = loader.getController();
        controlador.recibirIdExpediente(idExpediente);
        Stage stage = new Stage();
        stage.setTitle("Actualizar Expediente"); 
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.showAndWait(); 
    }
}