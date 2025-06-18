/**
    * Nombre del archivo: FXML_ActualizarResponsableController.java
    * Autor: Astrid Azucena Torres Lagunes
    * Fecha: 13/06/2025
    * Descripción: Controlador que permite actualizar el responsable de un proyecto en el sistema de gestión de prácticas profesionales.
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
import sistemagestionpracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la vista FXML_ActualizarResponsable que permite actualizar el responsable de un proyecto.
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
    @FXML
    private TableColumn colOrganizacionVinculada;
    
    ObservableList<ResponsableProyecto> responsablesDisponibles;
    private Proyecto proyecto;
    
    /**
     * Inicializa el controlador y configura la tabla de responsables de proyecto.
     * @param url URL de localización del archivo FXML.
     * @param rb ResourceBundle con recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaResponsables();
    } 
    
     /**
      * Inicializa el proyecto seleccionado
      * @param proyecto Proyecto seleccionado al que se le cambiará el responsable
      */
    public void inicializarProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        lbProyectoSeleccionado.setText(proyecto.getNombre()); 
        lbResponsableActual.setText(proyecto.getNombreResponsable()); 
        cargarResponsablesProyecto();
    }
    
    /**
     * Carga la lista de responsables que no tienen proyecto asignado de la organización
     * vinculada del proyecto seleccionado
     */    
    private void cargarResponsablesProyecto() {
        try {
            responsablesDisponibles = FXCollections.observableArrayList();
            List<ResponsableProyecto> responsablesDisponiblesDAO = ResponsableProyectoDAO
                .obtenerResponsableSinProyectoDeUnaOV(proyecto.getIdOrganizacionVinculada());

            responsablesDisponibles.addAll(responsablesDisponiblesDAO);
            tvResponsableProyecto.setItems(responsablesDisponibles);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", 
                "No hay conexión con la base de datos");
            Utilidad.cerrarVentanaActual(tvResponsableProyecto);
        }
    }

    
    /**
     * Configura las columnas de la tabla de responsables
     */
    private void configurarTablaResponsables(){
        colNombreResponsable.setCellValueFactory(new PropertyValueFactory("nombre"));
        colCorreoResponsable.setCellValueFactory(new PropertyValueFactory("correo"));
        colOrganizacionVinculada.setCellValueFactory(new PropertyValueFactory("nombreOrganizacionVinculada"));
        colTelefonoResponsable.setCellValueFactory(new PropertyValueFactory("telefono"));
    }
    
     /**
      * Actualiza el responsable del proyecto seleccionado
      * @param event Evento del botón aceptar
      */
    @FXML
    private void clickAceptar(ActionEvent event) {
        ResponsableProyecto nuevoResponsable = tvResponsableProyecto.getSelectionModel().getSelectedItem();

        if (nuevoResponsable == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin Seleccion", "Debes seleccionar un nuevo responsable para continuar");
            return;
        }

        try {
            boolean actualizado = ProyectoDAO.actualizarResponsableDeProyecto(proyecto.getIdProyecto(), nuevoResponsable.getIdResponsable());
            if (actualizado) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Actualizacion Exitosa", "El responsable del proyecto ha sido actualizado exitosamente");
                Utilidad.cerrarVentanaActual(tfBuscarResponsable);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al actualizar", "No se pudo actualizar el responsable, Intenta más tarde");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
        }
    }
    
    /**
     * Cierra la ventana actual si el usuario lo confirma
     * @param event Evento del botón cancelar
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Seguro Cancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(tfBuscarResponsable);
        } 
    }

    /**
     * Filtra la lista de responsables por nombre conforme se escribe en el campo de búsqueda
     * @param event Evento de teclado que activa el filtro de búsqueda
     */
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
