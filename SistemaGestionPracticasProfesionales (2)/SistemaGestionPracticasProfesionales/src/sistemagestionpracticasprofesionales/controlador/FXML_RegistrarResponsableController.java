package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
<<<<<<< HEAD
=======
import java.sql.SQLException;
import java.util.ArrayList;
>>>>>>> cea35e77b829d9c025609ea2d6d4cf4d12a5e560
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sistemagestionpracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
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
    private ComboBox<OrganizacionVinculada> cbOrganizacionVinculada;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarOrganizaciones();
    }

    private void cargarOrganizaciones() {
    try {
        ArrayList<OrganizacionVinculada> lista = OrganizacionVinculadaDAO.obtenerOrganizacionesVinculadas();
        ObservableList<OrganizacionVinculada> observableList = FXCollections.observableArrayList(lista);
        cbOrganizacionVinculada.setItems(observableList);
        System.out.println("Total organizaciones cargadas: " + lista.size()); // Puedes quitarlo después
    } catch (Exception e) {
        e.printStackTrace(); // ← Esto imprimirá el error exacto en consola
        Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
            "Error", "No se pudieron cargar las organizaciones.\nConsulta la consola para más detalles.");
    }
}

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

        ResponsableProyecto responsable = new ResponsableProyecto();
        responsable.setNombre(nombre);
        responsable.setCorreo(correo);
        responsable.setTelefono(telefono);
        responsable.setIdOrganizacionVinculada(organizacion.getIdOrganizacionVinculada());

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

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfNombreResponsable);
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfNombreResponsable);
    }

    private boolean validarCampos() {
        String nombre = tfNombreResponsable.getText();
        String telefono = tfTelefonoResponsable.getText();
        String correo = tfCorreoResponsable.getText();
        lbErrorCorreo.setText("");
        lbErrorNombre.setText("");
        lbErrorTelefono.setText("");
        boolean camposValidos = true;
        if (nombre.isEmpty()) {
            lbErrorNombre.setText("Nombre obligatorio");
            camposValidos = false;
        }
        if (telefono.isEmpty()) {
            lbErrorTelefono.setText("Teléfono obligatorio");
            camposValidos = false;
        }
        if (correo.isEmpty()) {
            lbErrorCorreo.setText("Correo obligatorio");
            camposValidos = false;
        }
        return camposValidos;
    }
}