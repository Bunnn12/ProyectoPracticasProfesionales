/**
    * Nombre del archivo: FXML_EvaluarEstudianteController.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 16/06/2025
    * Descripción: Controlador que permite evaluar la presentación de un estudiante en base a los criterios de evaluación establecidos.
*/
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import sistemagestionpracticasprofesionales.interfaz.INotificacion;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.dao.CriterioEvaluacionDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.CriterioEvaluacion;
import sistemagestionpracticasprofesionales.modelo.pojo.CriterioEvaluacionFila;
import sistemagestionpracticasprofesionales.modelo.dao.EvaluacionPresentacionDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.EvaluacionPresentacion;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;


/**
 * FXML Controller class

 Controlador para la vista FXML_EvaluarPresentacionController, permite evaluar la presentación de un estudiante.
 */
public class FXML_EvaluarPresentacionController implements Initializable {

    @FXML
    private TableView<CriterioEvaluacionFila> tvCriterios;
    @FXML
    private TableColumn<CriterioEvaluacionFila, String> colCriterio;
    @FXML
    private TableColumn<CriterioEvaluacionFila, String> colDescripcion;
    @FXML
    private TableColumn<CriterioEvaluacionFila, CheckBox> col10;
    @FXML
    private TableColumn<CriterioEvaluacionFila, CheckBox> col9;
    @FXML
    private TableColumn<CriterioEvaluacionFila, CheckBox> col8;
    @FXML
    private TableColumn<CriterioEvaluacionFila, CheckBox> col7;
    @FXML
    private TableColumn<CriterioEvaluacionFila, CheckBox> col6;
    @FXML
    private TableColumn<CriterioEvaluacionFila, CheckBox> col5;
    @FXML
    private Label lbNombreEstudiante;
    @FXML
    private Label lbNombreProyecto;
    @FXML
    private Label lbNombreOV;
    @FXML
    private TextArea taRetroalimentacion;

    private int idEstudiante;
    INotificacion observador;
    
    /**
     * Initializes the controller class.
     * Inicializa el controlador y carga los criterios de evaluación.
     * @param url URL de localización del archivo FXML.
     * @param rb ResourceBundle con recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarCriterios();
    }
    
    /**
     * Recupera los datos del estudiante seleccionado por evaluar.
     * @param estudiante que fue seleccionado para evaluar.
     */
    public void inicializarDatos(Estudiante estudiante) {
        idEstudiante = estudiante.getIdEstudiante();
        lbNombreEstudiante.setText(estudiante.getNombreCompleto());
        lbNombreProyecto.setText(estudiante.getNombreProyecto());
        lbNombreOV.setText(estudiante.getNombreOV());
    }

    /**
     * Configura las columnas de la tabla de criterios.
     */
    private void configurarColumnas() {
        colCriterio.setCellValueFactory(cellData -> cellData.getValue().nombreCriterioProperty());
        colDescripcion.setPrefWidth(240);
        colDescripcion.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        colDescripcion.setCellFactory(columna -> {
            return new TableCell<CriterioEvaluacionFila, String>() {
                private final Label label = new Label();

                {
                    label.setWrapText(true); // Permitir múltiples líneas
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        label.setText(item);
                        setGraphic(label);
                        
                        // Forzar que la fila se adapte al contenido del label
                        label.heightProperty().addListener((obs, oldVal, newVal) -> {
                            this.setMinHeight(newVal.doubleValue() + 10); // margen
                            this.setPrefHeight(newVal.doubleValue() + 10);
                        });
                    }
                }
            };
        });
        
        col10.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCb10()));
        col9.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCb9()));
        col8.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCb8()));
        col7.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCb7()));
        col6.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCb6()));
        col5.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCb5()));
    }

    /**
     * Carga los criterios de evaluación en la tabla.
     */
    private void cargarCriterios() {
        try {
            List<CriterioEvaluacion> listaCriterios = CriterioEvaluacionDAO.obtenerCriteriosEvaluacionPresentacion();
            ObservableList<CriterioEvaluacionFila> items = FXCollections.observableArrayList();
            for (CriterioEvaluacion criterio : listaCriterios) {
                items.add(new CriterioEvaluacionFila(criterio));
            }
            tvCriterios.setItems(items);
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al cargar criterios de evaluación: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Cierra la ventana actual.
     * @param event Evento del botón regresar.
     */
    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tvCriterios);
    }

    /**
     * Cierra la ventana si el usuario lo confirma.
     * @param event Evento del botón cancelar.
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(tvCriterios);
        }
    }

    /**
     * Guarda la evaluación de la presentación del estudiante.
     * @param event Evento del botón aceptar.
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        ObservableList<CriterioEvaluacionFila> filas = tvCriterios.getItems();
        double suma = 0;
        int total = filas.size();

        for (CriterioEvaluacionFila fila : filas) {
            int calificacion = fila.getCalificacionSeleccionada();
            if (calificacion == 0) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Evaluación incompleta", 
                    "Debes seleccionar una calificación en cada criterio.");
                return;
            }
            suma += calificacion;
        }
        
        if (taRetroalimentacion.getText().isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Retroalimentación requerida", "Debe escribir una retroalimentación.");
            return;
            
        }
        else if (taRetroalimentacion.getLength() < 10) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Retroalimentación inválida", "Debe contener mínimo 10 caracteres.");
            return;
        }
        
        double promedio = suma / total;
        EvaluacionPresentacion evaluacion = new EvaluacionPresentacion();
        evaluacion.setPuntajeTotalObtenido(promedio);
        evaluacion.setRetroalimentacion(taRetroalimentacion.getText().trim());
        
        try {
            ResultadoOperacion resultado = EvaluacionPresentacionDAO.registrarEvaluacionPresentacion(evaluacion, idEstudiante);
            
            if (!resultado.isError()) {
                EvaluacionPresentacionDAO.registrarDetallesEvaluacion(evaluacion.getIdEvaluacionPresentacion(), filas);
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Evaluación insertada", resultado.getMensaje());
                Utilidad.cerrarVentanaActual(tvCriterios);
                if (observador != null) {
                    // observador.operacionExitosa("Insertar", evaluacion.getNombre());
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al registrar", resultado.getMensaje());
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexión", "No se pudo guardar la evaluacion: " + e.getMessage());
        }
        
    }
    
    
}