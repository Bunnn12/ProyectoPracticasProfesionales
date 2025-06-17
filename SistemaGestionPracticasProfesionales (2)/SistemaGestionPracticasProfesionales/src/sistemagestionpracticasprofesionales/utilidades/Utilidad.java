/*
 * Nombre del archivo: Utilidad.java
 * Autor: Rodrigo Santa Bárbara Murrieta
 * Fecha: 08/06/2025
 * Descripción: Clase con métodos utilitarios para mostrar alertas y manejar ventanas
 * en aplicaciones JavaFX, facilitando la interacción con la interfaz gráfica.
 */

package sistemagestionpracticasprofesionales.utilidades;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;

/**
 * Métodos estáticos para mostrar alertas, cerrar ventanas y obtener etapas
 * en aplicaciones JavaFX.
 */
public class Utilidad {
    
    /**
     * Muestra una alerta simple con el tipo, título y contenido especificados.
     *
     * @param tipo Tipo de alerta (INFORMATION, WARNING, ERROR, etc.).
     * @param titulo Título de la ventana de la alerta.
     * @param contenido Mensaje que se mostrará en el cuerpo de la alerta.
     */
    public static void mostrarAlertaSimple(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    
    /**
     * Cierra la ventana actual a partir de un componente de la interfaz.
     * 
     * @param componente Componente JavaFX ubicado dentro de la ventana a cerrar.
     */
    public static void cerrarVentanaActual(Node componente) {
        if (componente != null && componente.getScene() != null && componente.getScene().getWindow() != null) {
            Stage escenario = (Stage) componente.getScene().getWindow();
            escenario.close();
        } 
    }
    
    /**
     * Obtiene el Stage (ventana) asociado a un componente Control de JavaFX.
     * 
     * @param componente Componente Control del cual obtener la ventana.
     * @return El Stage asociado a dicho componente.
     */
    public static Stage obtenerEscenarioComponente(Control componente){
        return (Stage) componente.getScene().getWindow();
    }
    
    /**
     * Muestra una alerta de confirmación con un título y mensaje especificados.
     * Espera la respuesta del usuario y devuelve true si confirma (OK).
     * 
     * @param titulo Título de la ventana de confirmación.
     * @param contenido Mensaje a mostrar en la alerta.
     * @return true si el usuario confirma, false en otro caso.
     */
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido){
        Alert alertaConfirmacion= new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setHeaderText(null);
        alertaConfirmacion.setContentText(contenido);
        Optional<ButtonType> seleccion= alertaConfirmacion.showAndWait();
        return seleccion.get() == ButtonType.OK;
    }

}

