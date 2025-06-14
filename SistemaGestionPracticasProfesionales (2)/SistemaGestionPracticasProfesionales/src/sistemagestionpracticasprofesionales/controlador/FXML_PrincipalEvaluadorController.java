/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sistemagestionpracticasprofesionales.modelo.pojo.Usuario;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_PrincipalEvaluadorController implements Initializable {
Usuario usuarioSesion;
    @FXML
    private Label lbSaludoUsuario;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
        
    public void inicializarInformacion(Usuario usuarioSesion){
        this.usuarioSesion = usuarioSesion;
        cargarInformacionUsuario();
    }
    
    private void cargarInformacionUsuario(){
        if (usuarioSesion != null){
            lbSaludoUsuario.setText("Hola, " + usuarioSesion.getNombre());
        }
    }


    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Confirmar Cerrar sesión", "¿Estás seguro de que quieres cerrar sesión?");
        
        if (confirmado) {
        Utilidad.cerrarVentanaActual(lbSaludoUsuario);
        try {
            System.out.println("Cargando FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/sistemagestionpracticasprofesionales/vista/FXML_InicioSesionUsuario.fxml"));
            Parent vista = loader.load();

            Stage nuevaVentana = new Stage();
            Scene escenaInicioSesion = new Scene(vista);
            nuevaVentana.setScene(escenaInicioSesion);
            nuevaVentana.setTitle("Inicio de Sesión");
            nuevaVentana.centerOnScreen();
            nuevaVentana.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana de inicio de sesión: " + e.getMessage());
            }
        }
    }

    @FXML
    private void clickEvaluarEstudiante(ActionEvent event) {
    }
}
