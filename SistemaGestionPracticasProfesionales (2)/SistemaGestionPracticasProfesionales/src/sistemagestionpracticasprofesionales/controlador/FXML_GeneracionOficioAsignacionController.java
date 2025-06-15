/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
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
import sistemagestionpracticasprofesionales.modelo.dao.OficioAsignacionDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.DatosDocumentoAsignacion;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.utilidades.DocumentoGenerador;
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
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colGrupo.setCellValueFactory(new PropertyValueFactory("grupo"));
    }
        private void cargarEstudiantes() {
        try {
            estudiantes.clear();
            ArrayList<Estudiante> estudiantesDAO = EstudianteDAO.obtenerEstudiantesPeriodoActualConProyecto();
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
        Utilidad.cerrarVentanaActual(tvEstudiantes);
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
            try {
        ArrayList<DatosDocumentoAsignacion> listaDatos = EstudianteDAO.obtenerDatosDocumentosAsignacion();
        System.out.println("Registros únicos a generar: " + listaDatos.size());

        if (listaDatos.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin datos", "No hay estudiantes con proyecto asignado en el periodo actual");
            return;
        }

        int generados = 0;
        for (DatosDocumentoAsignacion datos : listaDatos) {
            System.out.println("Generando documento para: " + datos.getNombreCompleto() + " (" + datos.getMatricula() + ")");

            // Generar archivo docx
            File archivoDoc = DocumentoGenerador.generarOficioAsignacion(datos);
            if (archivoDoc != null && archivoDoc.exists()) {
                byte[] contenidoDoc = Files.readAllBytes(archivoDoc.toPath());

                // Validar existencia del estudiante antes de guardar
                if (EstudianteDAO.existeEstudiante(datos.getIdEstudiante())) {
                    boolean guardado = OficioAsignacionDAO.guardarOficio(datos.getIdEstudiante(), datos.getIdProyecto(), contenidoDoc);
                    if (guardado) {
                        generados++;
                    } else {
                        System.out.println("No se pudo guardar oficio para estudiante con id: " + datos.getIdEstudiante());
                    }
                } else {
                    System.out.println("Estudiante con id " + datos.getIdEstudiante() + " no existe.");
                }
            } else {
                System.out.println("No se pudo generar archivo para: " + datos.getNombreCompleto());
            }
        }

        if (generados > 0) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Se generaron y guardaron " + generados + " documentos correctamente");
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo generar o guardar ningún documento.");
        }

    } catch (Exception ex) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Ocurrió un error al generar los documentos");
        ex.printStackTrace();
    }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
        Utilidad.cerrarVentanaActual(tvEstudiantes);
        } 
    }
    public static byte[] leerArchivoBytes(File archivo) throws IOException {
        return Files.readAllBytes(archivo.toPath());
    }

}
