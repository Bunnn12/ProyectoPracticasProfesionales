package sistemagestionpracticasprofesionales.controlador;

import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.dao.InicioSesionEstudianteDAO;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.io.IOException;
import sistemagestionpracticasprofesionales.SistemaGestionPracticasProfesionales;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;

/**
 * FXML Controller class
 *
 * @author rodri
 */
public class FXML_InicioSesionEstudianteController implements Initializable {

    @FXML
    private TextField tfMatricula;
    @FXML
    private PasswordField pfContrasenia;
    @FXML
    private Label lblErrorMatricula;
    @FXML
    private Label lblErrorContrasenia;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private boolean validarCampos(String matricula, String password) {
        lblErrorMatricula.setText(""); 
        lblErrorContrasenia.setText("");
        boolean camposValidos = true;
        if (matricula.isEmpty()) {
            lblErrorMatricula.setText("Matricula obligatoria");
            camposValidos = false;
        }
        if (password.isEmpty()){
            lblErrorContrasenia.setText("Contraseña obligatoria");
            camposValidos = false;
        }
        return camposValidos;
    }
    
    private void validarCredenciales(String matricula, String password) {
        try {
            Estudiante estudianteSesion = InicioSesionEstudianteDAO.verificarCredenciales(matricula, password);
            if (estudianteSesion != null){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales correctas", "Bienvenido(a) al sistema");
                irPantallaPrincipal(estudianteSesion);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Credenciales incorrectos", "Usuario y/o contraseñas incorrectas, por favor verifica tu información");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Problemas de conexión", e.getMessage());
        }
    }
    
    private void irPantallaPrincipal(Estudiante estudianteSesion) {
        if (estudianteSesion == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error interno", "El usuario no ha sido encontrado en el sistema");
            return;
        }
        Sesion.setEstudianteSeleccionado(estudianteSesion);
        try{
            Stage escenarioBase = (Stage) tfMatricula.getScene().getWindow(); 
            FXMLLoader cargador= new FXMLLoader(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_PrincipalEstudiante.fxml"));
            Parent vista= cargador.load();
            FXML_PrincipalEstudianteController controlador= cargador.getController();
            controlador.inicializarInformacion(estudianteSesion);
            Scene escenaPrincipal= new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Principal Estudiante");
            escenarioBase.centerOnScreen();
            escenarioBase.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void clickIniciarSesion(ActionEvent event) {
        String matricula = tfMatricula.getText();
        String password = pfContrasenia.getText();
        
        if (validarCampos(matricula, password)) {
            validarCredenciales(matricula, password);
        }
    }
    
    @FXML
    private void clickNoSoyEstudiante(ActionEvent event) {
        try{
            Stage escenarioBase= (Stage) tfMatricula.getScene().getWindow();
            FXMLLoader cargador= new FXMLLoader(SistemaGestionPracticasProfesionales.class.getResource("vista/FXML_InicioSesionUsuario.fxml"));
            Parent vista= cargador.load();
            Scene escenaInicio = new Scene(vista);
            escenarioBase.setScene(escenaInicio);
            escenarioBase.setTitle("InicioSesion");
            escenarioBase.centerOnScreen();
        }catch(IOException e){
            e.getMessage();
            e.printStackTrace();
        }
    }

    
}
