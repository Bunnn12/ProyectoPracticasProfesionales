package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sistemagestionpracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

public class FXML_ActualizarResponsableController implements Initializable {

    @FXML
    private TextField tfCorreoResponsable;
    @FXML
    private TextField tfTelefonoResponsable;
    @FXML
    private Label Nombre;
    @FXML
    private Label lbErrorNombre;
    @FXML
    private Label lbErrorCorreo;
    @FXML
    private Label lbErrorTelefono;
    @FXML
    private Label lbErrorOV;
    @FXML
    private ComboBox<OrganizacionVinculada> cbOrganizacionVinculada;

    private ResponsableProyecto responsable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void inicializarCampos(ResponsableProyecto responsable) {
        this.responsable = responsable;
        Nombre.setText(responsable.getNombre());
        tfCorreoResponsable.setText(responsable.getCorreo());
        tfTelefonoResponsable.setText(responsable.getTelefono());
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
        lbErrorCorreo.setText("");
        lbErrorTelefono.setText("");
        lbErrorOV.setText("");

        String nuevoCorreo = tfCorreoResponsable.getText().trim();
        String nuevoTelefono = tfTelefonoResponsable.getText().trim();
        OrganizacionVinculada seleccionada = cbOrganizacionVinculada.getValue();

        boolean esValido = true;

        if (!nuevoCorreo.isEmpty() && !esCorreoValido(nuevoCorreo)) {
            lbErrorCorreo.setText("Correo no válido");
            esValido = false;
        }

        if (!nuevoTelefono.isEmpty() && !nuevoTelefono.matches("\\d{1,10}")) {
            lbErrorTelefono.setText("Teléfono inválido. Solo 1-10 dígitos");
            esValido = false;
        }

        if (!esValido) return;

        // Solo se actualizan los campos modificados
        if (!nuevoCorreo.isEmpty()) {
            responsable.setCorreo(nuevoCorreo);
        }
        if (!nuevoTelefono.isEmpty()) {
            responsable.setTelefono(nuevoTelefono);
        }

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
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                    "Error de BD", "No se pudo actualizar el responsable");
            e.printStackTrace();
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmar = Utilidad.mostrarAlertaConfirmacion("Cancelar", "¿Estás seguro de cancelar la modificación?");
        if (confirmar) {
            Utilidad.cerrarVentanaActual(tfCorreoResponsable);
        }
    }

    private boolean esCorreoValido(String correo) {
        String patronCorreo = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$";
        return Pattern.matches(patronCorreo, correo);
    }
}