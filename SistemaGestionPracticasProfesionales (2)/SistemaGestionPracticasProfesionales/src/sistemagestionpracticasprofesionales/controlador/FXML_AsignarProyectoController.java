/**
 * Nombre del archivo: FXML_AsignarProyectoController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 13/06/2025
 * Descripción: Controlador para asignar estudiantes a proyectos dentro del sistema de gestión de prácticas profesionales.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import sistemagestionpracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la vista FXML_AsignarProyecto, permite asignar estudiantes a proyectos disponibles
 */
public class FXML_AsignarProyectoController implements Initializable {

    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colNombreEstudiante;
    @FXML
    private TableColumn colApellidoPaterno;
    @FXML
    private TableColumn colApellidoMaterno;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableView<Proyecto> tvProyectos;
    @FXML
    private TableColumn colNombreProyecto;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colFechaInicio;
    @FXML
    private TableColumn colFechaTermino;
    private TableColumn colNombreOV;
    @FXML
    private TableColumn colNombreResponsable;
    @FXML
    private TableColumn colCantEstudiantes;
    @FXML
    private TextField tfBusquedaEstudiante;
    @FXML
    private TextField tfBusquedaProyecto;
    
    private ObservableList<Proyecto> proyectos= FXCollections.observableArrayList();
    private ObservableList<Estudiante> estudiantes= FXCollections.observableArrayList();
    @FXML
    private TableColumn<?, ?> colOrganizacionVinculada;
    
    /**
     * Inicializa el controlador y carga los datos iniciales en las tablas de estudiantes y proyectos
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaEstudiantes();
        configurarTablaProyectos();
        cargarEstudiantes();
        cargarProyectos();
    }    
    
    /**
     * Cierra la ventana actual
     */
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tvEstudiantes);
    }

    /**
     * Configura las columnas de la tabla de estudiantes
     */
    private void configurarTablaEstudiantes() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNombreEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    }
    
     /**
     * Configura las columnas de la tabla de proyectos
     */
    private void configurarTablaProyectos() {
        colNombreProyecto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaTermino.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colNombreOV.setCellValueFactory(new PropertyValueFactory<>("nombreOrganizacion"));
        colNombreResponsable.setCellValueFactory(new PropertyValueFactory<>("nombreResponsable"));
        colCantEstudiantes.setCellValueFactory(new PropertyValueFactory<>("cantidadEstudiantesParticipantes"));
    }
    
    /**
     * Carga los estudiantes sin proyecto en la tabla
     */
    private void cargarEstudiantes() {
        try {
            estudiantes.clear();
            ArrayList<Estudiante> estudiantesDAO = EstudianteDAO.buscarEstudiantesSinProyectoPorMatricula(tfBusquedaEstudiante.getText());
            if (estudiantesDAO != null) {
                estudiantes.addAll(estudiantesDAO);
            }
            tvEstudiantes.setItems(estudiantes);
        } catch (SQLException ex) {
            Logger.getLogger(FXML_AsignarProyectoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Carga los proyectos disponibles en la tabla
     */
    private void cargarProyectos() {
        try {
            proyectos.clear(); 
            ArrayList<Proyecto> proyectosDAO = ProyectoDAO.buscarProyectoPorNombre(tfBusquedaProyecto.getText());
            if (proyectosDAO != null) {
                proyectos.addAll(proyectosDAO);
            }
            tvProyectos.setItems(proyectos);
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de proyectos.");
        }
    }
    
    /**
     * Valida las selecciones y realiza la asignación del estudiante al proyecto.
     */
    private void validarSeleccion() {
        Estudiante estudianteSeleccionado = tvEstudiantes.getSelectionModel().getSelectedItem();
        Proyecto proyectoSeleccionado = tvProyectos.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Seleccionar estudiante", "Por favor selecciona un estudiante.");
            return;
        }
        if (proyectoSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Seleccionar proyecto", "Por favor selecciona un proyecto.");
            return;
        }

        if (proyectoSeleccionado.getEstudiantesAsignados() >= proyectoSeleccionado.getCantidadEstudiantesParticipantes()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Límite de estudiantes alcanzado","El proyecto '" + proyectoSeleccionado.getNombre() + "' ya tiene el máximo de estudiantes asignados");
            return;
        }
        
        try {
            boolean asignacionExitosa = ProyectoDAO.asignarProyectoAEstudiante(estudianteSeleccionado.getMatricula(), proyectoSeleccionado.getIdProyecto());
            if (asignacionExitosa) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Asignación exitosa", "Proyecto asignado correctamente.");
                cargarEstudiantes(); 
                cargarProyectos();  // actualizar la lista de proyectos para reflejar el cambio en cantidad
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al asignar", "No se pudo asignar el proyecto. Intenta de nuevo.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "Ocurrió un error al asignar el proyecto.");
        }
    }

    
    /**
     * Ejecuta búsqueda al escribir en el textField tfBusquedaEstudiante
     */
    @FXML
    private void buscarEstudiante(KeyEvent event){
        cargarEstudiantes();
    }
    
    /**
     * Ejecuta búsqueda al escribir en el textField tfBusquedaProyectos
     */
    @FXML
    private void buscarProyecto(KeyEvent event) {
        cargarProyectos();
    }

    /**
     * Ejecuta la validación y asignación de proyecto
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        validarSeleccion();
    }
    
    /**
     * Cierra la ventana si el usuario lo confirma
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
        Utilidad.cerrarVentanaActual(tfBusquedaProyecto);
        } 
    }
}
