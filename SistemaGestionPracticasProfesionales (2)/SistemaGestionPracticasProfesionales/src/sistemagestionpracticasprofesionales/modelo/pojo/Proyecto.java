/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 *
 * @author reino
 */
public class Proyecto {
    private int idProyecto;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String horaEntrada;
    private String horaSalida;
    private int cantidadEstudiantesParticipantes;
    private int idOrganizacionVinculada;
    private int idResponsableProyecto;
    private Integer idEstudiante; 
    private String nombreResponsable;
    private String nombreOrganizacion;

    public Proyecto() {}

    public Proyecto(int idProyecto, String nombre, String descripcion, String fechaInicio, String fechaFin, String horaEntrada, String horaSalida, int cantidadEstudiantesParticipantes, int idOrganizacionVinculada, int idResponsableProyecto, Integer idEstudiante, String nombreResponsable, String nombreOrganizacion) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.cantidadEstudiantesParticipantes = cantidadEstudiantesParticipantes;
        this.idOrganizacionVinculada = idOrganizacionVinculada;
        this.idResponsableProyecto = idResponsableProyecto;
        this.idEstudiante = idEstudiante;
        this.nombreResponsable = nombreResponsable;
        this.nombreOrganizacion = nombreOrganizacion;
    }
    
    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getNombreOrganizacion() {
        return nombreOrganizacion;
    }

    public void setNombreOrganizacion(String nombreOrganizacion) {
        this.nombreOrganizacion = nombreOrganizacion;
    }
    
    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getCantidadEstudiantesParticipantes() {
        return cantidadEstudiantesParticipantes;
    }

    public void setCantidadEstudiantesParticipantes(int cantidadEstudiantesParticipantes) {
        this.cantidadEstudiantesParticipantes = cantidadEstudiantesParticipantes;
    }

    public int getIdOrganizacionVinculada() {
        return idOrganizacionVinculada;
    }

    public void setIdOrganizacionVinculada(int idOrganizacionVinculada) {
        this.idOrganizacionVinculada = idOrganizacionVinculada;
    }

    public int getIdResponsableProyecto() {
        return idResponsableProyecto;
    }

    public void setIdResponsableProyecto(int idResponsableProyecto) {
        this.idResponsableProyecto = idResponsableProyecto;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }
}
