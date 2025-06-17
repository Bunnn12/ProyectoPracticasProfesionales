/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 *
 * @author rodri
 */
public class ExpedienteCompleto {
    private String nombreEstudiante;
    private String nombreProyecto;
    private String nombreOV;
    private String fechaInicio;
    private String fechaFin;
    private int horasAcumuladas;
    private String estado;
    private String evaluacionPresentacion;
    private double puntajeObtenido;
    private String evaluacionOV;

    public ExpedienteCompleto() {
    }

    public ExpedienteCompleto(String nombreEstudiante, String nombreProyecto, String nombreOV, String fechaInicio, String fechaFin, int horasAcumuladas, String estado, String evaluacionPresentacion, double puntajeObtenido, String evaluacionOV) {
        this.nombreEstudiante = nombreEstudiante;
        this.nombreProyecto = nombreProyecto;
        this.nombreOV = nombreOV;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horasAcumuladas = horasAcumuladas;
        this.estado = estado;
        this.evaluacionPresentacion = evaluacionPresentacion;
        this.puntajeObtenido = puntajeObtenido;
        this.evaluacionOV = evaluacionOV;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getNombreOV() {
        return nombreOV;
    }

    public void setNombreOV(String nombreOV) {
        this.nombreOV = nombreOV;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
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

    public String getEvaluacionPresentacion() {
        return evaluacionPresentacion;
    }

    public void setEvaluacionPresentacion(String evaluacionPresentacion) {
        this.evaluacionPresentacion = evaluacionPresentacion;
    }

    public double getPuntajeObtenido() {
        return puntajeObtenido;
    }

    public void setPuntajeObtenido(double puntajeObtenido) {
        this.puntajeObtenido = puntajeObtenido;
    }

    public String getEvaluacionOV() {
        return evaluacionOV;
    }

    public void setEvaluacionOV(String evaluacionOV) {
        this.evaluacionOV = evaluacionOV;
    }
}
