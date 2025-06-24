/**
 * Nombre del archivo: FXML_RegistrarOVController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 12/06/2025
 * Descripción: Controlador para la vista de registro de Organizaciones Vinculadas.
 * Permite capturar datos, validar y guardar una nueva organización vinculada.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sistemagestionpracticasprofesionales.interfaz.INotificacion;
import sistemagestionpracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

public class FXML_RegistrarOVController implements Initializable {

    @FXML private TextField tfNombreOV;
    @FXML private TextField tfCorreoOV;
    @FXML private TextField tfTelefonoOV;
    @FXML private TextField tfDireccionOV;

    @FXML private Label lbErrorNombre;
    @FXML private Label lbErrorCorreo;
    @FXML private Label lbErrorTelefono;
    @FXML private Label lbErrorDireccion;

    INotificacion observador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
        guardarOV();
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(lbErrorCorreo);
        }
    }

    private void guardarOV() {
        if (!validarCampos()) {
            return;
        }

        OrganizacionVinculada nuevaOV = new OrganizacionVinculada(
            0,
            tfNombreOV.getText(),
            tfTelefonoOV.getText(),
            tfDireccionOV.getText(),
            tfCorreoOV.getText()
        );

        try {
            ResultadoOperacion resultado = OrganizacionVinculadaDAO.registrarOV(nuevaOV);
            if (!resultado.isError()) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Organización Vinculada insertada", resultado.getMensaje());
                Utilidad.cerrarVentanaActual(tfNombreOV);
                if (observador != null) {
                    observador.operacionExitosa("Insertar", nuevaOV.getNombre());
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al registrar", resultado.getMensaje());
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
        }
    }

    private boolean validarCampos() {
        String nombre = tfNombreOV.getText().trim();
        String direccion = tfDireccionOV.getText().trim();
        String telefono = tfTelefonoOV.getText().trim();
        String correo = tfCorreoOV.getText().trim();

        lbErrorCorreo.setText("");
        lbErrorNombre.setText("");
        lbErrorTelefono.setText("");
        lbErrorDireccion.setText("");

        tfNombreOV.setStyle("");
        tfCorreoOV.setStyle("");
        tfTelefonoOV.setStyle("");
        tfDireccionOV.setStyle("");

        boolean camposValidos = true;
        boolean primerFoco = false;

        if (nombre.isEmpty()) {
            lbErrorNombre.setText("Nombre obligatorio");
            tfNombreOV.setStyle("-fx-border-color: red;");
            camposValidos = false;
            if (!primerFoco) {
                tfNombreOV.requestFocus();
                primerFoco = true;
            }
        } else if (nombre.length() > 100) {
            lbErrorNombre.setText("Máximo 100 caracteres");
            tfNombreOV.setStyle("-fx-border-color: red;");
            camposValidos = false;
            if (!primerFoco) {
                tfNombreOV.requestFocus();
                primerFoco = true;
            }
        } else if (nombre.length() < 2) {
            lbErrorNombre.setText("Mínimo 2 caracteres");
            tfNombreOV.setStyle("-fx-border-color: red;");
            camposValidos = false;
            if (!primerFoco) {
                tfNombreOV.requestFocus();
                primerFoco = true;
            }
        }

        if (direccion.isEmpty()) {
            lbErrorDireccion.setText("Dirección obligatoria");
            tfDireccionOV.setStyle("-fx-border-color: red;");
            camposValidos = false;
            if (!primerFoco) {
                tfDireccionOV.requestFocus();
                primerFoco = true;
            }
        } else if (direccion.length() < 15) {
            lbErrorDireccion.setText("Debe contener mínimo 15 caracteres");
            tfDireccionOV.setStyle("-fx-border-color: red;");
            camposValidos = false;
            if (!primerFoco) {
                tfDireccionOV.requestFocus();
                primerFoco = true;
            }
        } else if (direccion.length() > 150) {
            lbErrorDireccion.setText("Máximo 150 caracteres");
            tfDireccionOV.setStyle("-fx-border-color: red;");
            camposValidos = false;
            if (!primerFoco) {
                tfDireccionOV.requestFocus();
                primerFoco = true;
            }
        }

        if (telefono.isEmpty()) {
            lbErrorTelefono.setText("Teléfono obligatorio");
            tfTelefonoOV.setStyle("-fx-border-color: red;");
            camposValidos = false;
            if (!primerFoco) {
                tfTelefonoOV.requestFocus();
                primerFoco = true;
            }
        } else if (!telefono.matches("\\d{10}")) {
            lbErrorTelefono.setText("Debe contener 10 números");
            tfTelefonoOV.setStyle("-fx-border-color: red;");
            camposValidos = false;
            if (!primerFoco) {
                tfTelefonoOV.requestFocus();
                primerFoco = true;
            }
        } else {
            try {
                if (OrganizacionVinculadaDAO.existeOVConTelefono(telefono)) {
                    lbErrorTelefono.setText("Ya hay una OV con ese teléfono");
                    tfTelefonoOV.setStyle("-fx-border-color: red;");
                    camposValidos = false;
                    if (!primerFoco) {
                        tfTelefonoOV.requestFocus();
                        primerFoco = true;
                    }
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo verificar teléfono");
                camposValidos = false;
            }
        }

        if (correo.isEmpty()) {
            lbErrorCorreo.setText("Correo obligatorio");
            tfCorreoOV.setStyle("-fx-border-color: red;");
            camposValidos = false;
            if (!primerFoco) {
                tfCorreoOV.requestFocus();
                primerFoco = true;
            }
        } else if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            lbErrorCorreo.setText("Formato de correo inválido");
            tfCorreoOV.setStyle("-fx-border-color: red;");
            camposValidos = false;
            if (!primerFoco) {
                tfCorreoOV.requestFocus();
                primerFoco = true;
            }
        } else {
            try {
                if (OrganizacionVinculadaDAO.existeOVConCorreo(correo)) {
                    lbErrorCorreo.setText("Ya hay una OV con ese correo");
                    tfCorreoOV.setStyle("-fx-border-color: red;");
                    camposValidos = false;
                    if (!primerFoco) {
                        tfCorreoOV.requestFocus();
                        primerFoco = true;
                    }
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo verificar correo");
                camposValidos = false;
            }
        }

        return camposValidos;
    }
}