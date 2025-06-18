package sistemagestionpracticasprofesionales.modelo.pojo;

import java.time.LocalDate;

public class ExpedienteCompleto {
    private int idExpediente;             // ID del expediente
    private String nombreEstudiante;
    private String nombreProyecto;
    private String nombreOV;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int horasAcumuladas;
    private String estado;
    private String evaluacionPresentacion;
    private double puntajeObtenido;
    private String evaluacionOV;

    // También puedes tener un objeto Estudiante en vez de solo nombre
    private Estudiante estudiante;  // Si tienes la clase Estudiante y quieres usarla

    public ExpedienteCompleto() {
    }

    public ExpedienteCompleto(int idExpediente, String nombreEstudiante, String nombreProyecto, String nombreOV,
                             LocalDate fechaInicio, LocalDate fechaFin, int horasAcumuladas, String estado,
                             String evaluacionPresentacion, double puntajeObtenido, String evaluacionOV) {
        this.idExpediente = idExpediente;
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

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    // El resto de getters y setters que ya tenías

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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
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
