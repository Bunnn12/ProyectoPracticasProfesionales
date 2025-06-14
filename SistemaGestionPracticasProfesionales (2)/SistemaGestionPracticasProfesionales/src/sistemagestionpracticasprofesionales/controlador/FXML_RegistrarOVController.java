/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
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

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_RegistrarOVController implements Initializable {

    @FXML
    private TextField tfNombreOV;
    @FXML
    private TextField tfCorreoOV;
    @FXML
    private TextField tfTelefonoOV;
    @FXML
    private TextField tfDireccionOV;
    @FXML
    private Label lbErrorNombre;
    @FXML
    private Label lbErrorCorreo;
    @FXML
    private Label lbErrorTelefono;
    @FXML
    private Label lbErrorDireccion;
    INotificacion observador;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfNombreOV);
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
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexión", "No se pudo guardar la organización vinculada: " + e.getMessage());
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

    boolean camposValidos = true;

    if (nombre.isEmpty()) {
        lbErrorNombre.setText("Nombre obligatorio");
        camposValidos = false;
    }

    if (direccion.isEmpty()) {
        lbErrorDireccion.setText("Dirección obligatoria");
        camposValidos = false;
    }

    if (telefono.isEmpty()) {
        lbErrorTelefono.setText("Teléfono obligatorio");
        camposValidos = false;
    } else if (!telefono.matches("\\d{10}")) { // solo 10 dígitos numéricos para el cel
        lbErrorTelefono.setText("Debe contener 10 números");
        camposValidos = false;
    }

    if (correo.isEmpty()) {
        lbErrorCorreo.setText("Correo obligatorio");
        camposValidos = false;
    } else if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
        lbErrorCorreo.setText("Formato de correo inválido");
        camposValidos = false;
    }

    return camposValidos;
}

}
