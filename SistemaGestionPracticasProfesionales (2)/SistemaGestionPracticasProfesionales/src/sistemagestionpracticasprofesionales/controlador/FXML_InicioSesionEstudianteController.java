/**
 * Nombre del archivo: FXML_InicioSesionEstudianteController.java
 * Autor: Rodrigo Santa Bárbara Murrieta
 * Fecha: 08/06/2025
 * Descripción: Controlador para la pantalla de inicio de sesión del estudiante.
 * Permite validar los campos de usuario y contraseña, cifrar la contraseña
 * usando SHA-256, verificar credenciales y navegar a la pantalla principal del estudiante
 * o a la pantalla de inicio de sesión para otros usuarios.
 */

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sistemagestionpracticasprofesionales.SistemaGestionPracticasProfesionales;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;

/**
 * Controlador para la vista FXML_InicioSesionEstudiante.
 * Gestiona el proceso de inicio de sesión del estudiante, validando campos,
 * autenticando credenciales contra la base de datos y redirigiendo a la pantalla principal si el acceso es exitoso.
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
     * Inicializa el controlador.
     * 
     * @param url URL de localización del archivo FXML.
     * @param rb ResourceBundle con recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    /**
     * Valida que los campos matrícula y contraseña no estén vacíos y la contraseña tenga 5
     * o más caracteres.
     * En caso de que algún campo esté vacío, la contraseña no  o la matrícula no cumpla el formato válido,
     * se muestran los mensajes de error correspondientes en las etiquetas lblErrorMatricula y lblErrorContrasenia.
     * 
     * @param matricula Matrícula ingresada.
     * @param password Contraseña ingresada.
     * @return true si ambos campos son válidos, false en caso contrario.
     */
    private boolean validarCampos(String matricula, String password) {
        lblErrorMatricula.setText(""); 
        lblErrorContrasenia.setText("");
        boolean camposValidos = true;

        if (matricula.isEmpty()) {
            lblErrorMatricula.setText("Matrícula obligatoria");
            camposValidos = false;
        } else if (!validarMatricula(matricula)) {
            lblErrorMatricula.setText("Matrícula no válida");
            camposValidos = false;
        }

        if (password.isEmpty()) {
            lblErrorContrasenia.setText("Contraseña obligatoria");
            camposValidos = false;
        } else if (password.length() < 5) {
            lblErrorContrasenia.setText("Debe tener al menos 5 caracteres");
            camposValidos = false;
        }

        return camposValidos;
    }


    /**
     * Verifica las credenciales del estudiante usando matrícula y contraseña hasheada.
     *
     * @param matricula Matrícula ingresada.
     * @param passwordHasheada Contraseña ya cifrada con SHA-256.
     */
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
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
        }
    }
    
    /**
     * Valida que la matrícula cumpla con el formato:
     * - Comienza con 'S'
     * - Sigue con dos dígitos para el año (por ejemplo, 20, 23)
     * - Longitud exacta de 9 caracteres (ejemplo: S23014092)
     * 
     * @param matricula la matrícula a validar
     * @return true si es válida, false en caso contrario
     */
   private boolean validarMatricula(String matricula) {
        if (matricula == null || matricula.length() != 9) {
            return false;
        }
        if (!matricula.startsWith("S")) {
            return false;
        }
        String anioIngreso = matricula.substring(1, 3);
        if (!anioIngreso.matches("\\d{2}")) {
            return false;
        }
        int anio = Integer.parseInt(anioIngreso);
        if (anio < 0 || anio > 25) {
            return false;
        }
        return true;
    }

   /**
     * Cifra una contraseña usando el algoritmo SHA-256.
     *
     * @param contrasenia Contraseña en texto plano.
     * @return Cadena hexadecimal representando el hash.
     */
    private String hashContrasenia(String contrasenia) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(contrasenia.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error al hashear la contraseña: " + e.getMessage());
            return null;
        }
    }
    /**
     * Cambia a la pantalla principal del estudiante.
     * 
     * @param estudianteSesion Estudiante que ha iniciado sesión.
     */
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
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de carga", "No se pudo cargar la vista principal");
        } 
    }
    
    /**
     * Maneja el evento del botón "Iniciar sesión".
     * Valida campos, cifra la contraseña y verifica credenciales.
     *
     * @param event Evento del botón iniciar sesión.
     */
    @FXML
    private void clickIniciarSesion(ActionEvent event) {
        String matricula = tfMatricula.getText();
        String password = pfContrasenia.getText();

        if (validarCampos(matricula, password)) {
            String passwordHasheada = hashContrasenia(password);
            if (passwordHasheada != null) {
                validarCredenciales(matricula, passwordHasheada);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo procesar la contraseña");
            }
        }
    }
    
    /**
     * Maneja el evento del botón "No soy estudiante" para cambiar a la pantalla de inicio de sesión de usuario.
     * 
     * @param event Evento del botón Soy Estudiante
     */
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
        }
    }
}
