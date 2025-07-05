/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.interfaz.INotificacion;
import sistemagestionpracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author rodri
 */
public class FXML_HorarioProyectoController implements Initializable {

    @FXML
    private TextField tfEntradaLunes;
    @FXML
    private TextField tfSalidaLunes;
    @FXML
    private Label lbErrorEntradaLunes;
    @FXML
    private Label lbErrorSalidaLunes;
    @FXML
    private Label lbErrorSalidaMartes;
    @FXML
    private Label lbErrorEntradaMartes;
    @FXML
    private TextField tfSalidaMartes;
    @FXML
    private TextField tfEntradaMartes;
    @FXML
    private TextField tfEntradaMiercoles;
    @FXML
    private TextField tfSalidaMiercoles;
    @FXML
    private Label lbErrorEntradaMiercoles;
    @FXML
    private Label lbErrorSalidaMiercoles;
    @FXML
    private Label lbErrorSalidaJueves;
    @FXML
    private Label lbErrorEntradaJueves;
    @FXML
    private TextField tfSalidaJueves;
    @FXML
    private TextField tfEntradaJueves;
    @FXML
    private TextField tfEntradaViernes;
    @FXML
    private TextField tfSalidaViernes;
    @FXML
    private Label lbErrorEntradaViernes;
    @FXML
    private Label lbErrorSalidaViernes;

    INotificacion observador;
    private Stage ventanaRegistrarProyecto;
    Proyecto nuevoProyecto;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfEntradaLunes.setTextFormatter(filtroHora());
        tfSalidaLunes.setTextFormatter(filtroHora());
        tfEntradaMartes.setTextFormatter(filtroHora());
        tfSalidaMartes.setTextFormatter(filtroHora());
        tfEntradaMiercoles.setTextFormatter(filtroHora());
        tfSalidaMiercoles.setTextFormatter(filtroHora());
        tfEntradaJueves.setTextFormatter(filtroHora());
        tfSalidaJueves.setTextFormatter(filtroHora());
        tfEntradaViernes.setTextFormatter(filtroHora());
        tfSalidaViernes.setTextFormatter(filtroHora());
    }
    
    public void inicializarDatos(Proyecto proyecto, Stage ventanaRegistrar) {
        this.nuevoProyecto = proyecto;
        this.ventanaRegistrarProyecto = ventanaRegistrar;
    }

    /**
     * Aplica una restricción de formato para permitir solo horas válidas
     * @return 
     */
    private TextFormatter<String> filtroHora() {
        return new TextFormatter<>(cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            
            if (nuevoTexto.matches("[0-9:]*") && nuevoTexto.length() <= 5) {
                return cambio;
            } else {
                return null;
            }
        });
    }
    
    @FXML
    private void clickAceptar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }
        try {
            ResultadoOperacion resultado;
            resultado = ProyectoDAO.registrarHorario(nuevoProyecto, "Lunes", tfEntradaLunes.getText(), tfSalidaLunes.getText());
            resultado = ProyectoDAO.registrarHorario(nuevoProyecto, "Martes", tfEntradaMartes.getText(), tfSalidaMartes.getText());
            resultado = ProyectoDAO.registrarHorario(nuevoProyecto, "Miercoles", tfEntradaMiercoles.getText(), tfSalidaMiercoles.getText());
            resultado = ProyectoDAO.registrarHorario(nuevoProyecto, "Jueves", tfEntradaJueves.getText(), tfSalidaJueves.getText());
            resultado = ProyectoDAO.registrarHorario(nuevoProyecto, "Viernes", tfEntradaViernes.getText(), tfSalidaViernes.getText());
            
            if (!resultado.isError()) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Proyecto insertado", resultado.getMensaje());
                // Cierra ambas ventanas
                Stage ventanaHorario = (Stage) tfEntradaLunes.getScene().getWindow();
                ventanaHorario.close(); // Cierra esta ventana
                if (ventanaRegistrarProyecto != null) {
                    ventanaRegistrarProyecto.close(); // Cierra la original
                }
                if (observador != null) {
                    observador.operacionExitosa("Insertar", nuevoProyecto.getNombre());
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al registrar", resultado.getMensaje());
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexión", "No se pudo guardar el proyecto: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        String entradaLunes = tfEntradaLunes.getText();
        String salidaLunes = tfSalidaLunes.getText();
        String entradaMartes = tfEntradaMartes.getText();
        String salidaMartes = tfSalidaMartes.getText();
        String entradaMiercoles = tfEntradaMiercoles.getText();
        String salidaMiercoles = tfSalidaMiercoles.getText();
        String entradaJueves = tfEntradaJueves.getText();
        String salidaJueves = tfSalidaJueves.getText();
        String entradaViernes = tfEntradaViernes.getText();
        String salidaViernes = tfSalidaViernes.getText();
        
        lbErrorEntradaLunes.setText("");
        lbErrorSalidaLunes.setText("");
        lbErrorEntradaMartes.setText("");
        lbErrorSalidaMartes.setText("");
        lbErrorEntradaMiercoles.setText("");
        lbErrorSalidaMiercoles.setText("");
        lbErrorEntradaJueves.setText("");
        lbErrorSalidaJueves.setText("");
        lbErrorEntradaViernes.setText("");
        lbErrorSalidaViernes.setText("");

        boolean camposValidos = true;

        String patronHora = "(0\\d|1\\d|2[0-3]):[0-5]\\d";
        
        // === LUNES ===
        if (entradaLunes.isEmpty()) {
            lbErrorEntradaLunes.setText("Hora obligatoria");
            camposValidos = false;
        } else if (!entradaLunes.matches(patronHora)) {
            lbErrorEntradaLunes.setText("Formato inválido (HH:MM)");
            camposValidos = false;
        }
        
        if (salidaLunes.isEmpty()) {
            lbErrorSalidaLunes.setText("Hora obligatoria");
            camposValidos = false;
        } else if (!salidaLunes.matches(patronHora)) {
            lbErrorSalidaLunes.setText("Formato inválido (HH:MM)");
            camposValidos = false;
        }
        
        if (entradaLunes.matches(patronHora) && salidaLunes.matches(patronHora)) {
            try {
                LocalTime entrada = LocalTime.parse(entradaLunes);
                LocalTime salida = LocalTime.parse(salidaLunes);
                if (!salida.isAfter(entrada)) {
                    lbErrorSalidaLunes.setText("Hora de salida debe ser posterior a hora de entrada");
                    camposValidos = false;
                }
            } catch (DateTimeParseException e) {
                lbErrorEntradaLunes.setText("Formato de hora inválidoo");
                lbErrorSalidaLunes.setText("Formato de hora inválidoo");
                camposValidos = false;
            }
        }
        
        // === MARTES ===
        if (entradaMartes.isEmpty()) {
            lbErrorEntradaMartes.setText("Hora obligatoria");
            camposValidos = false;
        } else if (!entradaMartes.matches(patronHora)) {
            lbErrorEntradaMartes.setText("Formato inválido (HH:MM)");
            camposValidos = false;
        }

        if (salidaMartes.isEmpty()) {
            lbErrorSalidaMartes.setText("Hora obligatoria");
            camposValidos = false;
        } else if (!salidaMartes.matches(patronHora)) {
            lbErrorSalidaMartes.setText("Formato inválido (HH:MM)");
            camposValidos = false;
        }

        if (entradaMartes.matches(patronHora) && salidaMartes.matches(patronHora)) {
            try {
                LocalTime entrada = LocalTime.parse(entradaMartes);
                LocalTime salida = LocalTime.parse(salidaMartes);
                if (!salida.isAfter(entrada)) {
                    lbErrorSalidaMartes.setText("Hora de salida debe ser posterior a hora de entrada");
                    camposValidos = false;
                }
            } catch (DateTimeParseException e) {
                lbErrorEntradaMartes.setText("Formato de hora inválido");
                lbErrorSalidaMartes.setText("Formato de hora inválido");
                camposValidos = false;
            }
        }

        // === MIÉRCOLES ===
        if (entradaMiercoles.isEmpty()) {
            lbErrorEntradaMiercoles.setText("Hora obligatoria");
            camposValidos = false;
        } else if (!entradaMiercoles.matches(patronHora)) {
            lbErrorEntradaMiercoles.setText("Formato inválido (HH:MM)");
            camposValidos = false;
        }

        if (salidaMiercoles.isEmpty()) {
            lbErrorSalidaMiercoles.setText("Hora obligatoria");
            camposValidos = false;
        } else if (!salidaMiercoles.matches(patronHora)) {
            lbErrorSalidaMiercoles.setText("Formato inválido (HH:MM)");
            camposValidos = false;
        }

        if (entradaMiercoles.matches(patronHora) && salidaMiercoles.matches(patronHora)) {
            try {
                LocalTime entrada = LocalTime.parse(entradaMiercoles);
                LocalTime salida = LocalTime.parse(salidaMiercoles);
                if (!salida.isAfter(entrada)) {
                    lbErrorSalidaMiercoles.setText("Hora de salida debe ser posterior a hora de entrada");
                    camposValidos = false;
                }
            } catch (DateTimeParseException e) {
                lbErrorEntradaMiercoles.setText("Formato de hora inválido");
                lbErrorSalidaMiercoles.setText("Formato de hora inválido");
                camposValidos = false;
            }
        }

        // === JUEVES ===
        if (entradaJueves.isEmpty()) {
            lbErrorEntradaJueves.setText("Hora obligatoria");
            camposValidos = false;
        } else if (!entradaJueves.matches(patronHora)) {
            lbErrorEntradaJueves.setText("Formato inválido (HH:MM)");
            camposValidos = false;
        }

        if (salidaJueves.isEmpty()) {
            lbErrorSalidaJueves.setText("Hora obligatoria");
            camposValidos = false;
        } else if (!salidaJueves.matches(patronHora)) {
            lbErrorSalidaJueves.setText("Formato inválido (HH:MM)");
            camposValidos = false;
        }

        if (entradaJueves.matches(patronHora) && salidaJueves.matches(patronHora)) {
            try {
                LocalTime entrada = LocalTime.parse(entradaJueves);
                LocalTime salida = LocalTime.parse(salidaJueves);
                if (!salida.isAfter(entrada)) {
                    lbErrorSalidaJueves.setText("Hora de salida debe ser posterior a hora de entrada");
                    camposValidos = false;
                }
            } catch (DateTimeParseException e) {
                lbErrorEntradaJueves.setText("Formato de hora inválido");
                lbErrorSalidaJueves.setText("Formato de hora inválido");
                camposValidos = false;
            }
        }

        // === VIERNES ===
        if (entradaViernes.isEmpty()) {
            lbErrorEntradaViernes.setText("Hora obligatoria");
            camposValidos = false;
        } else if (!entradaViernes.matches(patronHora)) {
            lbErrorEntradaViernes.setText("Formato inválido (HH:MM)");
            camposValidos = false;
        }

        if (salidaViernes.isEmpty()) {
            lbErrorSalidaViernes.setText("Hora obligatoria");
            camposValidos = false;
        } else if (!salidaViernes.matches(patronHora)) {
            lbErrorSalidaViernes.setText("Formato inválido (HH:MM)");
            camposValidos = false;
        }

        if (entradaViernes.matches(patronHora) && salidaViernes.matches(patronHora)) {
            try {
                LocalTime entrada = LocalTime.parse(entradaViernes);
                LocalTime salida = LocalTime.parse(salidaViernes);
                if (!salida.isAfter(entrada)) {
                    lbErrorSalidaViernes.setText("Hora de salida debe ser posterior a hora de entrada");
                    camposValidos = false;
                }
            } catch (DateTimeParseException e) {
                lbErrorEntradaViernes.setText("Formato de hora inválido");
                lbErrorSalidaViernes.setText("Formato de hora inválido");
                camposValidos = false;
            }
        }
        
        return camposValidos;
    }
    
    /**
     * Cierra la ventana si el usuario lo confirma
     * @param event 
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            try {
                ResultadoOperacion resultado = ProyectoDAO.eliminarProyecto(nuevoProyecto.getIdProyecto());

                if (!resultado.isError()) {
                    // Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Cancelado", "Proyecto cancelado");
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Atención", resultado.getMensaje());
                }

                // Cerrar solo esta ventana
                Stage ventanaActual = (Stage) tfEntradaLunes.getScene().getWindow();
                ventanaActual.close();

            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el proyecto: " + e.getMessage());
            }
        }
    }
    
}
