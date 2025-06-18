/**
 * Nombre del archivo: FXML_BusquedaEstudianteValidarDocumentoController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 15/06/2025
 * Descripción: Controlador que permite buscar estudiantes con documentos entregados para su validación
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.IOException;
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
import sistemagestionpracticasprofesionales.SistemaGestionPracticasProfesionales;
import sistemagestionpracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la vista FXML_BusquedaEstudianteValidarDocumento
 * Permite buscar estudiantes con documentos entregados y abrir la vista de validación
 */
public class FXML_BusquedaEstudianteValidarDocumentoController implements Initializable {

    @FXML
    private TextField tfBusquedaEstudiante;
    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApPaterno;
    @FXML
    private TableColumn colApMaterno;
    @FXML
    private TableColumn colCorreo;
    
    private ObservableList<Estudiante> estudiantes= FXCollections.observableArrayList();
    private String tipoDocumento= "inicial";
    
    /**
     * Inicializa el controlador, configurando la tabla y cargando la lista de estudiantes
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaEstudiantes();
        cargarEstudiantes();
    }  
    
    /**
     * Configura las columnas de la tabla de estudiantes.
     */
    private void configurarTablaEstudiantes() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        colApMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    }
    
    /**
     * Carga en la tabla a los estudiantes con documentos entregados según la búsqueda por matrícula.
     */
    private void cargarEstudiantes() {
        try {
            estudiantes.clear();
            ArrayList<Estudiante> estudiantesDAO = EstudianteDAO.buscarEstudiantesConDocumentosEntregadosPorTipoYMatricula(
                tfBusquedaEstudiante.getText().trim(), tipoDocumento
            );
            if (estudiantesDAO != null) {
                estudiantes.addAll(estudiantesDAO);
            }
            tvEstudiantes.setItems(estudiantes);
        } catch (SQLException ex) {
            Logger.getLogger(FXML_BusquedaEstudianteValidarDocumentoController.class.getName()).log(Level.SEVERE, null, ex);
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "No se pudieron cargar los estudiantes.");
        }
    }
    
    
    /**
     * Cierra la ventana si el usuario lo confirma
     * @param event Evento de acción del botón cancelar
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
        Utilidad.cerrarVentanaActual(tfBusquedaEstudiante);
        } 
    }

    /**
     * Abre la vista para seleccionar el tipo de documento si un estudiante ha sido seleccionado
     * @param event Evento del botón aceptar
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        Estudiante estudianteSeleccionado = tvEstudiantes.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado != null) {
            Sesion.setEstudianteSeleccionado(estudianteSeleccionado);

            try {
                Stage escenario = new Stage();
                Parent vista = FXMLLoader.load(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_EleccionTipoDocumento.fxml"));
                Scene escena = new Scene(vista);
                escenario.setScene(escena);
                escenario.setTitle("Elección de tipo de documento");
                escenario.initModality(Modality.APPLICATION_MODAL);
                escenario.showAndWait();
            } catch (IOException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana");
            }

        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Ningún estudiante seleccionado", "Debes seleccionar un estudiante para continuar");
        }
    }
    
    /**
     * Actualiza la tabla conforme se escribe en el campo de búsqueda
     * @param event Evento de teclado que activa la búsqueda
     */
    @FXML
    private void buscarEstudiantePorMatricula(KeyEvent event) {
        cargarEstudiantes();
    }    
}
