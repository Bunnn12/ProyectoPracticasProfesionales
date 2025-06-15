/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
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
import javafx.scene.control.Label;
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
 * FXML Controller class
 *
 * @author reino
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
    private TableColumn colNombreOV;
    @FXML
    private TableColumn colNombreResponsable;
    @FXML
    private TableColumn colCantEstudiantes;
    private ObservableList<Proyecto> proyectos;
    private ObservableList<Estudiante> estudiantes;
    @FXML
    private TextField tfBusquedaEstudiante;
    @FXML
    private TextField tfBusquedaProyecto;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        estudiantes = FXCollections.observableArrayList();
        proyectos = FXCollections.observableArrayList();
        configurarTablaEstudiantes();
        configurarTablaProyectos();
        cargarEstudiantes();
        cargarProyectos();
    }    

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tvEstudiantes);
    }

    
    private void configurarTablaEstudiantes() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNombreEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    }

    private void configurarTablaProyectos() {
        colNombreProyecto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaTermino.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colNombreOV.setCellValueFactory(new PropertyValueFactory<>("nombreOrganizacion"));
        colNombreResponsable.setCellValueFactory(new PropertyValueFactory<>("nombreResponsable"));
        colCantEstudiantes.setCellValueFactory(new PropertyValueFactory<>("cantidadEstudiantesParticipantes"));
    }
    
    
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
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Límite de estudiantes alcanzado",
                "El proyecto '" + proyectoSeleccionado.getNombre() + "' ya tiene el máximo de estudiantes asignados.");
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


    @FXML
    private void buscarEstudiante(KeyEvent event){
        cargarEstudiantes();
    }

    @FXML
    private void buscarProyecto(KeyEvent event) {
        cargarProyectos();
    }


    @FXML
    private void clickAceptar(ActionEvent event) {
        validarSeleccion();
    }
    
    @FXML
    private void clickCancelar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tvEstudiantes);
    }


}
