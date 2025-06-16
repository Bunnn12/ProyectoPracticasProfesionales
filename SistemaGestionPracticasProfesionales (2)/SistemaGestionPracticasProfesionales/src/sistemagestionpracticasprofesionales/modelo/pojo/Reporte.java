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
public class Reporte {
    private int idReporte;
    private Date fechaEntrega;
    private int numeroHorasTrabajadas;
    private int idExpediente;
    private String nombre;
    private byte[] archivo;
    private String estado;

    public Reporte(int idReporte, Date fechaEntrega, int numeroHorasTrabajadas, int idExpediente, String nombre, byte[] archivo, String estado) {
        this.idReporte = idReporte;
        this.fechaEntrega = fechaEntrega;
        this.numeroHorasTrabajadas = numeroHorasTrabajadas;
        this.idExpediente = idExpediente;
        this.nombre = nombre;
        this.archivo = archivo;
        this.estado = estado;
    }


    public Reporte() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public int getNumeroHorasTrabajadas() {
        return numeroHorasTrabajadas;
    }

    public void setNumeroHorasTrabajadas(int numeroHorasTrabajadas) {
        this.numeroHorasTrabajadas = numeroHorasTrabajadas;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }
    
    
}
