/**
 * Nombre del archivo: DocumentoAnexo.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 15/06/2025
 * Descripción: Clase POJO que representa un documento anexo del expediente de prácticas.
 * Contiene información como nombre, tipo, estado, archivo y retroalimentación.
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

import java.sql.Date;
import sistemagestionpracticasprofesionales.interfaz.IArchivoPDF;

/**
 * Clase que representa un documento anexo dentro de un expediente.
 * Incluye información relevante como nombre, fecha de elaboración,
 * tipo de documento(inicial, final o intermedio), estado y retroalimentación.
 */
public class DocumentoAnexo implements IArchivoPDF {
    private int idDocumentoAnexo;
    private String nombre;
    private Date fechaElaboracion;
    private String tipo; 
    private int idExpediente;
    private byte[] archivo;
    private String estado; 
    private String retroalimentacion; 

    /**
     * Constructor que inicializa todos los atributos del documento anexo.
     *
     * @param idDocumentoAnexo Identificador único del documento anexo.
     * @param nombre Nombre del documento.
     * @param fechaElaboracion Fecha de elaboración del documento.
     * @param tipo Tipo de documento (por ejemplo, carta, reporte).
     * @param idExpediente Identificador del expediente al que pertenece.
     * @param archivo Archivo binario del documento.
     * @param estado Estado del documento (pendiente, aceptado, rechazado).
     * @param retroalimentacion Comentarios o retroalimentación del responsable.
     */
    public DocumentoAnexo(int idDocumentoAnexo, String nombre, Date fechaElaboracion, String tipo, int idExpediente, byte[] archivo, String estado, String retroalimentacion) {
        this.idDocumentoAnexo = idDocumentoAnexo;
        this.nombre = nombre;
        this.fechaElaboracion = fechaElaboracion;
        this.tipo = tipo;
        this.idExpediente = idExpediente;
        this.archivo = archivo;
        this.estado = estado;
        this.retroalimentacion = retroalimentacion;
    }

    public DocumentoAnexo() {
    }

    public int getIdDocumentoAnexo() {
        return idDocumentoAnexo;
    }

    public void setIdDocumentoAnexo(int idDocumentoAnexo) {
        this.idDocumentoAnexo = idDocumentoAnexo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public Date getFechaEntrega() {
        return fechaElaboracion; // campo a la interfaz común
    }
    
    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRetroalimentacion() {
        return retroalimentacion;
    }

    public void setRetroalimentacion(String retroalimentacion) {
        this.retroalimentacion = retroalimentacion;
    }
    
}
