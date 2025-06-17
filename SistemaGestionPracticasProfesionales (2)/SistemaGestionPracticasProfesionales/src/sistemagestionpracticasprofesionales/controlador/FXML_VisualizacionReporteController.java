/**
 * Nombre del archivo: FXML_VisualizacionReporteController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 15/06/2025
 * Descripción: Controlador que permite visualizar un reporte de prácticas profesionales,
 * validarlo o enviar retroalimentación, abriendo el documento PDF en el visor predeterminado del sistema.
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Reporte;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la vista FXML_VisualizacionReporte.
 * Permite visualizar un reporte en PDF, validarlo o generar retroalimentación.
 */
public class FXML_VisualizacionReporteController implements Initializable {
    
    @FXML
    private Label lbNombreReporteVisualizado;
    @FXML
    private Label lbNombreEstudianteReporte;
    
    private FXML_ReportesDisponiblesEstudianteController controladorPrincipal;
    private Reporte reporteVisualizado;

    /**
     * Inicializa el controlador.
     *
     * @param url URL de localización.
     * @param rb ResourceBundle con recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    /**
     * Valida el reporte visualizado actual.
     * 
     * @param event Evento del botón Validar.
     */
    @FXML
    private void clickValidar(ActionEvent event) {
        if (reporteVisualizado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Validación", "No hay reporte cargado");
            return;
        }
         if ("Validado".equalsIgnoreCase(reporteVisualizado.getEstado())) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Ya validado", "El reporte ya ha sido validado");
            return;
        }
        try {
            boolean exito = ExpedienteDAO.actualizarEstadoReporte(reporteVisualizado.getIdReporte(), "Validado");
            if (exito) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "El reporte fue validado correctamente");

                if (controladorPrincipal != null) {
                    controladorPrincipal.cargarReportes();
                }

                Utilidad.cerrarVentanaActual(lbNombreReporteVisualizado);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo validar el reporte");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
        }
    }
    
    /**
     * Abre la ventana para retroalimentar el reporte.
     * 
     * @param event Evento del botón Retroalimentar.
     */

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
        }
    }

    /**
     * Cierra la ventana actual.
     * 
     * @param event Evento del botón Regresar.
     */
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(lbNombreEstudianteReporte);
    }
    
    /**
     * Carga el reporte a visualizar, lo muestra en pantalla y lo abre en el visor predeterminado del sistema.
     * 
     * @param reporte Objeto Reporte a visualizar.
     * @param nombreEstudiante Nombre del estudiante correspondiente al reporte.
     */
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
                Desktop.getDesktop().open(tempFile.toFile());  
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Aviso", "Tu sistema no soporta abrir archivos automáticamente.");
            }
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de archivo", "Ocurrió un error al guardar o abrir el archivo del reporte");
        } catch (UnsupportedOperationException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Operación no soportada", "Esta operación no es compatible con tu sistema");
        } catch (Exception e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error inesperado", "No se pudo cargar el reporte");
        }
    } else {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Información", "El reporte no contiene archivo");
    }

    }
    
    /**
     * Establece el controlador principal que permite refrescar la tabla al regresar.
     * 
     * @param controlador Controlador principal (pantalla anterior).
     */
    public void setControladorPrincipal(FXML_ReportesDisponiblesEstudianteController controlador) {
         this.controladorPrincipal = controlador;
    }

}
