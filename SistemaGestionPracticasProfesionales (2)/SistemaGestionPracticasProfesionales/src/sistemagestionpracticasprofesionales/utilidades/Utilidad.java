package sistemagestionpracticasprofesionales.utilidades;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;

/**
 *
 * @author rodri
 */
public class Utilidad {
    public static void mostrarAlertaSimple(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    public static void cerrarVentanaActual(Node etiquetaStage){
        Stage stageActual = (Stage) etiquetaStage.getScene().getWindow();
        stageActual.close();
    }
    public static Stage obtenerEscenarioComponente(Control componente){
        return (Stage) componente.getScene().getWindow();
    }
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido){
        Alert alertaConfirmacion= new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setHeaderText(null);
        alertaConfirmacion.setContentText(contenido);
        Optional<ButtonType> seleccion= alertaConfirmacion.showAndWait();
        return seleccion.get() == ButtonType.OK;
    }

}

