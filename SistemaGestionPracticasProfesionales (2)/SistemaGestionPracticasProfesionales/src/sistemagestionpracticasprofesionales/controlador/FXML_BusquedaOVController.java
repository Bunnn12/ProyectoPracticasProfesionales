/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
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
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_BusquedaOVController implements Initializable {

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
    
    ObservableList<Proyecto> proyectos;

    /**
     * Initializes the controller class.
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

    @FXML
    private void clickAceptar(ActionEvent event) {
        Proyecto proyectoSeleccionado = tvProyectos.getSelectionModel().getSelectedItem();

        if (proyectoSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selección requerida", "Debes seleccionar un proyecto para continuar.");
            return;
        }

        Estudiante estudianteEnSesion = Sesion.getEstudianteSeleccionado();
        if (estudianteEnSesion == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de sesión", "No hay un estudiante autenticado en la sesión.");
            return;
        }

        int idEstudianteSesion = estudianteEnSesion.getIdEstudiante();

        try {
            if (ProyectoDAO.esProyectoDeEstudiante(idEstudianteSesion, proyectoSeleccionado.getIdProyecto())) {

                // Verificar si el estudiante tiene expediente
                int idExpediente = ExpedienteDAO.obtenerIdExpedientePorEstudiante(idEstudianteSesion);
                if (idExpediente == -1) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin expediente", 
                        "El estudiante aún no tiene expediente, por lo que no puede evaluarse la Organización Vinculada.");
                    return;
                }

                // Abrir ventana de evaluación
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_EvaluarOV.fxml"));
                Parent root = loader.load();

                FXML_EvaluarOVController controlador = loader.getController();
                controlador.inicializarDatos(proyectoSeleccionado);

                Stage stage = new Stage();
                stage.setTitle("Evaluar Organización Vinculada");
                stage.setScene(new Scene(root));
                stage.show();

                Utilidad.cerrarVentanaActual(tvProyectos);

            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Acceso denegado", "Solo puedes evaluar proyectos que te han sido asignados");
            }
        } catch (IOException | SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Ocurrió un error al intentar abrir la ventana: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(tfBusquedaProyecto);
        } 
    }
    
}
