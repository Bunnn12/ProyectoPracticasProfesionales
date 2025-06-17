/**
 * Nombre del archivo: FXML_RetroalimentacionDocumentoController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 16/06/2025
 * Descripción: Controlador para gestionar la retroalimentación de un documento específico,
 * permitiendo visualizar el nombre del documento y estudiante, así como guardar la retroalimentación.
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
 * Controlador para la vista FXML_RetroalimentacionDocumento.
 * Permite mostrar los datos del documento y del estudiante, 
 * así como guardar y cancelar la retroalimentación del documento.
 */
public class FXML_RetroalimentacionDocumentoController implements Initializable {

    @FXML
    private Label lbNombreDocumento;
    @FXML
    private Label lbEstudianteDocumento;
    @FXML
    private TextArea taRetroalimentacionDocumento;
    
    private int idDocumento;
    
    /**
     * Inicializa el controlador.
     * 
     * @param url URL de localización del archivo FXML.
     * @param rb ResourceBundle con recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    /**
     * Maneja el evento para regresar y cerrar la ventana actual.
     * 
     * @param event Evento de clic en el botón regresar.
     */
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(lbNombreDocumento);
    }

    /**
     * Maneja el evento para aceptar y guardar la retroalimentación.
     * Valida que se pueda actualizar en la base de datos y muestra alertas según resultado.
     * 
     * @param event Evento de clic en el botón aceptar.
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        String retroalimentacion = taRetroalimentacionDocumento.getText().trim();
        if (retroalimentacion.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campo vacío", 
                "Por favor escribe la retroalimentación antes de guardar");
            return;
        }

        int maxPalabras = 100;
        String[] palabras = retroalimentacion.trim().split("\\s+");

        if (palabras.length < 3) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Texto insuficiente", 
                "La retroalimentación debe contener al menos 3 palabras");
            return;
        }

        if (palabras.length > maxPalabras) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Texto demasiado largo", 
                "La retroalimentación no debe exceder las " + maxPalabras + " palabras");
            return;
        }

        if (retroalimentacion.replaceAll("\\s", "").isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Contenido inválido", 
                "No se permiten comentarios con solo espacios o saltos de línea.");
            return;
        }
        String[] palabrasProhibidas = {"tonto", "inútil", "basura"}; 
        for (String palabra : palabrasProhibidas) {
            if (retroalimentacion.toLowerCase().contains(palabra)) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Contenido inapropiado",
                    "La retroalimentación contiene palabras no permitidas");
                return;
            }
        }

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

    /**
     * Maneja el evento para cancelar y cerrar la ventana si el usuario confirma.
     * 
     * @param event Evento de clic en el botón cancelar.
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Seguro Cancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(lbEstudianteDocumento);
        } 
    }
    
    /**
     * Establece los datos del documento y estudiante para mostrar en la vista.
     * 
     * @param nombreDocumento Nombre del documento.
     * @param nombreEstudiante Nombre del estudiante.
     * @param idDocumento ID del documento.
     */
    public void setDatosDocumento(String nombreDocumento, String nombreEstudiante, int idDocumento) {
        lbNombreDocumento.setText(nombreDocumento);
        lbEstudianteDocumento.setText(nombreEstudiante);
        this.idDocumento = idDocumento;
    }
}
