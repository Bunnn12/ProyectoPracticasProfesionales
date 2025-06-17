/**
 * Nombre del archivo: FXML_BusquedaProyectoController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 14/06/2025
 * Descripción: Controlador para la vista de búsqueda de proyectos que permite seleccionar un proyecto para editar su responsable.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la vista FXML_BusquedaProyecto. 
 * Permite listar y buscar proyectos, y seleccionar uno para modificar su responsable.
 */
public class FXML_BusquedaProyectoController implements Initializable {

    @FXML
    private TextField tfBusquedaProyecto;
    @FXML
    private TableView<Proyecto> tvProyectos;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colFechaInicio;
    @FXML
    private TableColumn colFechaTermino;
    @FXML
    private TableColumn colOV;
    @FXML
    private TableColumn colResponsable;
    @FXML
    private TableColumn colCantEstudiantes;
    
    ObservableList<Proyecto> proyectos;
    
    /**
     * Inicializa el controlador, configura la tabla y carga los proyectos.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaProyectos();
        cargarProyectos();
    }    

    /**
     * Configura las columnas de la tabla de proyectos.
     */
    private void configurarTablaProyectos(){
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
        colCantEstudiantes.setCellValueFactory(new PropertyValueFactory("cantidadEstudiantesParticipantes"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        colFechaTermino.setCellValueFactory(new PropertyValueFactory("fechaFin"));
        colOV.setCellValueFactory(new PropertyValueFactory("nombreOrganizacion"));
        colResponsable.setCellValueFactory(new PropertyValueFactory("nombreResponsable"));
    }

    /**
     * Carga todos los proyectos disponibles desde la base de datos.
     */
    private void cargarProyectos(){
        try {
           proyectos = FXCollections.observableArrayList();
           List<Proyecto> proyectosDAO = ProyectoDAO.obtenerProyectos(); 
           proyectos.addAll(proyectosDAO);
           tvProyectos.setItems(proyectos);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de los proyectos, por favor intentélo más tarde");
            Utilidad.cerrarVentanaActual(tvProyectos);
        }
    }
    
    /**
     * Filtra los proyectos en la tabla conforme se escribe en el campo de búsqueda.
     * @param event Evento de teclado al escribir
     */
    @FXML
    private void buscarProyectoPorNombre(KeyEvent event) {
        String proyectoBuscado = tfBusquedaProyecto.getText().toLowerCase();

        if (proyectoBuscado.isEmpty()) {
            tvProyectos.setItems(proyectos); 
            return;
        } 
        
        ObservableList<Proyecto> proyectosFiltrados = FXCollections.observableArrayList();
        for (Proyecto p : proyectos) {
            if (p.getNombre().toLowerCase().contains(proyectoBuscado)) {
                proyectosFiltrados.add(p);
            }
        }
        tvProyectos.setItems(proyectosFiltrados);
        
    }
    
    /**
     * Cierra la ventana actual sin necesidad de confirmación.
     * @param event Evento del botón regresar
     */
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfBusquedaProyecto);
    }
    
    
    /**
     * Cierra la ventana actual si el usuario confirma la cancelación.
     * @param event Evento de acción del botón cancelar
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(tfBusquedaProyecto);
        } 
    }

    /**
     * Abre la vista para actualizar el responsable del proyecto seleccionado
     * @param event Evento del botón aceptar
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        Proyecto proyectoSeleccionado = tvProyectos.getSelectionModel().getSelectedItem();
    
        if (proyectoSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Por favor selecciona un proyecto para continuar");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_ActualizarResponsable.fxml"));
            Parent root = loader.load();

            FXML_ActualizarResponsableController controlador = loader.getController();
            controlador.inicializarProyecto(proyectoSeleccionado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Actualizar Responsable");
            stage.show();

            Utilidad.cerrarVentanaActual(tfBusquedaProyecto);
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana de actualizar responsable");
        }
    } 
}
