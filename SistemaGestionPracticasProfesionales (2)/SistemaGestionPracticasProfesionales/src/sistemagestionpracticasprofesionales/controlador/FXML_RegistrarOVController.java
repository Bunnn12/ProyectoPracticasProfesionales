/**
    * Nombre del archivo: FXML_RegistrarOVController.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 22/06/2025
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
import javafx.scene.control.TextFormatter;
import sistemagestionpracticasprofesionales.interfaz.INotificacion;
import sistemagestionpracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la vista de registro de Organizaciones Vinculadas.
 * Gestiona la captura, validación y almacenamiento de datos de una organización vinculada.
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
    private Label lbErrorCorreoDominio;
    @FXML
    private Label lbErrorTelefono;
    @FXML
    private Label lbErrorDireccion;
    
    INotificacion observador;
    
    /**
     * Inicializa el controlador.
     *
     * @param url URL de localización del archivo FXML.
     * @param rb ResourceBundle con recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfTelefonoOV.setTextFormatter(new TextFormatter<>(cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            if (nuevoTexto.matches("\\d{0,10}")) {
                return cambio;
            } else {
                return null;
            }
        }));
    }    

    /**
     * Acción para el botón aceptar: valida y guarda la organización vinculada.
     * 
     * @param event Evento generado por el botón aceptar.
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        guardarOV();
    }

    /**
     * Acción para el botón cancelar: pregunta confirmación y cierra ventana si se confirma.
     * 
     * @param event Evento generado por el botón cancelar.
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(lbErrorCorreo);
        } 
    }
    
    /**
     * Valida los campos de entrada y guarda la nueva organización vinculada en la base de datos.
     */
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
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Organización vinculada insertada", resultado.getMensaje());
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


     /**
     * Valida los campos del formulario mostrando mensajes de error si es necesario.
     * 
     * @return true si todos los campos son válidos; false en caso contrario.
     */
    private boolean validarCampos() {
        String nombre = tfNombreOV.getText().trim();
        String direccion = tfDireccionOV.getText().trim();
        String telefono = tfTelefonoOV.getText().trim();
        String correo = tfCorreoOV.getText().trim();

        lbErrorNombre.setText("");
        lbErrorCorreo.setText("");
        lbErrorCorreoDominio.setText("");
        lbErrorTelefono.setText("");
        lbErrorDireccion.setText("");

        boolean camposValidos = true;

        if (nombre.isEmpty()) {
            lbErrorNombre.setText("Nombre obligatorio");
            camposValidos = false;
        } else if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            lbErrorNombre.setText("El nombre solo puede contener letras y espacios");
            camposValidos = false;
        } else if (nombre.length() < 5) {
            lbErrorNombre.setText("Mínimo 6 caracteres");
            camposValidos = false;
        } else if (nombre.length() > 100) {
            lbErrorNombre.setText("Máximo 100 caracteres");
            camposValidos = false;
        } else {
            try {
                if (OrganizacionVinculadaDAO.existeOVConNombre(nombre)) {
                    lbErrorNombre.setText("Ya hay una organización vinculada registrada con ese nombre");
                    camposValidos = false;
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo verificar el nombre");
                camposValidos = false;
            }
        }

        if (correo.isEmpty()) {
            lbErrorCorreo.setText("Correo obligatorio");
            camposValidos = false;
        } else if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
            lbErrorCorreo.setText("Formato de correo inválido");
            lbErrorCorreoDominio.setText("Debe terminar en .com");
            camposValidos = false;
        } else {
            try {
                if (OrganizacionVinculadaDAO.existeOVConCorreo(correo)) {
                    lbErrorCorreo.setText("Ya hay una organización vinculada registrada con ese correo");
                    camposValidos = false;
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo verificar correo");
                camposValidos = false;
            }
        }
        
        if (telefono.isEmpty()) {
            lbErrorTelefono.setText("Teléfono obligatorio");
            camposValidos = false;
        } else if (telefono.length() != 10) {
            lbErrorTelefono.setText("Debe contener 10 dígitos");
            camposValidos = false;
        } else {
            try {
                if (OrganizacionVinculadaDAO.existeOVConTelefono(telefono)) {
                    lbErrorTelefono.setText("Ya hay una organización vinculada registrada con ese teléfono");
                    camposValidos = false;
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo verificar teléfono");
                camposValidos = false;
            }
        }

        if (direccion.isEmpty()) {
            lbErrorDireccion.setText("Dirección obligatoria");
            camposValidos = false;
        } else if (direccion.length() < 15) {
            lbErrorDireccion.setText("Mínimo 15 caracteres");
            camposValidos = false;
        } else if (direccion.length() > 150) {
            lbErrorDireccion.setText("Máximo 150 caracteres");
            camposValidos = false;
        }
        
        return camposValidos;
    }

}
