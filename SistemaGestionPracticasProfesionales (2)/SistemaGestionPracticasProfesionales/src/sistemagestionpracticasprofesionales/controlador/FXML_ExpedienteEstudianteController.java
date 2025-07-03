/**
    * Nombre del archivo: FXML_ExpedienteEstudianteController.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 16/06/2025
    * Descripción: Controlador que permite ver el expediente del estudiante.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Desktop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import sistemagestionpracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.ExpedienteCompleto;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.DocumentoAnexo;
import sistemagestionpracticasprofesionales.modelo.pojo.Reporte;
import sistemagestionpracticasprofesionales.interfaz.IArchivoPDF;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * Controlador que permite ver el expediente del estudiante.
 */
public class FXML_ExpedienteEstudianteController implements Initializable {

    @FXML
    private TableView<IArchivoPDF> tvArchivos;
    @FXML
    private TableColumn<IArchivoPDF, String> colNombre;
    @FXML
    private TableColumn<IArchivoPDF, Date> colFechaEntrega;
    @FXML
    private TableColumn<IArchivoPDF, String> colEstado;
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

    private ObservableList<IArchivoPDF> listaArchivos = FXCollections.observableArrayList();
    private int idEstudiante;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Al hacer doble clic en una fila
        tvArchivos.setRowFactory(tv -> {
            TableRow<IArchivoPDF> fila = new TableRow<>();
            fila.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !fila.isEmpty()) {
                    IArchivoPDF archivoSeleccionado = fila.getItem();
                    abrirReportePDF(archivoSeleccionado);
                }
            });
            return fila;
        });
    }    
    
    /**
     * Inicializa los datos del expediente del estudiante
     */
    public void inicializarDatos(Estudiante estudiante) {
        this.idEstudiante = estudiante.getIdEstudiante(); // Asignar correctamente el ID
        
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
                
                listaArchivos.clear();
                listaArchivos.addAll(ExpedienteDAO.obtenerDocumentosInicialesPorEstudiante(idEstudiante));
                listaArchivos.addAll(ExpedienteDAO.obtenerReportesPorEstudiante(idEstudiante));
                tvArchivos.setItems(listaArchivos);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Sin expediente", "El estudiante seleccionado no tiene expediente registrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXML_ExpedienteEstudianteController.class.getName()).log(Level.SEVERE, null, ex);
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar el expediente del estudiante.");
        }
    }
    
    private void abrirReportePDF(IArchivoPDF archivo) {
        if (archivo.getArchivo() == null || archivo.getArchivo().length == 0) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin archivo", "Este reporte no contiene un archivo PDF adjunto.");
            return;
        }

        try {
            // Crear archivo temporal con nombre adecuado
            String nombreLimpio = archivo.getNombre().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_\\.]", "");
            if (!nombreLimpio.toLowerCase().endsWith(".pdf")) {
                nombreLimpio += ".pdf";
            }

            File tempFile = File.createTempFile("archivo_", "_" + archivo.getNombre().replaceAll("[^a-zA-Z0-9]", "_") + ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(archivo.getArchivo());
            }

            // Abrir el archivo con el visor de PDF por defecto
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "El visor de archivos no está disponible en este sistema.");
            }

        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir el archivo.");
            e.printStackTrace();
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
