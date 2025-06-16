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
public class Expediente {
    private int idExpediente;
    private Date fechaInicio;
    private Date fechaFin;
    private int horasAcumuladas;
    private String estado; 
    private int idEstudiante;

    public Expediente() {}

    public Expediente(int idExpediente, Date fechaInicio, Date fechaFin, int horasAcumuladas, String estado, int idEstudiante) {
        this.idExpediente = idExpediente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horasAcumuladas = horasAcumuladas;
        this.estado = estado;
        this.idEstudiante = idEstudiante;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getHorasAcumuladas() {
        return horasAcumuladas;
    }

    public void setHorasAcumuladas(int horasAcumuladas) {
        this.horasAcumuladas = horasAcumuladas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }
    
    
} 
