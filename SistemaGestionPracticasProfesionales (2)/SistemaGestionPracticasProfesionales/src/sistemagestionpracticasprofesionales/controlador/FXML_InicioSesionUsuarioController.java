/**
 * Nombre del archivo: FXML_InicioSesionUsuarioController.java
 * Autor: Rodrigo Santa Bárbara Murrieta
 * Fecha: 08/06/2025
 * Descripción: Controlador para la pantalla de inicio de sesión del usuario.
 * Permite validar los campos de usuario y contraseña, verificar credenciales
 * y navegar a la pantalla principal correspondiente según el rol del usuario.
 */    
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
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;

    /**
    * Controlador para la vista FXML_InicioSesionUsuario.
    * Gestiona el proceso de inicio de sesión del usuario general, validando campos,
    * autenticando credenciales contra la base de datos y redirigiendo a la pantalla principal según el rol.
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
     * Inicializa el controlador.
     * 
     * @param url URL de localización del archivo FXML.
     * @param rb ResourceBundle con recursos internacionalizados.
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Valida que los campos usuario y contraseña no estén vacíos.
     * Muestra mensajes de error en caso contrario.
     * 
     * @param username Nombre de usuario ingresado.
     * @param password Contraseña ingresada.
     * @return true si ambos campos son válidos, false en caso contrario.
     */
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

    /**
     * Valida las credenciales del usuario con la base de datos.
     * 
     * @param username Nombre de usuario ingresado.
     * @param password Contraseña ingresada.
     */
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
        
    /**
     * Navega a la pantalla principal correspondiente al rol del usuario.
     * 
     * @param usuarioSesion Usuario que ha iniciado sesión.
     */
        private void irPantallaPrincipal(Usuario usuarioSesion) {
        if (usuarioSesion == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Rol desconocido", "El rol del usuario no está definido correctamente");
            return;
        }
        Sesion.setUsuarioSeleccionado(usuarioSesion);
        String rol = usuarioSesion.getRol();
        if (rol == null || rol.trim().isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Rol desconocido", "El rol del usuario no está definido correctamente");
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
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Rol desconocido", "El rol del usuario no está definido correctamente");
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
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al abrir vista", "No se pudo cargar la pantalla principal: " + e.getMessage());
        }
    }

    /**
     * Evento al dar clic en el botón de iniciar sesión.
     * Valida campos y credenciales.
     * 
     * @param event Evento del botón iniciar sesión.
     */    
        @FXML
        private void clickIniciarSesion(ActionEvent event) {
            String username = tfUsuario.getText();
            String password = pfContrasenia.getText();

            if (validarCampos(username, password)) {
                validarCredenciales(username, password);
            }
        }

    /**
     * Evento para cambiar a la pantalla de inicio de sesión para estudiantes
     * 
     * @param event Evento del botón "Soy estudiante".
     */    
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
            }
        }    
    }
