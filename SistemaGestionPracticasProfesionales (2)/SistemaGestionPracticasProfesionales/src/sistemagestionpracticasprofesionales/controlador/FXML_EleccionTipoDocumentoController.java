/**
 * Nombre del archivo: FXML_EleccionTipoDocumentoController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 15/06/2025
 * Descripción: Controlador que permite seleccionar el tipo de documento para validar en el sistema de gestión de prácticas profesionales.
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
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.SistemaGestionPracticasProfesionales;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Expediente;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la vista FXML_EleccionTipoDocumento.
 * Permite seleccionar diferentes tipos de documentos entregados por un estudiante para 
 * posteriormente validarlos o retroalimentarlos
 */
public class FXML_EleccionTipoDocumentoController implements Initializable {

    @FXML
    private Label lbEstudianteSeleccionado;

    /**
     * Inicializa el controlador mostrando el nombre del estudiante seleccionado.
     *
     * @param url URL de localización del archivo FXML.
     * @param rb ResourceBundle con recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Estudiante estudiante = Sesion.getEstudianteSeleccionado();
        if (estudiante != null) {
            lbEstudianteSeleccionado.setText(estudiante.getNombreCompleto());
        } else {
            lbEstudianteSeleccionado.setText("Ningún estudiante seleccionado");
        }
    }    
    
    /**
     * Cierra la ventana actual.
     * 
     * @param event Evento del botón regresar.
     */
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(lbEstudianteSeleccionado);
    }

    /**
     * Abre la visualización de documentos iniciales entregados por el estudiante
     *
     * @param event Evento del botón validar documentos iniciales.
     */
    @FXML
    private void clickValidarDocIniciales(ActionEvent event) {
        abrirVisualizacionDocumentos("inicial");
    }
    
    /**
     * Abre la visualización de documentos finales.
     *
     * @param event Evento del botón validar documentos finales entregados por el estudiante
     */

    @FXML
    private void clickValidarDocFinales(ActionEvent event) {
        abrirVisualizacionDocumentos("final");
    }
    
    /**
     * Abre la visualización de reportes entregados por el estudiante.
     *
     * @param event Evento del botón validar reportes.
     */
    @FXML
    private void clickValidarDocReportes(ActionEvent event) {
        try {
            Estudiante estudiante = Sesion.getEstudianteSeleccionado();
            if (estudiante == null) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin estudiante", "No se encontró estudiante seleccionado.");
                return;
            }

            Expediente expediente = ExpedienteDAO.obtenerExpedientePorIdEstudiante(estudiante.getIdEstudiante());
            if (expediente == null) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin expediente", "El estudiante no tiene expediente asignado.");
                return;
            }

            int totalReportes = ExpedienteDAO.contarReportes(expediente.getIdExpediente());
            if (totalReportes == 0) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Sin reportes", "El estudiante no ha entregado ningún reporte.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_ReportesDisponiblesEstudiante.fxml"));
            Parent vista = loader.load();

            FXML_ReportesDisponiblesEstudianteController controlador = loader.getController();
            controlador.setIdEstudiante(estudiante.getIdEstudiante());

            Stage escenario = new Stage();
            Scene escena = new Scene(vista);
            escenario.setScene(escena);
            escenario.setTitle("Reportes disponibles");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de archivo", "Ocurrió un error al abrir o manejar archivos");
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
        }
    }
    
    /**
     * Abre la visualización de documentos intermedios.
     *
     * @param event Evento del botón validar documentos intermedios entregados por el estudiante
     */
    @FXML
    private void clickValidarDocIntermedios(ActionEvent event) {
        abrirVisualizacionDocumentos("intermedio");
    }

    /**
     * Cierra la ventana actual si el usuario lo confirma
     *
     * @param event Evento del botón cancelar.
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(lbEstudianteSeleccionado);
        } 
    }

    /**
     * Abre la vista de visualización de documentos según el tipo especificado.
     * 
     * @param tipoDocumento Tipo de documento a visualizar (inicial, final, intermedio)
     */
    private void abrirVisualizacionDocumentos(String tipoDocumento) {
        try {
            Estudiante estudiante = Sesion.getEstudianteSeleccionado();
            if (estudiante == null) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin estudiante", "No se encontró estudiante seleccionado.");
                return;
            }

            Expediente expediente = ExpedienteDAO.obtenerExpedientePorIdEstudiante(estudiante.getIdEstudiante());
            if (expediente == null) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin expediente", "El estudiante no tiene expediente asignado.");
                return;
            }

            int totalEntregados;
            if (tipoDocumento.equalsIgnoreCase("reportes")) {
                totalEntregados = ExpedienteDAO.contarReportes(expediente.getIdExpediente());
            } else {
                totalEntregados = ExpedienteDAO.contarDocumentosPorTipo(expediente.getIdExpediente(), tipoDocumento);
            }

            if (totalEntregados == 0) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Sin documentos", "El estudiante no ha entregado ningún documento de tipo: " + tipoDocumento);
                return;
            }

            FXMLLoader loader = new FXMLLoader(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_VisualizacionDocumentos.fxml"));
            Parent vista = loader.load();

            FXML_VisualizacionDocumentosController controlador = loader.getController();
            controlador.setTipoDocumento(tipoDocumento);
            controlador.setIdExpediente(expediente.getIdExpediente());

            Stage stage = new Stage();
            stage.setScene(new Scene(vista));
            stage.setTitle("Visualización documentos " + tipoDocumento);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la visualización de documentos");
        } catch (SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
        }
    }

}

    

