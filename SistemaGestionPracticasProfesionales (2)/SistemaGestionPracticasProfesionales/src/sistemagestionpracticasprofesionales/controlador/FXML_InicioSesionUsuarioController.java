    package sistemagestionpracticasprofesionales.controlador;

    import sistemagestionpracticasprofesionales.modelo.pojo.Usuario;
    import sistemagestionpracticasprofesionales.modelo.dao.InicioSesionUsuarioDAO;
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
import java.lang.reflect.InvocationTargetException;
    import sistemagestionpracticasprofesionales.SistemaGestionPracticasProfesionales;

    /**
     * FXML Controller class
     *
     * @author rodri
     */
    public class FXML_InicioSesionUsuarioController implements Initializable {

        @FXML
        private TextField tfUsuario;
        @FXML
        private PasswordField pfContrasenia;
        @FXML
        private Label lblErrorUsuario;
        @FXML
        private Label lblErrorContrasenia;

        /**
         * Initializes the controller class.
         */
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            // TODO
        }

        private boolean validarCampos(String username, String password) {
            lblErrorUsuario.setText(""); 
            lblErrorContrasenia.setText("");
            boolean camposValidos = true;
            if (username.isEmpty()) {
                lblErrorUsuario.setText("Usuario obligatorio");
                camposValidos = false;
            }
            if (password.isEmpty()){
                lblErrorContrasenia.setText("Contraseña obligatoria");
                camposValidos = false;
            }
            return camposValidos;
        }

        private void validarCredenciales(String username, String password) {
            try {
                Usuario usuarioSesion = InicioSesionUsuarioDAO.verificarCredenciales(username, password);
                if (usuarioSesion != null){
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales correctas", "Bienvenido(a) al sistema");
                    irPantallaPrincipal(usuarioSesion);
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Credenciales incorrectos", "Usuario y/o contraseñas incorrectas, por favor verifica tu información");
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Problemas de conexión", e.getMessage());
            }
        }
        
        private void irPantallaPrincipal(Usuario usuarioSesion) {
        if (usuarioSesion == null) {
            mostrarError("Error interno", "El usuario no ha sido encontrado en el sistema");
            return;
        }

        String rol = usuarioSesion.getRol();
        if (rol == null || rol.trim().isEmpty()) {
            mostrarError("Rol desconocido", "El rol del usuario no está definido correctamente.");
            return;
        }

        String fxmlPath;
        String titulo;

        switch (rol.toLowerCase().trim()) {
            case "coordinador":
                fxmlPath = "/sistemagestionpracticasprofesionales/vista/FXML_PrincipalCoordinador.fxml";
                titulo = "Principal coordinador";
                break;
            case "evaluador":
                fxmlPath = "/sistemagestionpracticasprofesionales/vista/FXML_PrincipalEvaluador.fxml";
                titulo = "Principal evaluador";
                break;
            case "profesor ee":
                fxmlPath = "/sistemagestionpracticasprofesionales/vista/FXML_PrincipalProfesorEE.fxml";
                titulo = "Principal profesor ee";
                break;
            default:
                mostrarError("Rol desconocido", "El rol del usuario no está definido correctamente.");
                return;
        }

        try {
            Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent vista = cargador.load();

            Object controlador = cargador.getController();
            controlador.getClass().getMethod("inicializarInformacion", Usuario.class).invoke(controlador, usuarioSesion);

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle(titulo);
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            mostrarError("Error al abrir vista", "No se pudo cargar la pantalla principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

        private void mostrarError(String titulo, String mensaje) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, titulo, mensaje);
}



        @FXML
        private void clickIniciarSesion(ActionEvent event) {
            String username = tfUsuario.getText();
            String password = pfContrasenia.getText();

            if (validarCampos(username, password)) {
                validarCredenciales(username, password);
            }
        }

        @FXML
        private void clickSoyEstudiante(ActionEvent event) {
            try {
                Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_InicioSesionEstudiante.fxml"));
                Parent vista = cargador.load();

                Scene escenaInicioEstudiante = new Scene(vista);
                escenarioBase.setScene(escenaInicioEstudiante);
                escenarioBase.setTitle("Inicio de Sesión - Estudiante");
                escenarioBase.centerOnScreen();
                escenarioBase.show();
            } catch (IOException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cambiar de vista", "No se pudo cargar la vista de inicio de sesión de usuario");
                e.printStackTrace();
            }
        }    
    }
