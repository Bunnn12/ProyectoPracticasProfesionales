/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.awt.Desktop;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.DocumentoAnexo;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_VisualizacionDocumentosController implements Initializable {

    @FXML
    private TableView<DocumentoAnexo> tvDocumentos;
    @FXML
    private TableColumn<?, ?> colNombreDocumento;
    @FXML
    private TableColumn<?, ?> colFechaEntrega;
    @FXML
    private TableColumn<?, ?> colEstado;
    private String tipoDocumento;
    private int idExpediente;
    @FXML
    private Label lbNombreEstudiante;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaDocumentos();
        Estudiante estudianteSeleccionado = Sesion.getEstudianteSeleccionado();
        if (estudianteSeleccionado != null) {
            lbNombreEstudiante.setText(estudianteSeleccionado.getNombreCompleto());
        } else {
            lbNombreEstudiante.setText("Ningún estudiante seleccionado");
        }
    }    

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tvDocumentos);
    }

    @FXML
    private void clickValidar(ActionEvent event) {
    }

    @FXML
    private void clickRetroalimentar(ActionEvent event) {
    }
    
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        cargarDocumentos();
    }
    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }
    
    private void configurarTablaDocumentos(){
        colNombreDocumento.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaElaboracion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        }
    private void cargarDocumentos() {
        Task<ArrayList<DocumentoAnexo>> tarea = new Task<ArrayList<DocumentoAnexo>>() {
            @Override
            protected ArrayList<DocumentoAnexo> call() throws Exception {
                return ExpedienteDAO.obtenerDocumentosPorTipo(idExpediente, tipoDocumento);
            }
        };

        tarea.setOnSucceeded(e -> {
            tvDocumentos.getItems().setAll(tarea.getValue());
        });

        tarea.setOnFailed(e -> {
            tarea.getException().printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "No se pudieron obtener los documentos.");
        });

        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }


    @FXML
    private void seleccionarDocumento(MouseEvent event) {
        DocumentoAnexo docSeleccionado = tvDocumentos.getSelectionModel().getSelectedItem();
        if (docSeleccionado != null && docSeleccionado.getArchivo() != null) {
            try {
                byte[] archivoBytes = docSeleccionado.getArchivo();

                Path tempFile = Files.createTempFile("documento_temp_", ".pdf");
                Files.write(tempFile, archivoBytes);
                tempFile.toFile().deleteOnExit();

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(tempFile.toFile());
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No soportado", "Tu sistema no soporta abrir archivos automáticamente.");
                }
            } catch (Exception e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir el documento.");
                e.printStackTrace();
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Sin documento", "El documento no contiene archivo para mostrar.");
        }
    }
}
