/**
 * Nombre del archivo: FXML_ActualizarExpedienteController.java
 * Autor: Juan Pablo Silva Miranda
 * Fecha: 17/06/2025
 * Descripción: Controlador JavaFX encargado de gestionar la actualización de horas 
 * y evaluaciones de un expediente de prácticas profesionales. Muestra los datos del expediente 
 * y permite actualizar su estado, horas acumuladas y evaluaciones.
 */

package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Expediente;
import sistemagestionpracticasprofesionales.modelo.pojo.ExpedienteCompleto;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

public class FXML_ActualizarExpedienteController implements Initializable {

    @FXML private Label lbEstudiante;
    @FXML private Label lbMatricula;
    @FXML private Label lbProyecto;
    @FXML private Label lbHoras;
    @FXML private Label lbHorasAgregar;
    @FXML private TextField tfAgregarHoras;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private Label lbFechaInicio;
    @FXML private Label lbFechaFin;
    @FXML private Label lbEvaluacionPresentacion;
    @FXML private Label lbEvaluacionOV;
    @FXML private Label lbEstado;
    @FXML private ComboBox<String> cbEstado;

    private Expediente expedienteActual;
    private ExpedienteCompleto expedienteCompletoActual;

    /**
     * Inicializa el controlador al cargar la vista.
     * Establece validaciones para el campo de horas y valores predeterminados en los ComboBox.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfAgregarHoras.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfAgregarHoras.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        lbEstudiante.setText("Nombre:");
        lbMatricula.setText("Matrícula:");
        lbProyecto.setText("Proyecto:");
        lbHoras.setText("Horas:");
        lbHorasAgregar.setText("Agregar Horas:");
        cbEstado.getItems().addAll("En progreso", "Concluido");
        cbEstado.setValue("En progreso");

        dpFechaInicio.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(java.time.LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(java.time.LocalDate.now()));
            }
        });

        dpFechaInicio.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                dpFechaFin.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
                    @Override
                    public void updateItem(java.time.LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        java.time.LocalDate maxDate = newDate.plusMonths(6);
                        setDisable(empty || item.isBefore(newDate) || item.isAfter(maxDate));
                    }
                });
            }
        });
    }

    /**
     * Recibe el ID del expediente y carga la información relacionada en la interfaz.
     * @param idExpediente ID del expediente a mostrar.
     */
    public void recibirIdExpediente(Integer idExpediente) {
        try {
            expedienteActual = ExpedienteDAO.obtenerExpedienteConEstudianteYProyecto(idExpediente); 
            expedienteCompletoActual = ExpedienteDAO.obtenerExpedienteCompleto(idExpediente);

            if (expedienteActual != null) {
                Estudiante estudiante = expedienteActual.getEstudiante();

                lbEstudiante.setText("Nombre: " + estudiante.getNombre() + " " +
                                     estudiante.getApellidoPaterno() + " " +
                                     estudiante.getApellidoMaterno());

                lbMatricula.setText("Matrícula: " + estudiante.getMatricula());

                if (expedienteActual.getProyecto() != null &&
                    expedienteActual.getProyecto().getNombre() != null &&
                    !expedienteActual.getProyecto().getNombre().isEmpty()) {
                    lbProyecto.setText("Proyecto: " + expedienteActual.getProyecto().getNombre());
                } else {
                    lbProyecto.setText("Proyecto: No asignado");
                }

                lbHoras.setText("Horas: " + expedienteActual.getHorasAcumuladas());
                cbEstado.setValue(expedienteActual.getEstado());

            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se encontró el expediente.");
                cerrarVentana();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "No se pudo cargar el expediente.");
        }
    }

    /**
     * Maneja el evento del botón "Aceptar".
     * Valida las horas a agregar y actualiza la información del expediente.
     * @param event Evento de acción generado por el botón.
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        if (expedienteActual == null) return;

        String horasTexto = tfAgregarHoras.getText().trim();

        if (horasTexto.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campo vacío", "Ingresa las horas a agregar.");
            return;
        }

        try {
            int horasAgregar = Integer.parseInt(horasTexto);

            if (horasAgregar <= 0) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Horas inválidas", "Debes agregar al menos una hora.");
                return;
            }

            if (horasAgregar > 130) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Límite excedido", "No puedes agregar más de 130 horas en una sola operación.");
                return;
            }

            int horasActuales = expedienteActual.getHorasAcumuladas();
            int nuevasHoras = horasActuales + horasAgregar;

            if (nuevasHoras > 420) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Límite excedido", 
                    "No puedes exceder las 420 horas acumuladas. Actualmente tienes " + horasActuales + " horas.");
                return;
            }
            String estado = cbEstado.getValue();

            boolean horasActualizadas = ExpedienteDAO.actualizarHorasExpediente(
                expedienteActual.getIdExpediente(), nuevasHoras);

            boolean estadoActualizado = ExpedienteDAO.actualizarEstadoExpediente(
            expedienteActual.getIdExpediente(), estado);


            if (horasActualizadas && estadoActualizado) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Expediente actualizado correctamente.");
                cerrarVentana();
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudieron actualizar los datos del expediente.");
            }

        } catch (NumberFormatException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido", "Ingresa un número válido para las horas.");
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "Ocurrió un error al actualizar el expediente.");
        }
    }

    /**
     * Maneja el evento del botón "Cancelar", cerrando la ventana actual.
     * @param event Evento de acción generado por el botón.
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Confirmar cancelación", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            cerrarVentana();
        }
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) tfAgregarHoras.getScene().getWindow();
        stage.close();
    }
}
