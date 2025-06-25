/**
    * Nombre del archivo: FXML_EstudiantesPorEvaluarController.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 16/06/2025
    * Descripción: Controlador que muestra la lista completa de estudiantes que ya hicieron su presentación y aun no han sido evaluados.
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
 * Controlador para la vista FXML_EstudiantesPorEvaluar, permite buscar estudiantes que aun no han sido evaluados.
 */
public class FXML_EstudiantesPorEvaluarController implements Initializable {

    @FXML
    private TextField tfBusquedaEstudiante;
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

    private ObservableList<Estudiante> estudiantes;
    
    /**
     * Initializes the controller class.
     * Inicializa el controlador y carga los datos de los estudiantes.
     * @param url URL de localización del archivo FXML.
     * @param rb ResourceBundle con recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        estudiantes = FXCollections.observableArrayList();
        configurarTablaEstudiantes();
        cargarEstudiantes();
    }    

    /**
     * Configura las columnas de la tabla de estudiantes.
     */
    private void configurarTablaEstudiantes() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colProyectoAsignado.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
        colOrganizacionVinculada.setCellValueFactory(new PropertyValueFactory<>("nombreOV"));
    }
    
    /**
     * Carga la lista completa de estudiantes por evaluar.
     */
    private void cargarEstudiantes() {
        try {
            estudiantes.clear();
            ArrayList<Estudiante> listaEstudiantes = EstudianteDAO.obtenerEstudiantesPorEvaluar();
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
     * Cierra la ventana actual.
     * @param event Evento del botón regresar.
     */
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tvEstudiantes);
    }

    /**
     * Cierra la ventana si el usuario lo confirma.
     * @param event Evento del botón cancelar
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(tvEstudiantes);
        }
    }

    /**
     * Valida que haya sido seleccionado un estudiante y dirige a la ventana para evaluar al estudiante.
     * @param event Evento del botón aceptar.
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        Estudiante seleccionado = tvEstudiantes.getSelectionModel().getSelectedItem();
    
        if (seleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Debes seleccionar un estudiante antes de continuar.");
            return;
        }
        
        try {
            Utilidad.cerrarVentanaActual(tvEstudiantes);
            Stage escenarioEvaluar = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_EvaluarPresentacion.fxml"));
            Parent vista = loader.load();
            FXML_EvaluarPresentacionController controlador = loader.getController();
            controlador.inicializarDatos(seleccionado);
            Scene escena = new Scene(vista);
            escenarioEvaluar.setScene(escena);
            escenarioEvaluar.setTitle("Evaluar presentación de estudiante");
            escenarioEvaluar.initModality(Modality.APPLICATION_MODAL);
            escenarioEvaluar.showAndWait();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana para evaluar estudiante");
        }
    }

    /**
     * Busca un estudiante en la tabla que coincida con la matricula especificada.
     * @param event Evento de teclado que activa el filtro de búsqueda.
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
