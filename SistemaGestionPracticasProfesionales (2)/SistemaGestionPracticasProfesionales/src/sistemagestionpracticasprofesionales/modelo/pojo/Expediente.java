/**
 * Nombre del archivo: Expediente.java
 * Autor: Juan Pablo Silva Miranda
 * Fecha: 09/06/2025
 * Descripción: Clase POJO que representa un expediente de prácticas profesionales,
 * incluyendo fechas, horas acumuladas, estado y relación con un estudiante.
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

import java.sql.Date;

/**
 * Clase que representa un expediente de prácticas profesionales.
 */
public class Expediente {
    private int idExpediente;
    private Date fechaInicio;
    private Date fechaFin;
    private int horasAcumuladas;
    private String estado;
    private int idEstudiante;
    private Estudiante estudiante; 
     private Proyecto proyecto;

    public Expediente() {
    }

    /**
     * Constructor que inicializa todos los atributos excepto el objeto estudiante.
     *
     * @param idExpediente     Identificador del expediente.
     * @param fechaInicio      Fecha de inicio de las prácticas.
     * @param fechaFin         Fecha de fin de las prácticas.
     * @param horasAcumuladas  Cantidad de horas acumuladas.
     * @param estado           Estado actual del expediente.
     * @param idEstudiante     Identificador del estudiante relacionado.
     */
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

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}