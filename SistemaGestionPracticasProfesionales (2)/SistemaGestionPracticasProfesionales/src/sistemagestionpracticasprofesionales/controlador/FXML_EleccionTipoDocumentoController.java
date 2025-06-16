/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import javafx.stage.Window;
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
import sistemagestionpracticasprofesionales.modelo.pojo.Usuario;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_EleccionTipoDocumentoController implements Initializable {

    @FXML
    private Label lbEstudianteSeleccionado;

    /**
     * Initializes the controller class.
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

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(lbEstudianteSeleccionado);
    }


    @FXML
    private void clickValidarDocIniciales(ActionEvent event) {
        abrirVisualizacionDocumentos("inicial");
    }

    @FXML
    private void clickValidarDocFinales(ActionEvent event) {
        abrirVisualizacionDocumentos("final");
    }

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

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir los reportes.");
        }
    }

    @FXML
    private void clickValidarDocIntermedios(ActionEvent event) {
        abrirVisualizacionDocumentos("intermedio");
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Cancelar", "¿Estás seguro que quieres cancelar y volver a la pantalla principal?");
        if (!confirmado) return;

        Usuario usuarioSesion = Sesion.getUsuarioSeleccionado();
        if (usuarioSesion == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de sesión", "No se pudo recuperar la sesión del usuario.");
            return;
        }

        String rol = usuarioSesion.getRol();
        String fxmlDestino = null;
        String tituloVentana = null;

        switch (rol.toLowerCase().trim()) {
            case "coordinador":
                fxmlDestino = "/sistemagestionpracticasprofesionales/vista/FXML_PrincipalCoordinador.fxml";
                tituloVentana = "Principal Coordinador";
                break;
            case "evaluador":
                fxmlDestino = "/sistemagestionpracticasprofesionales/vista/FXML_PrincipalEvaluador.fxml";
                tituloVentana = "Principal Evaluador";
                break;
            case "profesor ee":
                fxmlDestino = "/sistemagestionpracticasprofesionales/vista/FXML_PrincipalProfesorEE.fxml";
                tituloVentana = "Principal Profesor EE";
                break;
            default:
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Rol desconocido", "No se puede determinar a qué pantalla volver.");
                return;
        }

        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource(fxmlDestino));
            Parent vista = cargador.load();

            Object controlador = cargador.getController();
            controlador.getClass().getMethod("inicializarInformacion", Usuario.class).invoke(controlador, usuarioSesion);

            Utilidad.cerrarVentanaActual(lbEstudianteSeleccionado);
            Stage nuevaVentana = new Stage();
            nuevaVentana.setScene(new Scene(vista));
            nuevaVentana.setTitle(tituloVentana);
            nuevaVentana.centerOnScreen();
            nuevaVentana.show();


        } catch (IOException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | java.lang.reflect.InvocationTargetException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al regresar", "No se pudo regresar a la pantalla principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

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

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la visualización de documentos.");
        }
    }

}

    

