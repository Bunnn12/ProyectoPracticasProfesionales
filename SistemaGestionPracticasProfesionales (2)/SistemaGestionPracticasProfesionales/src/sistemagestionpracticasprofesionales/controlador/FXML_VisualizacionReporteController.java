/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.awt.Desktop;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Reporte;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_VisualizacionReporteController implements Initializable {

    private WebView wvReporteVisualizado;
    private Reporte reporteVisualizado;
    @FXML
    private Label lbNombreReporteVisualizado;
    @FXML
    private Label lbNombreEstudianteReporte;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickValidar(ActionEvent event) {
    }

    @FXML
    private void clickRetroalimentar(ActionEvent event) {
    }

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(wvReporteVisualizado);
    }
    
    
    public void cargarReporte(Reporte reporte, String nombreEstudiante) {
        this.reporteVisualizado = reporte;
        // Poner el nombre del reporte en el label
        lbNombreReporteVisualizado.setText(reporte.getNombre());
        // Poner el nombre del estudiante en el label
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
                    wvReporteVisualizado.getEngine().loadContent(
                        "<html><body><h3>Tu sistema no soporta abrir archivos automáticamente.</h3></body></html>"
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                wvReporteVisualizado.getEngine().loadContent(
                    "<html><body><h3>No se pudo cargar el reporte</h3></body></html>"
                );
            }
        } else {
            wvReporteVisualizado.getEngine().loadContent(
                "<html><body><h3>El reporte no contiene archivo</h3></body></html>"
            );
        }
    }


}
