/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sistemagestionpracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_RegistrarResponsableController implements Initializable {

    @FXML
    private TextField tfNombreResponsable;
    @FXML
    private TextField tfCorreoResponsable;
    @FXML
    private TextField tfTelefonoResponsable;
    @FXML
    private TableView<OrganizacionVinculada> tvOrganizacionesVinculadas;
    @FXML
    private TableColumn nombreOV;
    @FXML
    private TableColumn direccionOV;
    @FXML
    private TableColumn correoOV;
    @FXML
    private TableColumn telefonoOV;
    @FXML
    private TextField tfBuscarOV;
    @FXML
    private Label lbErrorNombre;
    @FXML
    private Label lbErrorCorreo;
    @FXML
    private Label lbErrorTelefono;
    private ObservableList<OrganizacionVinculada> organizacionesVinculadas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickRegresar(ActionEvent event) {
        Utilidad.cerrarVentanaActual(tfNombreResponsable);
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
    }
    
    private boolean validarCampos(){
        String nombre= tfNombreResponsable.getText();
        String telefono= tfTelefonoResponsable.getText();
        String correo= tfCorreoResponsable.getText();
        lbErrorCorreo.setText(""); 
        lbErrorNombre.setText("");
        lbErrorTelefono.setText("");
        boolean camposValidos = true;
        if(nombre.isEmpty()){
            lbErrorNombre.setText("Nombre obligatorio");
            camposValidos= false;
        }
        if(telefono.isEmpty()){
            lbErrorTelefono.setText("Telefono obligatorio");
            camposValidos= false;
        }
        if(correo.isEmpty()){
            lbErrorCorreo.setText("Correo obligatorio");
            camposValidos= false;
        }
        return camposValidos;
    }
}
