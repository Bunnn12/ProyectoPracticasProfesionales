/**
 * Nombre del archivo: Proyecto.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 10/06/2025
 * Descripción: Clase POJO que representa un proyecto de prácticas profesionales,
 * incluyendo detalles del proyecto, organización, responsable y estudiantes.
 */

package sistemagestionpracticasprofesionales.modelo.pojo;


/**
 * Representa un proyecto vinculado a una organización y coordinado por un responsable,
 * que puede tener varios estudiantes asignados.
 */
public class Proyecto {
    private int idProyecto;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String diasTrabajo;
    private String horaEntrada;
    private String horaSalida;
    private int cantidadEstudiantesParticipantes;
    private int idOrganizacionVinculada;
    private int idResponsableProyecto;
    private Integer idEstudiante; 
    private String nombreResponsable;
    private String nombreOrganizacion;
    private int estudiantesAsignados;


    public Proyecto() {}

     /**
     * Constructor completo con todos los atributos del proyecto.
     *
     * @param idProyecto Identificador del proyecto
     * @param nombre Nombre del proyecto
     * @param descripcion Descripción del proyecto
     * @param fechaInicio Fecha de inicio del proyecto
     * @param fechaFin Fecha de finalización del proyecto
     * @param horaEntrada Hora de entrada diaria de los estudiantes
     * @param horaSalida Hora de salida diaria de los estudiantes
     * @param cantidadEstudiantesParticipantes Número total de estudiantes requeridos
     * @param idOrganizacionVinculada ID de la organización donde se desarrolla el proyecto
     * @param idResponsableProyecto ID del responsable del proyecto
     * @param idEstudiante ID del estudiante asignado (puede ser null)
     * @param nombreResponsable Nombre completo del responsable del proyecto
     * @param nombreOrganizacion Nombre de la organización vinculada
     * @param estudiantesAsignados Número actual de estudiantes asignados
     */
    public Proyecto(int idProyecto, String nombre, String descripcion, String fechaInicio, String fechaFin, String horaEntrada, String horaSalida, int cantidadEstudiantesParticipantes, int idOrganizacionVinculada, int idResponsableProyecto, Integer idEstudiante, String nombreResponsable, String nombreOrganizacion, int estudiantesAsignados) {
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
        this.estudiantesAsignados = estudiantesAsignados;
    }

    // Constructor alterno para la creación de objetos de tipo Proyecto
    public Proyecto(int idProyecto, String nombre, int idOrganizacionVinculada, int idResponsableProyecto, String fechaInicio, String fechaFin, String diasTrabajo, String horaEntrada, String horaSalida, int cantidadEstudiantesParticipantes, String descripcion) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.idOrganizacionVinculada = idOrganizacionVinculada;
        this.idResponsableProyecto = idResponsableProyecto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diasTrabajo = diasTrabajo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.cantidadEstudiantesParticipantes = cantidadEstudiantesParticipantes;
        this.descripcion = descripcion;
    }

    public int getEstudiantesAsignados() {
        return estudiantesAsignados;
    }

    public void setEstudiantesAsignados(int estudiantesAsignados) {
        this.estudiantesAsignados = estudiantesAsignados;
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
    public String getDiasTrabajo() {
        return diasTrabajo;
    }

    public void setDiasTrabajo(String diasTrabajo) {
        this.diasTrabajo = diasTrabajo;
    }
}
