/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemagestionpracticasprofesionales.controlador;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import sistemagestionpracticasprofesionales.modelo.dao.EntregaDocumentoDAO;
import sistemagestionpracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemagestionpracticasprofesionales.modelo.pojo.DocumentoAnexo;
import sistemagestionpracticasprofesionales.modelo.pojo.Estudiante;
import sistemagestionpracticasprofesionales.modelo.pojo.Sesion;
import sistemagestionpracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXML_EntregaDocInicialesController implements Initializable {

    @FXML
    private Label lbFechaMaximaEntrega;
    @FXML
    private Label lbNombreDoc1;
    @FXML
    private Label lbNombreDoc2;
    @FXML
    private Label lbNombreDoc3;
    @FXML
    private Label lbNombreDoc4;
    
    private List<File> archivosSeleccionados;
    private List<Integer> documentosGuardadosIds; 
    private List<String> documentosGuardadosNombres; 
    private Estudiante estudianteSesion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        estudianteSesion = Sesion.getEstudianteSeleccionado();
        archivosSeleccionados = new ArrayList<>();
        documentosGuardadosIds = new ArrayList<>();
        documentosGuardadosNombres = new ArrayList<>();

        if (estudianteSesion != null) {
            mostrarFechaMaximaEntrega();
            cargarDocumentosGuardados();  
            mostrarNombresArchivos();
        } else {
            lbFechaMaximaEntrega.setText("Estudiante no identificado");
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo obtener información del estudiante en sesión");
        }
        System.out.println("Estudiante sesión: " + estudianteSesion.getIdEstudiante());
        int idExpediente = ExpedienteDAO.obtenerIdExpedientePorEstudiante(estudianteSesion.getIdEstudiante());
        System.out.println("idExpediente obtenido: " + idExpediente);

        List<DocumentoAnexo> documentos = EntregaDocumentoDAO.obtenerDocumentosInicialesPorExpediente(idExpediente);
        System.out.println("Documentos encontrados: " + documentos.size());
        for (DocumentoAnexo doc : documentos) {
            System.out.println("Doc: " + doc.getNombre() + " id: " + doc.getIdDocumentoAnexo());
        }

    }    

    private void mostrarFechaMaximaEntrega() {
        LocalDate fechaMaxima = EntregaDocumentoDAO.obtenerFechaMaximaEntregaInicial();
        if (fechaMaxima != null) {
            lbFechaMaximaEntrega.setText("Fecha límite: " + fechaMaxima.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            lbFechaMaximaEntrega.setText("Fecha límite: No disponible");
        }
    }
    
    private void cargarDocumentosGuardados() {
        int idExpediente = ExpedienteDAO.obtenerIdExpedientePorEstudiante(estudianteSesion.getIdEstudiante());
        if (idExpediente == -1) return;

        List<DocumentoAnexo> documentos = EntregaDocumentoDAO.obtenerDocumentosInicialesPorExpediente(idExpediente);
        documentosGuardadosIds.clear();
        documentosGuardadosNombres.clear();

        for (DocumentoAnexo doc : documentos) {
            documentosGuardadosIds.add(doc.getIdDocumentoAnexo());
            documentosGuardadosNombres.add(doc.getNombre());
        }

        // Aseguramos que archivosSeleccionados tenga siempre 4 posiciones (máximo documentos)
        archivosSeleccionados.clear();
        int totalSlots = 4; // Máximo documentos permitidos
        for (int i = 0; i < totalSlots; i++) {
            if (i < documentosGuardadosNombres.size()) {
                archivosSeleccionados.add(null); // Documento guardado sin archivo local
            } else {
                archivosSeleccionados.add(null); // Posición vacía
            }
        }
    }

    @FXML
    private void clickAceptar(ActionEvent event) {
        if (archivosSeleccionados.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Aviso", "Debes seleccionar al menos un documento.");
            return;
        }

        boolean exito = subirArchivos(archivosSeleccionados);
        if (exito) {
            cargarDocumentosGuardados();
            mostrarNombresArchivos(); 

            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Entrega Exitosa", "Los documentos han sido entregados exitosamente");
            Utilidad.cerrarVentanaActual(lbFechaMaximaEntrega);
        }else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Ocurrió un error al subir los documentos");
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarAlertaConfirmacion("Seguro Cancelar", "¿Estás seguro que quieres cancelar?");
        if (confirmado) {
            Utilidad.cerrarVentanaActual(lbFechaMaximaEntrega);
        } 
    }

    @FXML
    private void clickSubirDocumento(ActionEvent event) {
        if (!EntregaDocumentoDAO.estaDentroDeRangoEntregaInicial()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "No está dentro del periodo de entrega permitido.");
            return;
        }
        
        long totalSeleccionados = archivosSeleccionados.stream().filter(f -> f != null).count();
        if ((documentosGuardadosIds.size() + totalSeleccionados) >= 4) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Límite alcanzado", "Ya tienes 4 documentos cargados. No puedes agregar más.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );

        List<File> archivos = fileChooser.showOpenMultipleDialog(Utilidad.obtenerEscenarioComponente(lbNombreDoc1));
        if (archivos != null && !archivos.isEmpty()) {
           int disponibles = 4 - (int) archivosSeleccionados.stream().filter(f -> f != null).count();

            if (archivos.size() > disponibles) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Aviso",
                        "Solo puedes seleccionar " + disponibles + " archivo(s) más.");
                archivos = archivos.subList(0, disponibles);
            }

            for (File archivo : archivos) {
                if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Archivo inválido", "Solo se permiten archivos PDF");
                    continue;
                }

                if (archivo.length() > (5 * 1024 * 1024)) { 
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Archivo demasiado grande", 
                            "El archivo " + archivo.getName() + " excede los 5 MB");
                    continue;
                }
                
                boolean yaSubido = documentosGuardadosNombres.stream().anyMatch(nombre -> nombre.equalsIgnoreCase(archivo.getName()));
                if (yaSubido) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Archivo repetido",
                        "Ya existe un archivo con el nombre: " + archivo.getName() + " en tu expediente.");
                    continue;
                }
                boolean duplicado = archivosSeleccionados.stream()
                        .filter(f -> f != null)
                        .anyMatch(existing -> existing.getName().equalsIgnoreCase(archivo.getName()));
                if (duplicado) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Archivo duplicado",
                            "Ya seleccionaste un archivo llamado: " + archivo.getName());
                    continue;
                }

                int idxReemplazo = encontrarIndiceReemplazo();
                if (idxReemplazo != -1) {
                    while (archivosSeleccionados.size() <= idxReemplazo) {
                        archivosSeleccionados.add(null);
                    }
                    archivosSeleccionados.set(idxReemplazo, archivo);
                    borrarDocumentoGuardado(idxReemplazo);
                } else if (archivosSeleccionados.size() < 4) {
                    archivosSeleccionados.add(archivo);
                }
            }

            mostrarNombresArchivos();
        }
    }

    private int encontrarIndiceReemplazo() {
        for (int i = documentosGuardadosIds.size(); i < archivosSeleccionados.size(); i++) {
            if (archivosSeleccionados.get(i) == null) {
                return i;
            }
        }
        if (archivosSeleccionados.size() < 4) return archivosSeleccionados.size();
        return -1;
    }


    private void borrarDocumentoGuardado(int indice) {
        if (indice < documentosGuardadosIds.size()) {
            int idDoc = documentosGuardadosIds.get(indice);
            EntregaDocumentoDAO.eliminarDocumentoPorId(idDoc); 
            documentosGuardadosIds.remove(indice);
            documentosGuardadosNombres.remove(indice);
        }
    }

    private int contarDocumentosGuardadosNoReemplazados() {
        int cuenta = 0;
        for (int i = 0; i < documentosGuardadosIds.size(); i++) {
            if (i >= archivosSeleccionados.size() || archivosSeleccionados.get(i) == null) {
                cuenta++;
            }
        }
        return cuenta;
    }

    private void mostrarNombresArchivos() {
        lbNombreDoc1.setText("No seleccionado");
        lbNombreDoc2.setText("No seleccionado");
        lbNombreDoc3.setText("No seleccionado");
        lbNombreDoc4.setText("No seleccionado");

        for (int i = 0; i < archivosSeleccionados.size() && i < 4; i++) {
            File archivo = archivosSeleccionados.get(i);
            if (archivo != null) {
                setLabelNombre(i, archivo.getName());
            } else if (i < documentosGuardadosNombres.size()) {
                setLabelNombre(i, documentosGuardadosNombres.get(i));
            } else {
                setLabelNombre(i, "No seleccionado");
            }
        }
    }

    private void setLabelNombre(int indice, String nombre) {
        switch(indice) {
            case 0: lbNombreDoc1.setText(nombre); break;
            case 1: lbNombreDoc2.setText(nombre); break;
            case 2: lbNombreDoc3.setText(nombre); break;
            case 3: lbNombreDoc4.setText(nombre); break;
        }
    }

    
    private void eliminarDocumento(int indice) {
        if (archivosSeleccionados != null && archivosSeleccionados.size() > indice) {
            archivosSeleccionados.set(indice, null); 

            if (indice < documentosGuardadosIds.size()) {
                int idDoc = documentosGuardadosIds.get(indice);
                EntregaDocumentoDAO.eliminarDocumentoPorId(idDoc);
                documentosGuardadosIds.remove(indice);
                documentosGuardadosNombres.remove(indice);
            }

            mostrarNombresArchivos();
        }
    }

    @FXML
    private void clickEliminarDoc1(ActionEvent event) {
        eliminarDocumento(0);
    }

    @FXML
    private void clickEliminarDoc2(ActionEvent event) {
        eliminarDocumento(1);
    }

    @FXML
    private void clickEliminarDoc3(ActionEvent event) {
        eliminarDocumento(2);
    }

    @FXML
    private void clickEliminarDoc4(ActionEvent event) {
        eliminarDocumento(3);
    }
    
   private boolean subirArchivos(List<File> archivos) {
        if (archivos == null || archivos.isEmpty()) return false;

        int idEstudiante = estudianteSesion.getIdEstudiante();
        int idExpediente = ExpedienteDAO.crearExpedienteSiNoExiste(idEstudiante);

        if (idExpediente == -1) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo crear ni obtener el expediente.");
            return false;
        }

        boolean exito = true;

        for (int i = 0; i < archivosSeleccionados.size(); i++) {
            File archivo = archivosSeleccionados.get(i);
            if (archivo == null) continue; 

            if (i < documentosGuardadosIds.size()) {
                int idDoc = documentosGuardadosIds.get(i);
                if (!EntregaDocumentoDAO.actualizarDocumentoInicial(idDoc, archivo)) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el archivo: " + archivo.getName());
                    exito = false;
                }
            } else {
                if (!EntregaDocumentoDAO.guardarDocumentoInicial(archivo, idExpediente)) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo guardar el archivo: " + archivo.getName());
                    exito = false;
                }
            }
        }

        return exito;
    }

}
