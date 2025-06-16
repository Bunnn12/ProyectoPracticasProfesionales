/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Reporte;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_ReportesDisponiblesEstudianteController implements Initializable {

    @FXML
    private TableView<Reporte> tvReportes;
    @FXML
    private TableColumn colNombreReporte;
    @FXML
    private TableColumn colFechaEntrega;
    private ObservableList<Reporte> reportes = FXCollections.observableArrayList();
    private int idEstudiante;
    private String nombreEstudiante;
    @FXML
    private TableColumn colEstado;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
        this.nombreEstudiante = EstudianteDAO.obtenerNombreEstudiantePorId(idEstudiante);
        configurarTablaReportes();
        cargarReportes();
    }
    private void configurarTablaReportes() {
        colNombreReporte.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }
     
    public void cargarReportes() {
        try {
            reportes.clear();
            ArrayList<Reporte> reportesDAO = ExpedienteDAO.obtenerReportesPorEstudiante(idEstudiante);
            if (reportesDAO != null) {
                reportes.addAll(reportesDAO);
            }
            tvReportes.setItems(reportes);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(
                javafx.scene.control.Alert.AlertType.ERROR,"Sin conexion","No hay conexion con la base de datos");
            ex.printStackTrace();
        }
    }     
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tvReportes);
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
        Reporte reporteSeleccionado = tvReportes.getSelectionModel().getSelectedItem();

        if (reporteSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(
                javafx.scene.control.Alert.AlertType.WARNING,
                "Sin selección",
                "Por favor selecciona un reporte para visualizar."
            );
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_VisualizacionReporte.fxml"));
            Parent vista = loader.load();

            FXML_VisualizacionReporteController controller = loader.getController();
            controller.cargarReporte(reporteSeleccionado, nombreEstudiante);

            controller.setControladorPrincipal(this);

            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Visualización de reporte");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(
                javafx.scene.control.Alert.AlertType.ERROR,
                "Error al abrir ventana",
                "Ocurrió un error al intentar abrir la visualización del reporte."
            );
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
        Utilidad.cerrarVentanaActual(tvReportes);
        } 
    }

}
