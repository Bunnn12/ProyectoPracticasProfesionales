package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sistemagestionpracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

public class FXML_ActualizarResponsableController implements Initializable {

    @FXML private TextField tfCorreoResponsable;
    @FXML private TextField tfTelefonoResponsable;
    @FXML private Label Nombre;
    @FXML private Label lbErrorNombre;
    @FXML private Label lbErrorCorreo;
    @FXML private Label lbErrorTelefono;

    private ResponsableProyecto responsable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No requiere carga inicial
    }

    public void inicializarCampos(ResponsableProyecto responsable) {
        this.responsable = responsable;
        Nombre.setText(responsable.getNombre());
        tfCorreoResponsable.setText(responsable.getCorreo());
        tfTelefonoResponsable.setText(responsable.getTelefono());
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
        if (!validarCampos()) return;

        String nuevoCorreo = tfCorreoResponsable.getText().trim();
        String nuevoTelefono = tfTelefonoResponsable.getText().trim();

        if (nuevoCorreo.equals(responsable.getCorreo()) &&
            nuevoTelefono.equals(responsable.getTelefono())) {
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.WARNING,
                    "Sin cambios", "No has realizado ningún cambio.");
            return;
        }

        responsable.setCorreo(nuevoCorreo);
        responsable.setTelefono(nuevoTelefono);

        try {
            ResultadoOperacion resultado = ResponsableProyectoDAO.actualizarResponsableProyecto(responsable);
            if (!resultado.isError()) {
                Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.INFORMATION,
                        "Actualización exitosa", "Responsable actualizado correctamente");
                Utilidad.cerrarVentanaActual(tfCorreoResponsable);
            } else {
                Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                        "Error", resultado.getMensaje());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                    "Error de BD", "No se pudo actualizar el responsable.");
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmar = Utilidad.mostrarAlertaConfirmacion("Cancelar", "¿Estás seguro de cancelar la modificación?");
        if (confirmar) {
            Utilidad.cerrarVentanaActual(tfCorreoResponsable);
        }
    }

    private boolean validarCampos() {
        String correo = tfCorreoResponsable.getText().trim();
        String telefono = tfTelefonoResponsable.getText().trim();

        lbErrorCorreo.setText("");
        lbErrorTelefono.setText("");

        lbErrorCorreo.setStyle("-fx-text-fill: red;");
        lbErrorTelefono.setStyle("-fx-text-fill: red;");

        boolean esValido = true;

        // Validación de correo
        if (correo.isEmpty()) {
            lbErrorCorreo.setText("Correo obligatorio");
            esValido = false;
        } else if (!esCorreoValido(correo)) {
            lbErrorCorreo.setText("Correo no válido");
            esValido = false;
        } else if (!correo.equals(responsable.getCorreo())) {
            try {
                if (ResponsableProyectoDAO.existeResponsablePorCorreo(correo)) {
                    lbErrorCorreo.setText("Este correo ya está registrado");
                    esValido = false;
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                        "Error", "No se pudo verificar correo duplicado");
                esValido = false;
            }
        }

        // Validación de teléfono
        if (telefono.isEmpty()) {
            lbErrorTelefono.setText("Teléfono obligatorio");
            esValido = false;
        } else if (!telefono.matches("\\d{10}")) {
            lbErrorTelefono.setText("Teléfono inválido, deben ser 10 dígitos");
            esValido = false;
        } else if (!telefono.equals(responsable.getTelefono())) {
            try {
                if (ResponsableProyectoDAO.existeResponsablePorTelefono(telefono)) {
                    lbErrorTelefono.setText("Este teléfono ya está registrado");
                    esValido = false;
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                        "Error", "No se pudo verificar teléfono duplicado");
                esValido = false;
            }
        }

        return esValido;
    }

    private boolean esCorreoValido(String correo) {
        String patronCorreo = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$";
        return Pattern.matches(patronCorreo, correo);
    }
}
