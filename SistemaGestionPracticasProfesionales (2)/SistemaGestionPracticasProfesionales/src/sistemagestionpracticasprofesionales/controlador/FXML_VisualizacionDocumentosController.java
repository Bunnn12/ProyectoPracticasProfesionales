/**
 * Nombre del archivo: FXML_VisualizacionDocumentosController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 15/06/2025
 * Descripción: Controlador para la vista que permite visualizar, validar y retroalimentar documentos anexos
 * asociados a un expediente y tipo de documento específico.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.DocumentoAnexo;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la gestión y visualización de documentos entregados
 * por el estudiante, ya sean iniciales, finales o intermedios, además de
 * la opción de poder validarlos o retroalimentarlos según se desee
 */
public class FXML_VisualizacionDocumentosController implements Initializable {

    @FXML
    private TableView<DocumentoAnexo> tvDocumentos;
    @FXML
    private TableColumn colNombreDocumento;
    @FXML
    private TableColumn colFechaEntrega;
    @FXML
    private TableColumn colEstado;
    @FXML
    private Label lbNombreEstudiante;

    private String tipoDocumento;
    private int idExpediente;
    
    /**
     * Inicializa el controlador, configura la tabla y muestra el nombre del estudiante seleccionado en sesión.
     *
     * @param url URL de localización.
     * @param rb ResourceBundle para internacionalización.
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

    /**
     * Valida el documento seleccionado actualizando su estado a "Validado" y recarga la tabla.
     *
     * @param event Evento del botón validar
     */
    @FXML
    private void clickValidar(ActionEvent event) {
        DocumentoAnexo documentoSeleccionado = tvDocumentos.getSelectionModel().getSelectedItem();
        if (documentoSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Validación", "Debes seleccionar un documento para validar");
            return;
        }

         if ("Validado".equalsIgnoreCase(documentoSeleccionado.getEstado())) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Documento ya validado", "El documento ya ha sido validado");
            return;
        }
        try {
            boolean actualizado = ExpedienteDAO.actualizarEstadoDocumento(documentoSeleccionado.getIdDocumentoAnexo(), "Validado");
            if (actualizado) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Documento validado", "Documento validado correctamente");
                cargarDocumentos(); 
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo validar el documento");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
        }
    }

    /**
     * Abre la ventana de retroalimentación para el documento seleccionado y recarga la tabla después.
     *
     * @param event Evento del botón retroalimentar.
     */
    @FXML
    private void clickRetroalimentar(ActionEvent event) {
        DocumentoAnexo documentoSeleccionado = tvDocumentos.getSelectionModel().getSelectedItem();
        if (documentoSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Retroalimentación", "Debes seleccionar un documento para retroalimentar");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_RetroalimentacionDocumento.fxml"));
            Parent vista = loader.load();

            FXML_RetroalimentacionDocumentoController controlador = loader.getController();
            controlador.setDatosDocumento(documentoSeleccionado.getNombre(), lbNombreEstudiante.getText(), documentoSeleccionado.getIdDocumentoAnexo());

            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Retroalimentación documento");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

            cargarDocumentos();
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de retroalimentación.");
        }
    }
    
    /**
     * Establece el tipo de documento y carga los documentos correspondientes.
     *
     * @param tipoDocumento Tipo de documento a filtrar.
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        cargarDocumentos();
    }
    
    /**
     * Establece el ID del expediente.
     *
     * @param idExpediente ID del expediente.
     */
    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }
    
    /**
     * Configura las columnas de la tabla de documentos.
     */
    private void configurarTablaDocumentos(){
        colNombreDocumento.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaElaboracion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }
    
    /**
     * Carga los documentos según el tipo y expediente en un hilo separado para no bloquear la UI.
     */
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

    /**
     * Evento para abrir el archivo PDF del documento seleccionado al hacer doble clic en la tabla.
     *
     * @param event Evento de mouse.
     */
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
            } catch (IOException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de archivo", "Ocurrió un problema al guardar o abrir el archivo");
            } catch (UnsupportedOperationException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No compatible", "Tu sistema no permite esta operación");
            } catch (Exception e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error inesperado", "Ocurrió un error inesperado al intentar abrir el documento");
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Sin documento", "El documento no contiene archivo para mostrar.");
        }
    }
}
