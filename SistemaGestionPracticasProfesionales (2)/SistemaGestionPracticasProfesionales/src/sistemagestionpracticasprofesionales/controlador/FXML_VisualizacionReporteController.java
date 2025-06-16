/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Reporte;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_VisualizacionReporteController implements Initializable {

    private Reporte reporteVisualizado;
    @FXML
    private Label lbNombreReporteVisualizado;
    @FXML
    private Label lbNombreEstudianteReporte;
    private FXML_ReportesDisponiblesEstudianteController controladorPrincipal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickValidar(ActionEvent event) {
        if (reporteVisualizado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Validación", "No hay reporte cargado");
            return;
        }
        try {
            boolean exito = ExpedienteDAO.actualizarEstadoReporte(reporteVisualizado.getIdReporte(), "Validado");
            if (exito) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "El reporte fue validado correctamente");

                // Recargar tabla en ventana principal
                if (controladorPrincipal != null) {
                    controladorPrincipal.cargarReportes();
                }

                Utilidad.cerrarVentanaActual(lbNombreReporteVisualizado);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo validar el reporte");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
            e.printStackTrace();
        }
    }

    @FXML
    private void clickRetroalimentar(ActionEvent event) {
        if (reporteVisualizado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Retroalimentación", "No hay reporte cargado");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_RetroalimentacionDocumento.fxml"));
            Parent vista = loader.load();

            FXML_RetroalimentacionDocumentoController controlador = loader.getController();
            controlador.setDatosDocumento(reporteVisualizado.getNombre(), lbNombreEstudianteReporte.getText(), reporteVisualizado.getIdReporte());

            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Retroalimentación del reporte");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de retroalimentación");
            e.printStackTrace();
        }
    }

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(lbNombreEstudianteReporte);
    }
    
    
    public void cargarReporte(Reporte reporte, String nombreEstudiante) {
        this.reporteVisualizado = reporte;
        lbNombreReporteVisualizado.setText(reporte.getNombre());
        lbNombreEstudianteReporte.setText(nombreEstudiante);

        byte[] archivo = reporte.getArchivo();

        if (archivo != null) {
        try {
            Path tempFile = Files.createTempFile("reporte_temp_", ".pdf");
            Files.write(tempFile, archivo);
            tempFile.toFile().deleteOnExit();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile.toFile());  // Abre con el programa predeterminado
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Aviso", "Tu sistema no soporta abrir archivos automáticamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar el reporte.");
        }
    } else {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Información", "El reporte no contiene archivo.");
    }

    }
    
   public void setControladorPrincipal(FXML_ReportesDisponiblesEstudianteController controlador) {
         this.controladorPrincipal = controlador;
    }

}
