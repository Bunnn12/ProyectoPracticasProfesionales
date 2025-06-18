/**
    * Nombre del archivo: FXML_ConsultarExpedienteController.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 16/06/2025
    * Descripción: Controlador que permite selecciona a un estudiante para poder ver su expediente.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * Controlador que permite selecciona a un estudiante para poder ver su expediente.
 */
public class FXML_ConsultarExpedienteController implements Initializable {

    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn<Estudiante, String> colMatricula;
    @FXML
    private TableColumn<Estudiante, String> colNombre;
    @FXML
    private TableColumn<Estudiante, String> colProyectoAsignado;
    @FXML
    private TableColumn<Estudiante, String> colOrganizacionVinculada;
    @FXML
    private TextField tfBusquedaEstudiante;
    
    private ObservableList<Estudiante> estudiantes;
    
    /**
     * Initializes the controller class.
     * Inicializa el controlador y carga los datos de los estudiantes
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        estudiantes = FXCollections.observableArrayList();
        configurarTablaEstudiantes();
        cargarEstudiantes();
    }    

    /**
     * Configura las columnas de la tabla
     */
    private void configurarTablaEstudiantes() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colProyectoAsignado.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
        colOrganizacionVinculada.setCellValueFactory(new PropertyValueFactory<>("nombreOV"));
    }
    
    /**
     * Carga la lista completa de estudiantes con un proyecto asignado
     */
    private void cargarEstudiantes() {
        try {
            estudiantes.clear();
            ArrayList<Estudiante> listaEstudiantes = EstudianteDAO.obtenerEstudiantesJuntoConSuProyectoYOrganizacionVinculadaYExpediente();
            if (listaEstudiantes != null) {
                estudiantes.addAll(listaEstudiantes);
            }
            tvEstudiantes.setItems(estudiantes);
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar la información de los estudiantes.");
        }
    }
    
    /**
     * Cierra la ventana actual
     * @param event Evento de acción del botón regresar
     */
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tvEstudiantes);
    }

    /**
     * Cierra la ventana si el usuario lo confirma
     * @param event Evento de acción del botón cerrar
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(tvEstudiantes);
        }
    }

    /**
     * Valida que un estudiante haya sido seleccionado para poder ver su expediente
     * @param event Evento de acción del botón aceptar
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        Estudiante estudianteSeleccionado = tvEstudiantes.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Por favor selecciona un estudiante para consultar su expediente.");
            return;
        }
        
        try {
            Stage escenarioEvaluar = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_ExpedienteEstudiante.fxml"));
            Parent vista = loader.load();
            FXML_ExpedienteEstudianteController controlador = loader.getController();
            controlador.inicializarDatos(estudianteSeleccionado);
            Scene escena = new Scene(vista);
            escenarioEvaluar.setScene(escena);
            escenarioEvaluar.setTitle("Expediente estudiante");
            escenarioEvaluar.initModality(Modality.APPLICATION_MODAL);
            escenarioEvaluar.showAndWait();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana para evaluar estudiante");
        }
    }

    /**
     * Busca un estudiante en la tabla que coincida con la matricula especificada
     * @param event Evento de teclado al escribir
     */
    @FXML
    private void buscarEstudiantePorMatricula(KeyEvent event) {
        String matriculaBuscada = tfBusquedaEstudiante.getText().toLowerCase();

        if (matriculaBuscada.isEmpty()) {
            tvEstudiantes.setItems(estudiantes); 
            return;
        }

        ObservableList<Estudiante> filtrados = FXCollections.observableArrayList();
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getMatricula().toLowerCase().contains(matriculaBuscada)) {
                filtrados.add(estudiante);
            }
        }

        tvEstudiantes.setItems(filtrados);
    }
    
}