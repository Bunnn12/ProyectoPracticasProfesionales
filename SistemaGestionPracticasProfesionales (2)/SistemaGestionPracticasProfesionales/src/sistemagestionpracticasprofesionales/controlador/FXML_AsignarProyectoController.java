/**
 * Nombre del archivo: FXML_AsignarProyectoController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 13/06/2025
 * Descripción: Controlador para asignar estudiantes a proyectos dentro del sistema de gestión de prácticas profesionales.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
    @FXML
    private TableColumn colNombreResponsable;
    @FXML
    private TableColumn colCantEstudiantes;
    @FXML
    private TextField tfBusquedaEstudiante;
    @FXML
    private TextField tfBusquedaProyecto;
    @FXML
    private TableColumn colOrganizacionVinculada;
    
    private ObservableList<Proyecto> proyectos= FXCollections.observableArrayList();
    private ObservableList<Estudiante> estudiantes= FXCollections.observableArrayList();
    
    /**
     * Inicializa el controlador y carga los datos iniciales en las tablas de estudiantes y proyectos.
     * @param url URL de inicialización.
     * @param rb ResourceBundle con recursos localizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaEstudiantes();
        configurarTablaProyectos();
        cargarEstudiantes();
        cargarProyectos();
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
        colOrganizacionVinculada.setCellValueFactory(new PropertyValueFactory<>("nombreOrganizacion"));
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
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexion", "No hay conexión con la base de datos");
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
     * Valida las selecciones, que el proyecto no haya terminado y realiza la asignación del estudiante al proyecto
     */
    private void validarSeleccion() {
        Estudiante estudianteSeleccionado = tvEstudiantes.getSelectionModel().getSelectedItem();
        Proyecto proyectoSeleccionado = tvProyectos.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error Selección", "Por favor selecciona un estudiante.");
            return;
        }
        if (proyectoSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error Selección", "Por favor selecciona un proyecto.");
            return;
        }
        if (proyectoSeleccionado== null && estudianteSeleccionado== null){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error Selección", 
                    "Tienes que seleccionar ambos, un proyecto y un estudiante, inténtalo nuevamente");
            return;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fechaHoy = LocalDate.now();
            LocalDate fechaFin = LocalDate.parse(proyectoSeleccionado.getFechaFin(), formatter);

            if (fechaHoy.isAfter(fechaFin)) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Proyecto finalizado",
                    "El proyecto seleccionado ya ha finalizado. Por favor selecciona otro proyecto");
                return;
            }
        } catch (DateTimeParseException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de formato de fecha",
                "La fecha de fin del proyecto no tiene un formato válido.");
            return;
        }


        if (proyectoSeleccionado.getEstudiantesAsignados() >= proyectoSeleccionado.getCantidadEstudiantesParticipantes()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Límite de estudiantes alcanzado","El proyecto '" + proyectoSeleccionado.getNombre() + 
                    "' ya tiene el máximo de estudiantes asignados, seleccione otro proyecto para continuar");
            return;
        }
        
        try {
            boolean asignacionExitosa = ProyectoDAO.asignarProyectoAEstudiante(estudianteSeleccionado.getMatricula(), proyectoSeleccionado.getIdProyecto());
            if (asignacionExitosa) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Asignación exitosa", "Proyecto asignado correctamente");
                cargarEstudiantes(); 
                cargarProyectos();  
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al asignar", "No se pudo asignar el proyecto. Intenta de nuevo.");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "Ocurrió un error al asignar el proyecto.");
        }
    }

    
    /**
     * Ejecuta búsqueda al escribir en el campo tfBusquedaEstudiante.
     * @param event Evento de teclado.
     */
    @FXML
    private void buscarEstudiante(KeyEvent event){
        cargarEstudiantes();
    }
    
    /**
     * Ejecuta búsqueda al escribir en el campo tfBusquedaProyecto
     * @param event Evento de teclado
     */
    @FXML
    private void buscarProyecto(KeyEvent event) {
        cargarProyectos();
    }

    /**
     * Ejecuta la validación y asignación de proyecto
     * @param event Evento del botón aceptar
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        validarSeleccion();
    }
    
    /**
     * Cierra la ventana si el usuario lo confirma
     * @param event Evento del botón cancelar
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(tfBusquedaProyecto);
        } 
    }
}
