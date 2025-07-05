/**
 * Nombre del archivo: FXML_RegistrarResponsableController.java
 * Autor: Juan Pablo Silva Miranda
 * Fecha: 17/06/2025
 * Descripción: Controlador JavaFX encargado de registrar responsables de proyecto
 * para prácticas profesionales. Permite capturar datos, validar y guardar la información
 * en la base de datos, además de gestionar la carga de organizaciones vinculadas.
 */
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
import javafx.scene.control.Alert;
import sistemagestionpracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

public class FXML_RegistrarResponsableController implements Initializable {

    @FXML
    private TextField tfNombreResponsable;
    @FXML
    private TextField tfCorreoResponsable;
    @FXML
    private TextField tfTelefonoResponsable;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarOrganizaciones();
    }

    private void cargarOrganizaciones() {
        try {
            ArrayList<OrganizacionVinculada> lista = OrganizacionVinculadaDAO.obtenerOrganizacionesVinculadas();
            ObservableList<OrganizacionVinculada> observableList = FXCollections.observableArrayList(lista);
            cbOrganizacionVinculada.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                    "Error", "No se pudieron cargar las organizaciones.");
        }
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        String nombre = tfNombreResponsable.getText().trim();
        String correo = tfCorreoResponsable.getText().trim();
        String telefono = tfTelefonoResponsable.getText().trim();
        OrganizacionVinculada organizacion = cbOrganizacionVinculada.getValue();

        ResponsableProyecto responsable = new ResponsableProyecto();
        responsable.setNombre(nombre);
        responsable.setCorreo(correo);
        responsable.setTelefono(telefono);
        responsable.setIdOrganizacionVinculada(organizacion.getIdOrganizacionVinculada());

        try {
            ResultadoOperacion resultado = ResponsableProyectoDAO.registrarResponsableProyecto(responsable);
            if (!resultado.isError()) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION,
                        "Éxito", "Responsable registrado correctamente");
                Utilidad.cerrarVentanaActual(tfNombreResponsable);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                        "Error", resultado.getMensaje());
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                    "Error de conexión", "No fue posible registrar el responsable.");
            e.printStackTrace();
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfNombreResponsable);
    }

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfNombreResponsable);
    }

    private boolean validarCampos() {
    String nombre = tfNombreResponsable.getText().trim();
    String correo = tfCorreoResponsable.getText().trim();
    String telefono = tfTelefonoResponsable.getText().trim();
    OrganizacionVinculada organizacion = cbOrganizacionVinculada.getValue();

    boolean esValido = true;

    lbErrorNombre.setText("");
    lbErrorCorreo.setText("");
    lbErrorTelefono.setText("");
    lbErrorOV.setText("");

    // Validar nombre
    if (nombre.isEmpty()) {
        lbErrorNombre.setText("Nombre obligatorio");
        esValido = false;
    } else if (nombre.contains("@")) {
        lbErrorNombre.setText("El nombre no debe contener '@'");
        esValido = false;
    }

    // Validar correo
    if (correo.isEmpty()) {
        lbErrorCorreo.setText("Correo obligatorio");
        esValido = false;
    } else if (!esCorreoValido(correo)) {
        lbErrorCorreo.setText("Correo no válido");
        esValido = false;
    } else {
        try {
            if (ResponsableProyectoDAO.existeResponsablePorCorreo(correo)) {
                lbErrorCorreo.setText("Este correo ya está registrado");
                esValido = false;
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al verificar correo duplicado.");
            esValido = false;
        }
    }

    // Validar teléfono
    if (telefono.isEmpty()) {
        lbErrorTelefono.setText("Teléfono obligatorio");
        esValido = false;
    } else if (!telefono.matches("\\d{10}")) {
        lbErrorTelefono.setText("Teléfono inválido, deben ser 10 dígitos");
        esValido = false;
    } else {
        try {
            if (ResponsableProyectoDAO.existeResponsablePorTelefono(telefono)) {
                lbErrorTelefono.setText("Este teléfono ya está registrado");
                esValido = false;
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al verificar teléfono duplicado.");
            esValido = false;
        }
    }

    // Validar organización
    if (organizacion == null) {
        lbErrorOV.setText("Debe seleccionar una organización");
        esValido = false;
    }

    return esValido;
}

    private boolean esCorreoValido(String correo) {
        String patronCorreo = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$";
        return Pattern.matches(patronCorreo, correo);
    }
}
