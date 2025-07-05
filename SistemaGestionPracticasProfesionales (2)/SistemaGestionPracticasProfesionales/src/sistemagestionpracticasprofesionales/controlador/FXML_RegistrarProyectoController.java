/**
    * Nombre del archivo: FXML_RegistrarProyectoController.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 22/06/2025
    * Descripción: Controlador para la vista de registro de proyectos.
    * Permite validar y guardar un nuevo proyecto en el sistema.
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.IOException;
import sistemagestionpracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.Proyecto;
import sistemagestionpracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemagestionpracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemagestionpracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;
import sistemagestionpracticasprofesionales.interfaz.INotificacion;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rodri
 * Controlador para la vista FXML_RegistrarProyecto, permite registrar nuevos proyectos
 */
public class FXML_RegistrarProyectoController implements Initializable {

    @FXML
    private TextField tfNombreProyecto;
    @FXML
    private ComboBox<OrganizacionVinculada> cbListaOVs;
    @FXML
    private ComboBox<ResponsableProyecto> cbListaResponsables;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;
    @FXML
    private TextField tfParticipantes;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private Label lbErrorNombre;
    @FXML
    private Label lbErrorOV;
    @FXML
    private Label lbErrorResponsable;
    @FXML
    private Label lbErrorFechaInicio;
    @FXML
    private Label lbErrorFechaFin;
    @FXML
    private Label lbErrorParticipantes;
    @FXML
    private Label lbErrorDescripcion;
    
    INotificacion observador;
    
    /**
     * Initializes the controller class.
     * Inicializa el controlador, carga los datos iniciales en el combobox de organizacion vinculada
     * y establece restricciones y formatos para cuando se ingresan fechas, horas y cantidades numéricas
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarOrganizacionesVinculadas();
        
        dpFechaInicio.valueProperty().addListener((obs, oldValue, nuevaFechaInicio) -> {
            if (nuevaFechaInicio != null) {
                dpFechaFin.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        // Deshabilita fechas anteriores a la fecha de inicio
                        setDisable(empty || date.isBefore(nuevaFechaInicio));
                    }
                });
            }
        });
        
        tfParticipantes.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfParticipantes.setText(oldValue); // Revierte el cambio si no es un número entero
            }
        });
    }
    
    /**
     * Carga la lista completa de organizaciones vinculadas
     */
    private void cargarOrganizacionesVinculadas() {
        try {
            ArrayList<OrganizacionVinculada> listaOVs = OrganizacionVinculadaDAO.obtenerOrganizacionesVinculadas();
            cbListaOVs.getItems().addAll(listaOVs);
            
            cbListaOVs.setConverter(new StringConverter<OrganizacionVinculada>() {
                @Override
                public String toString(OrganizacionVinculada organizacion) {
                    return organizacion != null ? organizacion.getNombre() : "";
                }
                
                @Override
                public OrganizacionVinculada fromString(String string) {
                    return null; // Obligatorio aunque no se usa porque el ComboBox no es editable
                }
            });
        } catch (SQLException e) {
            System.err.println("Error al cargar organizaciones: " + e.getMessage());
        }
    }
    
    /**
     * Carga los responsables disponibles sin proyecto de la organización vinculada seleccionada
     * al hacer clic en el ComboBox de responsables.
     * @param event 
     */
    @FXML
    private void clickSeleccionarResponsable(MouseEvent event) {
        OrganizacionVinculada ovSeleccionada = cbListaOVs.getValue();
        if (ovSeleccionada != null) {
            cbListaResponsables.getItems().clear();
            cargarResponsables(ovSeleccionada.getIdOrganizacionVinculada());
        }
    }
    
    /**
     * Limpia la lista de responsables cuando se cambia la organización vinculada seleccionada.
     * @param event 
     */
    @FXML
    private void clickSeleccionarOV(MouseEvent event) {
        cbListaResponsables.getItems().clear();
    }
    
    /**
     * Carga la lista completa de responsables de proyecto pertenecientes a la organización vinculada especificada
     */
    private void cargarResponsables(int idOrganizacionVinculada) {
        try {
            ArrayList<ResponsableProyecto> listaResponsables = ResponsableProyectoDAO.obtenerResponsableSinProyectoDeUnaOV(idOrganizacionVinculada);
            cbListaResponsables.getItems().addAll(listaResponsables);
            
            cbListaResponsables.setConverter(new StringConverter<ResponsableProyecto>() {
                @Override
                public String toString(ResponsableProyecto responsable) {
                    return responsable != null ? responsable.getNombre() : "";
                }
                
                @Override
                public ResponsableProyecto fromString(String string) {
                    return null; // Obligatorio aunque no se usa porque el ComboBox no es editable
                }
            });
        } catch (SQLException e) {
            System.err.println("Error al cargar a los responsables: " + e.getMessage());
        }
    }
    
    /**
     * Ejecuta el proceso de guardar el nuevo proyecto
     * @param event 
     */
    @FXML
    private void clickAceptar(ActionEvent event) {
        guardarProyecto();
    }

    /**
     * Intenta guardar el proyecto en la base de datos después de validar los campos.
     */
    private void guardarProyecto() {
        if (!validarCampos()) {
            return;
        }
        
        Proyecto nuevoProyecto = new Proyecto(
                0,
                tfNombreProyecto.getText().trim(),
                cbListaOVs.getValue().getIdOrganizacionVinculada(),
                cbListaResponsables.getValue().getIdResponsable(),
                dpFechaInicio.getValue().toString().trim(),
                dpFechaFin.getValue().toString().trim(),
                Integer.parseInt(tfParticipantes.getText().trim()),
                tfDescripcion.getText().trim()
        );
        
        try {
            ResultadoOperacion resultado = ProyectoDAO.registrarProyecto(nuevoProyecto);
            
            if (!resultado.isError()) {
               
                try {
                    Stage ventanaActual = (Stage) tfNombreProyecto.getScene().getWindow(); // ← guarda la ventana actual

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionpracticasprofesionales/vista/FXML_HorarioProyecto.fxml"));
                    Parent vista = loader.load();

                    FXML_HorarioProyectoController controlador = loader.getController();
                    controlador.inicializarDatos(nuevoProyecto, ventanaActual); // ← pásale el Stage actual

                    Stage nuevaVentana = new Stage();
                    nuevaVentana.setScene(new Scene(vista));
                    nuevaVentana.setTitle("Horario proyecto");
                    nuevaVentana.initModality(Modality.APPLICATION_MODAL); // o NONE si quieres permitir interacción con otras
                    nuevaVentana.show();
                } catch (IOException e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana para evaluar estudiante");
                }
                
                if (observador != null) {
                    observador.operacionExitosa("Insertar", nuevoProyecto.getNombre());
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al registrar", resultado.getMensaje());
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexión", "No se pudo guardar el proyecto: " + e.getMessage());
        }
    }
    
    /**
     * Valida que los campos por introducir no estén vacíos ni sean nulos
     * @return camposValidos;
     */
    private boolean validarCampos() {
        String nombreProyecto = tfNombreProyecto.getText().trim();
        OrganizacionVinculada ovSeleccionada = cbListaOVs.getValue();
        ResponsableProyecto responsableSeleccionado = cbListaResponsables.getValue();
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();
        String participantes = tfParticipantes.getText().trim();
        String descripcion = tfDescripcion.getText().trim();

        lbErrorNombre.setText("");
        lbErrorOV.setText("");
        lbErrorResponsable.setText("");
        lbErrorFechaInicio.setText("");
        lbErrorFechaFin.setText("");
        lbErrorParticipantes.setText("");
        lbErrorDescripcion.setText("");

        boolean camposValidos = true;

        if (nombreProyecto.isEmpty()) {
            lbErrorNombre.setText("Nombre obligatorio");
            camposValidos = false;
        } else if (nombreProyecto.length() < 6) {
            lbErrorNombre.setText("Debe tener mínimo 6 caracteres de longitud");
            camposValidos = false;
        } else if (nombreProyecto.length() > 45) {
            lbErrorNombre.setText("Debe tener máximo 45 caracteres de longitud");
            camposValidos = false;
        }
        if (ovSeleccionada == null) {
            lbErrorOV.setText("Organización vinculada obligatoria");
            camposValidos = false;
        }
        if (responsableSeleccionado == null) {
            lbErrorResponsable.setText("Responsable del proyecto obligatorio");
            camposValidos = false;
        }
        if (fechaInicio == null) {
            lbErrorFechaInicio.setText("Fecha de inicio obligatoria");
            camposValidos = false;
        }
        if (fechaFin == null) {
            lbErrorFechaFin.setText("Fecha de fin obligatoria");
            camposValidos = false;
        } else if (fechaInicio != null && fechaFin.isBefore(fechaInicio.plusMonths(3))) {
            lbErrorFechaFin.setText("El proyecto no puede durar menos de 3 meses");
            camposValidos = false;
        }
        
        if (participantes.isEmpty()) {
            lbErrorParticipantes.setText("Número de participantes obligatorio");
            camposValidos = false;
        } else {
            try {
                int num = Integer.parseInt(participantes);
                if (num == 0) {
                    lbErrorParticipantes.setText("Debe haber mínimo 1 participante");
                    camposValidos = false;
                } else if (num > 5) {
                    lbErrorParticipantes.setText("Debe haber máximo 5 participantes");
                    camposValidos = false;
                }
            } catch (NumberFormatException e) {
                lbErrorParticipantes.setText("Debe ser un número entero");
                camposValidos = false;
            }
        }

        if (descripcion.isEmpty()) {
            lbErrorDescripcion.setText("Descripción del proyecto obligatoria");
            camposValidos = false;
        } else if (descripcion.length() < 10) {
            lbErrorDescripcion.setText("Debe contener mínimo 10 caracteres");
            camposValidos = false;
        } else if (descripcion.length() > 255) {
            lbErrorDescripcion.setText("Debe contener máximo 250 caracteres");
            camposValidos = false;
        }

        return camposValidos;
    }
    
    /**
     * Cierra la ventana si el usuario lo confirma
     * @param event 
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("SeguroCancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(cbListaOVs);
        }
    }
    
}