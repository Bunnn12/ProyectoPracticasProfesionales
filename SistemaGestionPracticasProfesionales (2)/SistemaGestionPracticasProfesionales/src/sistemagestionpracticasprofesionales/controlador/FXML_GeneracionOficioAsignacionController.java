/**
 * Nombre del archivo: FXML_GeneracionOficioAsignacionController.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 13/06/2025
 * Descripción: Controlador que permite generar y guardar oficios de asignación para estudiantes con proyecto asignado en el periodo actual.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
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
import javafx.scene.control.cell.PropertyValueFactory;
import sistemagestionpracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemagestionpracticasprofesionales.modelo.dao.OficioAsignacionDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.DatosDocumentoAsignacion;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.utilidades.DocumentoGenerador;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la vista FXML_GeneracionOficioAsignacion.
 * Permite visualizar estudiantes con proyecto asignado del periodo actual y generar oficios de asignación.
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
     * Inicializa el controlador, configura la tabla y carga los estudiantes del periodo actual con proyecto asignado.
     *
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
     * Configura las columnas de la tabla de estudiantes
     */
    private void configurarTablaEstudiantes() {
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colGrupo.setCellValueFactory(new PropertyValueFactory("grupo"));
    }
    
    /**
     * Carga los estudiantes que tienen proyecto asignado en el periodo actual
     */
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
    
    /**
     * Cierra la ventana actual.
     * 
     * @param event Evento del botón regresar.
     */
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tvEstudiantes);
    }

    /**
     * Genera y guarda los oficios de asignación para los estudiantes que cumplan los requisitos (los que salen en la tabla)
     * 
     * @param event Evento del botón aceptar
     */
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

                File archivoDoc = DocumentoGenerador.generarOficioAsignacion(datos);
                if (archivoDoc != null && archivoDoc.exists()) {
                    byte[] contenidoDoc = Files.readAllBytes(archivoDoc.toPath());

                    if (EstudianteDAO.existeEstudiante(datos.getIdEstudiante())) {
                        boolean guardado = OficioAsignacionDAO.guardarOficio(datos.getIdEstudiante(), datos.getIdProyecto(), contenidoDoc);
                        if (guardado) {
                            generados++;
                        } else {
                            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo guardar oficio para estudiante con matrícula: " + datos.getMatricula());
                        }
                    } else {
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Estudiante con matrícula " + datos.getMatricula() + " no existe");
                    }
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo generar archivo para: " + datos.getNombreCompleto() + " (Matrícula: " + datos.getMatricula() + ")");
                }
            }

            if (generados > 0) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Se generaron y guardaron " + generados + " documentos correctamente");
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo generar o guardar ningún documento");
            }

        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de archivo", "Hubo un problema al leer o escribir en un archivo");
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
        } catch (NullPointerException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error inesperado", "Se encontró un valor nulo inesperado");
        }
    }

    /**
     * Cierra la ventana si el usuario confirma la acción.
     * 
     * @param event Evento del botón cancelar.
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(tvEstudiantes);
        } 
    }
    
    /**
     * Lee el contenido de un archivo en un arreglo de bytes.
     * 
     * @param archivo Archivo a leer.
     * @return Arreglo de bytes con el contenido del archivo.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static byte[] leerArchivoBytes(File archivo) throws IOException {
        return Files.readAllBytes(archivo.toPath());
    }

}
