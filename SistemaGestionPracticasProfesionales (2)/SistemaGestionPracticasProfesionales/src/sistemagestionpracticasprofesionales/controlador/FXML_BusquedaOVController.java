/**
 * Nombre del archivo: FXML_BusquedaOVController.java
 * Autor: 
 * Fecha: 16/06/25
 * Descripción: Controlador de la interfaz para la búsqueda y selección de proyectos asignados
 * al estudiante en sesión, con el propósito de evaluar la Organización Vinculada (OV)
 * asociada a dichos proyectos. Gestiona la visualización, filtrado y selección de proyectos,
 * validando que el estudiante tenga expediente y que el proyecto pertenezca al mismo.
 * Permite abrir la ventana de evaluación o cancelar la operación.
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
 * Controlador para la vista principal del estudiante.
 * Permite gestionar la sesión del estudiante en curso y controlar
 * la navegación a otras vistas desde la interfaz principal.
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
     * Inicializa el controlador, configura las columnas de la tabla y carga los proyectos desde la base de datos.
     * 
     * @param url URL del recurso (no utilizado).
     * @param rb ResourceBundle (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaProyectos();
        cargarProyectos();
    }    

   /**
     * Configura los PropertyValueFactory para cada columna de la tabla,
     * vinculando cada columna con la propiedad correspondiente del objeto Proyecto.
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
     * Carga la lista de proyectos desde la base de datos, la agrega a la lista observable
     * y asigna esta lista a la tabla para su visualización.
     * 
     * @throws SQLException si ocurre un error al acceder a la base de datos.
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
     * Filtra la lista de proyectos mostrados en la tabla según el texto ingresado
     * en el campo de búsqueda, permitiendo búsquedas parciales por nombre.
     * 
     * @param event Evento de teclado que se produce al escribir en el campo de búsqueda.
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
     * Evento manejador para el botón aceptar. Valida que el proyecto seleccionado
     * pertenezca al estudiante en sesión, que el estudiante tenga expediente y,
     * en caso afirmativo, abre la ventana para evaluar la Organización Vinculada.
     * Muestra mensajes de alerta en caso de error o condiciones no cumplidas.
     * 
     * @param event Evento de acción del botón aceptar.
     * @throws IOException si ocurre un error al cargar la ventana de evaluación.
     * @throws SQLException si ocurre un error al validar datos en la base de datos.
     */
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

                int idExpediente = ExpedienteDAO.obtenerIdExpedientePorEstudiante(idEstudianteSesion);
                if (idExpediente == -1) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin expediente", 
                        "El estudiante aún no tiene expediente, por lo que no puede evaluarse la Organización Vinculada.");
                    return;
                }

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

    
     /**
     * Evento manejador para el botón cancelar. Solicita confirmación para cerrar
     * la ventana actual si el usuario confirma la acción.
     * 
     * @param event Evento de acción del botón cancelar.
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(tfBusquedaProyecto);
        } 
    }
    
}
