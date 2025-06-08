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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sistemagestionpracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_GeneracionOficioAsignacionController implements Initializable {

    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellidoPaterno;
    @FXML
    private TableColumn colApellidoMaterno;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colGrupo;
    private ObservableList<Estudiante> estudiantes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        estudiantes = FXCollections.observableArrayList();
        configurarTablaEstudiantes();
        cargarEstudiantes();
    }    
    
        private void configurarTablaEstudiantes() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));
    }
        private void cargarEstudiantes() {
        try {
            estudiantes.clear();
            ArrayList<Estudiante> estudiantesDAO = EstudianteDAO.obtenerEstudiantesPeriodoActual();
            if (estudiantesDAO != null) {
                estudiantes.addAll(estudiantesDAO);
            }
            tvEstudiantes.setItems(estudiantes);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
            ex.printStackTrace();
        }
    }
    @FXML
    private void clickRegresar(ActionEvent event) {
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
    }
    
}
