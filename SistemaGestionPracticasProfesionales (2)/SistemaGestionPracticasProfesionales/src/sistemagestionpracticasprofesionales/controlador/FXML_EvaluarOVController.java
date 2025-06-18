/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import sistemagestionpracticasprofesionales.modelo.dao.CriterioEvaluacionDAO;
import sistemagestionpracticasprofesionales.modelo.dao.EvaluacionOvDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.CriterioEvaluacion;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
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
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void inicializarDatos(Proyecto proyecto) {
    if (proyecto != null) {
        this.proyecto = proyecto;
        System.out.println("Proyecto recibido: " + proyecto.getNombre() + ", idResponsable: " + proyecto.getIdResponsableProyecto());
        lbNombreProyecto.setText(proyecto.getNombre());
        lbNombreOrganizacionVinculada.setText(proyecto.getNombreOrganizacion());
        cargarCriterios(); 
    }
}



    private void cargarCriterios() {
        criterios = CriterioEvaluacionDAO.obtenerCriterios();
        vBoxCriteriosEvaluacion.getChildren().clear();
        mapComboPuntajes.clear();

        for (CriterioEvaluacion criterio : criterios) {
            HBox hbox = new HBox(20); // separacion 20px
            hbox.setPrefWidth(1020);
            hbox.setMaxWidth(1020);

            VBox vboxTextos = new VBox(3);
            Label lblNombre = new Label(criterio.getNombreCriterio());
            lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
            Label lblDescripcion = new Label(criterio.getDescripcion());
            lblDescripcion.setWrapText(true);

            vboxTextos.getChildren().addAll(lblNombre, lblDescripcion);

            // Que el VBox ocupe 85% del ancho total
            vboxTextos.setPrefWidth(1020 * 0.85);
            vboxTextos.setMaxWidth(1020 * 0.85);

            ComboBox<Integer> cbPuntaje = new ComboBox<>();
            cbPuntaje.getItems().addAll(1, 2, 3, 4, 5);
            cbPuntaje.setPromptText("Puntaje");
            cbPuntaje.setPrefWidth(120); // ancho fijo para mostrar el prompt completo

            hbox.getChildren().addAll(vboxTextos, cbPuntaje);

            vBoxCriteriosEvaluacion.getChildren().add(hbox);

            mapComboPuntajes.put(criterio.getIdCriterio(), cbPuntaje);
        }
    }




    @FXML
    private void clickAceptar(ActionEvent event) {
        if (proyecto == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No hay proyecto seleccionado.");
            return;
        }

        // Obtener el idEstudiante responsable (según diseño: idResponsableProyecto)
        int idEstudiante = Sesion.getEstudianteSeleccionado().getIdEstudiante();

        // Obtener el idExpediente asociado al estudiante
        int idExpediente = ExpedienteDAO.obtenerIdExpedientePorEstudiante(idEstudiante);
        if (idExpediente == -1) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se encontró expediente para el estudiante.");
            return;
        }

        // Calcular puntaje total, validando que todos estén calificados
        double puntajeTotal = calcularPuntajeTotal();
        if (puntajeTotal == -1) {
            // Ya mostró alerta en calcularPuntajeTotal, solo salir
            return;
        }

        // Calcular puntaje máximo y mínimo para aprobar dinámicamente
        double puntajeMax = criterios.size() * 5; // 5 es máximo por criterio
        double puntajeMinAprob = puntajeMax * 0.6; // Ejemplo: 60% para aprobar

        String retroalimentacion = taRetroalimentacion.getText().trim();

        // Validación de retroalimentación
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


        // Guardar la evaluación mediante DAO
        try {
            boolean exito = EvaluacionOvDAO.guardarEvaluacionOV(puntajeMax, puntajeMinAprob, puntajeTotal, retroalimentacion, idExpediente);
            if (exito) {
                ExpedienteDAO.actualizarEstadoEvaluacionOV(idExpediente);
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Evaluación guardada correctamente.");
                Utilidad.cerrarVentanaActual(lbNombreProyecto);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo guardar la evaluación.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al guardar la evaluación: " + e.getMessage());
        }
    }

    /**
     * Suma todos los puntajes seleccionados en los ComboBox.
     * Si alguno no está calificado, muestra alerta y devuelve -1.
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

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Cancelar evaluación", "¿Seguro que deseas cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(lbNombreProyecto);
        }
    }
    
}
