/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_SeleccionOrganizacionController implements Initializable {

    @FXML
    private TextField tfBuscarOV;
    @FXML
    private TableView<?> tvOrganizacionesVinculadas;
    @FXML
    private TableColumn<?, ?> nombreOV;
    @FXML
    private TableColumn<?, ?> direccionOV;
    @FXML
    private TableColumn<?, ?> correoOV;
    @FXML
    private TableColumn<?, ?> telefonoOV;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickRegresar(ActionEvent event) {
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
    }
    
}
