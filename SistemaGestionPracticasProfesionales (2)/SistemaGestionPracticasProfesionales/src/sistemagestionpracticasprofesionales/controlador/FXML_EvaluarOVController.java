/**
 * Nombre del archivo: FXML_EvaluarOVController.java
 * Autor: Juan Pablo Silva Miranda
 * Fecha: 17/06/2025
 * Descripción: Controlador para la vista de evaluación de organizaciones vinculadas (OV).
 * Permite mostrar los criterios de evaluación, capturar las calificaciones y retroalimentación,
 * y guardar la evaluación realizada para un proyecto vinculado.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sistemagestionpracticasprofesionales.modelo.dao.CriterioEvaluacionDAO;
import sistemagestionpracticasprofesionales.modelo.dao.EvaluacionOvDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.CriterioEvaluacion;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * Controlador para la interfaz de evaluación de organizaciones vinculadas (OV).
 * 
 * Funcionalidades:
 * - Mostrar la información del proyecto y la organización vinculada.
 * - Cargar y mostrar los criterios de evaluación con opciones para asignar puntajes.
 * - Validar la retroalimentación y las calificaciones ingresadas.
 * - Guardar la evaluación y actualizar el estado del expediente correspondiente.
 * - Permitir cancelar la evaluación con confirmación.
 */
public class FXML_EvaluarOVController implements Initializable {

    @FXML
    private Label lbNombreProyecto;
    @FXML
    private Label lbNombreOrganizacionVinculada;
    @FXML
    private ScrollPane spCriteriosEvaluacion;
    @FXML
    private VBox vBoxCriteriosEvaluacion;

    private Proyecto proyecto;
    private Map<Integer, ComboBox<Integer>> mapComboPuntajes = new HashMap<>();
    private List<CriterioEvaluacion> criterios;
    @FXML
    private TextArea taRetroalimentacion;

    /**
     * Inicializa el controlador y los elementos de la interfaz.
     * No realiza acciones específicas en esta implementación.
     *
     * @param url Ruta utilizada para resolver recursos relativos
     * @param rb  Recursos locales para la internacionalización
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    
    /**
     * Recibe el proyecto a evaluar, actualiza las etiquetas y carga los criterios.
     *
     * @param proyecto Proyecto que se evaluará.
     */
    public void inicializarDatos(Proyecto proyecto) {
    if (proyecto != null) {
        this.proyecto = proyecto;
        System.out.println("Proyecto recibido: " + proyecto.getNombre() + ", idResponsable: " + proyecto.getIdResponsableProyecto());
        lbNombreProyecto.setText(proyecto.getNombre());
        lbNombreOrganizacionVinculada.setText(proyecto.getNombreOrganizacion());
        cargarCriterios(); 
    }
}


    /**
     * Carga los criterios de evaluación desde la base de datos,
     * los muestra en la interfaz con sus respectivos ComboBox para puntuar.
     */
    private void cargarCriterios() {
        criterios = CriterioEvaluacionDAO.obtenerCriterios();
        vBoxCriteriosEvaluacion.getChildren().clear();
        mapComboPuntajes.clear();

        for (CriterioEvaluacion criterio : criterios) {
            HBox hbox = new HBox(20); 
            hbox.setPrefWidth(1020);
            hbox.setMaxWidth(1020);

            VBox vboxTextos = new VBox(3);
            Label lblNombre = new Label(criterio.getNombreCriterio());
            lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
            Label lblDescripcion = new Label(criterio.getDescripcion());
            lblDescripcion.setWrapText(true);

            vboxTextos.getChildren().addAll(lblNombre, lblDescripcion);

            vboxTextos.setPrefWidth(1020 * 0.85);
            vboxTextos.setMaxWidth(1020 * 0.85);

            ComboBox<Integer> cbPuntaje = new ComboBox<>();
            cbPuntaje.getItems().addAll(1, 2, 3, 4, 5);
            cbPuntaje.setPromptText("Puntaje");
            cbPuntaje.setPrefWidth(120); 

            hbox.getChildren().addAll(vboxTextos, cbPuntaje);

            vBoxCriteriosEvaluacion.getChildren().add(hbox);

            mapComboPuntajes.put(criterio.getIdCriterio(), cbPuntaje);
        }
    }



     /**
     * Evento que se activa al hacer clic en el botón "Aceptar".
     * Valida los datos, calcula el puntaje total y guarda la evaluación.
     *
     * @param event Evento del clic.
     */
    @FXML
private void clickAceptar(ActionEvent event) {
    if (proyecto == null) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No hay proyecto seleccionado.");
        return;
    }

    int idEstudiante = Sesion.getEstudianteSeleccionado().getIdEstudiante();
    int idExpediente = ExpedienteDAO.obtenerIdExpedientePorEstudiante(idEstudiante);
    if (idExpediente == -1) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se encontró expediente para el estudiante.");
        return;
    }

    double puntajeTotal = calcularPuntajeTotal();
    if (puntajeTotal == -1) return;

    double puntajeMax = criterios.size() * 5;
    double puntajeMinAprob = puntajeMax * 0.6;

    String retroalimentacion = taRetroalimentacion.getText().trim();
    if (retroalimentacion.isEmpty()) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Retroalimentación requerida", "Debes escribir una retroalimentación.");
        return;
    }

    String[] palabras = retroalimentacion.split("\\s+");
    int cantidadPalabras = palabras.length;

    if (cantidadPalabras < 3) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Retroalimentación insuficiente", "La retroalimentación debe contener al menos 3 palabras.");
        return;
    }

    if (cantidadPalabras > 100) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Retroalimentación demasiado larga", "La retroalimentación no debe exceder las 100 palabras.");
        return;
    }

    // ✅ Armar el mapa de criterios con sus puntajes
    Map<Integer, Integer> criteriosPuntaje = new HashMap<>();
    for (Map.Entry<Integer, ComboBox<Integer>> entry : mapComboPuntajes.entrySet()) {
        Integer idCriterio = entry.getKey();
        Integer puntaje = entry.getValue().getValue();
        criteriosPuntaje.put(idCriterio, puntaje);
    }

    try {
        ResultadoOperacion resultado = EvaluacionOvDAO.registrarEvaluacionOV(
            puntajeMax,
            puntajeMinAprob,
            puntajeTotal,
            retroalimentacion,
            idEstudiante,
            criteriosPuntaje
        );

        if (!resultado.isError()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", resultado.getMensaje());
            Utilidad.cerrarVentanaActual(lbNombreProyecto);
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", resultado.getMensaje());
        }

    } catch (SQLException e) {
        e.printStackTrace();
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al registrar la evaluación: " + e.getMessage());
    }
}


    /**
     * Calcula la suma total de los puntajes ingresados en los ComboBox.
     * Si algún criterio no está calificado, muestra alerta y retorna -1.
     *
     * @return La suma de puntajes o -1 si falta alguna calificación.
     */
    private double calcularPuntajeTotal() {
        double total = 0;
        for (Map.Entry<Integer, ComboBox<Integer>> entry : mapComboPuntajes.entrySet()) {
            ComboBox<Integer> cb = entry.getValue();
            Integer valor = cb.getValue();
            if (valor == null) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Debes calificar todos los criterios.");
                return -1;
            }
            total += valor;
        }
        return total;
    }

     /**
     * Evento que se activa al hacer clic en el botón "Cancelar".
     * Pregunta confirmación para cerrar la ventana actual.
     *
     * @param event Evento del clic.
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Cancelar evaluación", "¿Seguro que deseas cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(lbNombreProyecto);
        }
    }
    
}
