/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

import java.sql.Date;

/**
 *
 * @author reino
 */
public class DocumentoAnexo {
    private int idDocumentoAnexo;
    private String nombre;
    private Date fechaElaboracion;
    private String tipo; 
    private int idExpediente;
    private byte[] archivo;
    private String estado; 
    private String retroalimentacion; 

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
