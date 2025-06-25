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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

public class FXML_BusquedaResponsableController implements Initializable {

    @FXML
    private TableColumn<ResponsableProyecto, String> colResponsable;
    @FXML
    private TableColumn<ResponsableProyecto, String> colTelefono;
    @FXML
    private TableColumn<ResponsableProyecto, String> colCorreo;
    @FXML
    private TableColumn<ResponsableProyecto, String> colOV;
    @FXML
    private TextField tfBusquedaResponsable;
    @FXML
    private TableView<ResponsableProyecto> tvResponsables;

    private ObservableList<ResponsableProyecto> responsablesOriginal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarResponsables();
    }

    private void configurarColumnas() {
        colResponsable.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colOV.setCellValueFactory(new PropertyValueFactory<>("nombreOrganizacionVinculada"));
    }

    private void cargarResponsables() {
        try {
            ArrayList<ResponsableProyecto> lista = ResponsableProyectoDAO.obtenerResponsablesSinProyecto();
            responsablesOriginal = FXCollections.observableArrayList(lista);
            tvResponsables.setItems(responsablesOriginal);
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                    "Error", "No se pudieron cargar los responsables sin proyecto");
        }
    }

    @FXML
    private void buscarResponsablePorNombre(KeyEvent event) {
        String filtro = tfBusquedaResponsable.getText().toLowerCase();
        ObservableList<ResponsableProyecto> filtrados = FXCollections.observableArrayList();
        for (ResponsableProyecto r : responsablesOriginal) {
            if (r.getNombre().toLowerCase().contains(filtro)) {
                filtrados.add(r);
            }
        }
        tvResponsables.setItems(filtrados);
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfBusquedaResponsable);
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
        ResponsableProyecto seleccionado = tvResponsables.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.WARNING,
                    "Seleccionar", "Debes seleccionar un responsable para modificar");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_ActualizarResponsable.fxml"));
            Parent root = loader.load();

            FXML_ActualizarResponsableController controlador = loader.getController();
            controlador.inicializarCampos(seleccionado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Actualizar Responsable");
            stage.show();

            Utilidad.cerrarVentanaActual(tfBusquedaResponsable);

        } catch (Exception e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                    "Error", "No se pudo abrir la ventana de actualización");
        }
    }
}
