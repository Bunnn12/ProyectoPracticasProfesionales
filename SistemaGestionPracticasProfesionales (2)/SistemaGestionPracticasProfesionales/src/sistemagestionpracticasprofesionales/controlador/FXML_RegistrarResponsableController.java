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

    /**
     * Método llamado al inicializar el controlador.
     * Se encarga de cargar las organizaciones vinculadas para mostrar en el ComboBox.
     * 
     * @param url URL de localización (no utilizado).
     * @param rb  Recursos internacionalizados (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarOrganizaciones();
    }

    /**
     * Carga las organizaciones vinculadas desde la base de datos
     * y las establece como ítems en el ComboBox.
     * Muestra un mensaje de error si la carga falla.
     */
    private void cargarOrganizaciones() {
        try {
            ArrayList<OrganizacionVinculada> lista = OrganizacionVinculadaDAO.obtenerOrganizacionesVinculadas();
            ObservableList<OrganizacionVinculada> observableList = FXCollections.observableArrayList(lista);
            cbOrganizacionVinculada.setItems(observableList);
            System.out.println("Total organizaciones cargadas: " + lista.size()); 
        } catch (Exception e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                    "Error", "No se pudieron cargar las organizaciones.\nConsulta la consola para más detalles.");
        }
    }

    /**
     * Evento disparado al dar clic en el botón aceptar para registrar el responsable.
     * Valida los campos, crea el objeto responsable y lo registra mediante el DAO.
     * Muestra alertas con el resultado de la operación.
     * 
     * @param event Evento de acción.
     * @throws SQLException Si hay error en la consulta a la base de datos.
     */
    @FXML
    private void clickAceptar(ActionEvent event) throws SQLException {
        if (!validarCampos()) {
            return;
        }

        String nombre = tfNombreResponsable.getText().trim();
        String correo = tfCorreoResponsable.getText().trim();
        String telefono = tfTelefonoResponsable.getText().trim();
        OrganizacionVinculada organizacion = cbOrganizacionVinculada.getValue();

        if (organizacion == null) {
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                    "Error", "Debe seleccionar una organización vinculada");
            return;
        }

        // Construcción del objeto responsable con los datos capturados
        ResponsableProyecto responsable = new ResponsableProyecto();
        responsable.setNombre(nombre);
        responsable.setCorreo(correo);
        responsable.setTelefono(telefono);
        responsable.setIdOrganizacionVinculada(organizacion.getIdOrganizacionVinculada());

        // Registro en base de datos
        ResultadoOperacion resultado = ResponsableProyectoDAO.registrarResponsableProyecto(responsable);

        if (!resultado.isError()) {
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.INFORMATION,
                    "Éxito", "Responsable registrado correctamente");
            Utilidad.cerrarVentanaActual(tfNombreResponsable);
        } else {
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                    "Error", resultado.getMensaje());
        }
    }

    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfNombreResponsable);
    }

    /**
     * Evento disparado al dar clic en el botón cancelar.
     * Cierra la ventana actual sin guardar cambios.
     * 
     * @param event Evento de acción.
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfNombreResponsable);
    }

    /**
    * Valida los campos de texto para nombre, correo, teléfono y la selección de una organización vinculada.
    * Muestra mensajes de error tanto en etiquetas como en alertas.
    *
    * @return true si todos los campos son válidos; false en caso contrario.
    */
   private boolean validarCampos() {
    String nombre = tfNombreResponsable.getText().trim();
    String telefono = tfTelefonoResponsable.getText().trim();
    String correo = tfCorreoResponsable.getText().trim();
    OrganizacionVinculada organizacion = cbOrganizacionVinculada.getValue();

    boolean esValido = true;

    lbErrorNombre.setText("");
    lbErrorTelefono.setText("");
    lbErrorCorreo.setText("");
    lbErrorOV.setText("");

    if (nombre.isEmpty()) {
        lbErrorNombre.setText("Nombre obligatorio");
        esValido = false;
    }

    if (telefono.isEmpty()) {
        lbErrorTelefono.setText("Teléfono obligatorio");
        esValido = false;
    } else if (!telefono.matches("\\d{1,10}")) {
        lbErrorTelefono.setText("Teléfono inválido, solo 10 .");
        esValido = false;
    }

    if (correo.isEmpty()) {
        lbErrorCorreo.setText("Correo obligatorio");
        esValido = false;
    } else if (!esCorreoValido(correo)) {
        lbErrorCorreo.setText("Correo no válido");
        esValido = false;
    }

    if (organizacion == null) {
        lbErrorOV.setText("Debe seleccionar una organización");
        esValido = false;
    }

    return esValido;
}



    /**
     * Verifica si el formato del correo electrónico es válido.
     * 
     * @param correo Cadena con el correo a validar.
     * @return true si el correo cumple con el patrón, false si no.
     */
    private boolean esCorreoValido(String correo) {
        String patronCorreo = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$";
        return Pattern.matches(patronCorreo, correo);
    }
}
