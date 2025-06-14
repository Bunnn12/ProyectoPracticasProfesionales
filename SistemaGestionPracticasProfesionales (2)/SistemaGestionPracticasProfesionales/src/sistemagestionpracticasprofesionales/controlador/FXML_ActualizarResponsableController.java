/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import sistemagestionpracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_ActualizarResponsableController implements Initializable {

    @FXML
    private Label lbProyectoSeleccionado;
    @FXML
    private Label lbResponsableActual;
    @FXML
    private TextField tfBuscarResponsable;
    @FXML
    private TableView<ResponsableProyecto> tvResponsableProyecto;
    @FXML
    private TableColumn colNombreResponsable;
    @FXML
    private TableColumn colCorreoResponsable;
    @FXML
    private TableColumn colTelefonoResponsable;
    ObservableList<ResponsableProyecto> responsablesDisponibles;
    private Proyecto proyecto;
    @FXML
    private TableColumn colOrganizacionVinculada;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaResponsables();
        cargarResponsablesProyecto();
    }    
    
        public void inicializarProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        lbProyectoSeleccionado.setText(proyecto.getNombre()); 
        lbResponsableActual.setText(proyecto.getNombreResponsable()); 
    }
        
        private void cargarResponsablesProyecto(){
        try {
           responsablesDisponibles = FXCollections.observableArrayList();
           List<ResponsableProyecto> responsablesDisponiblesDAO = ResponsableProyectoDAO.obtenerResponsablesSinProyecto(); 
           responsablesDisponibles.addAll(responsablesDisponiblesDAO);
           tvResponsableProyecto.setItems(responsablesDisponibles);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de los responsables de proyecto, por favor intentélo más tarde");
            Utilidad.cerrarVentanaActual(tvResponsableProyecto);
        }
    }
    
    private void configurarTablaResponsables(){
        colNombreResponsable.setCellValueFactory(new PropertyValueFactory("nombre"));
        colCorreoResponsable.setCellValueFactory(new PropertyValueFactory("correo"));
        colOrganizacionVinculada.setCellValueFactory(new PropertyValueFactory("nombreOrganizacionVinculada"));
        colTelefonoResponsable.setCellValueFactory(new PropertyValueFactory("telefono"));
    }
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfBuscarResponsable);
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
        ResponsableProyecto nuevoResponsable = tvResponsableProyecto.getSelectionModel().getSelectedItem();

        if (nuevoResponsable == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "SinSeleccion", "Debes seleccionar un nuevo responsable para continuar");
            return;
        }

        try {
            boolean actualizado = ProyectoDAO.actualizarResponsableDeProyecto(proyecto.getIdProyecto(), nuevoResponsable.getIdResponsable());
            if (actualizado) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "ActualizacionExitosa", "El responsable del proyecto ha sido actualizado exitosamente");
                Utilidad.cerrarVentanaActual(tfBuscarResponsable);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al actualizar", "No se pudo actualizar el responsable, Intenta más tarde");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexión", "No fue posible actualizar el responsable debido a un error en la base de datos.");
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
        Utilidad.cerrarVentanaActual(tfBuscarResponsable);
        } 
    }


    @FXML
    private void buscarResponsablePorNombre(KeyEvent event) {
        String responsableBuscado = tfBuscarResponsable.getText().toLowerCase();

        if (responsableBuscado.isEmpty()) {
            tvResponsableProyecto.setItems(responsablesDisponibles); 
            return;
        } 
        
        ObservableList<ResponsableProyecto> responsablesFiltrados = FXCollections.observableArrayList();
        for (ResponsableProyecto rp : responsablesDisponibles) {
            if (rp.getNombre().toLowerCase().contains(responsableBuscado)) {
                responsablesFiltrados.add(rp);
            }
        }
        tvResponsableProyecto.setItems(responsablesFiltrados);
    }
    
}
