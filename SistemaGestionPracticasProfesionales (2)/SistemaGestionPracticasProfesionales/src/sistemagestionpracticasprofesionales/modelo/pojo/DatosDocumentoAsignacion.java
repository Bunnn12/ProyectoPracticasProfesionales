/**
 * Nombre del archivo: DatosDocumentoAsignacion.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 14/06/2025
 * Descripción: Clase POJO que encapsula los datos necesarios para generar 
 * el documento de asignación de prácticas profesionales. 
 * Contiene información del estudiante, proyecto, organización y responsable.
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 * Clase que contiene los datos para la generación del documento de asignación
 * de un estudiante a un proyecto dentro de una organización vinculada.
 */
public class DatosDocumentoAsignacion {
    // Estudiante
    private String nombreCompleto;
    private String matricula;
    private int idEstudiante;
    
    // Proyecto
    private String nombreProyecto;
    private String fechaInicio;
    private String fechaFin;
    private String horaEntrada;
    private String horaSalida;

    // Organización
    private String nombreOrganizacion;

    // Responsable del proyecto
    private String nombreResponsable;
    private String correoResponsable;
    private String telefonoResponsable;
    private int idProyecto;

    /**
     * Constructor que inicializa todos los atributos de la clase.
     *
     * @param nombreCompleto Nombre completo del estudiante.
     * @param matricula Matrícula del estudiante.
     * @param nombreProyecto Nombre del proyecto asignado.
     * @param fechaInicio Fecha de inicio de las prácticas.
     * @param fechaFin Fecha de finalización de las prácticas.
     * @param horaEntrada Hora de entrada diaria del estudiante a las practicas.
     * @param horaSalida Hora de salida diaria del estudiante a las practicas.
     * @param nombreOrganizacion Nombre de la organización vinculada.
     * @param nombreResponsable Nombre del responsable del proyecto.
     * @param correoResponsable Correo electrónico del responsable.
     * @param telefonoResponsable Teléfono del responsable del proyecto.
     * @param idEstudiante Identificador único del estudiante.
     * @param idProyecto Identificador único del proyecto.
     */
    public DatosDocumentoAsignacion(String nombreCompleto, String matricula, String nombreProyecto, String fechaInicio, String fechaFin, String horaEntrada, String horaSalida, String nombreOrganizacion, String nombreResponsable, String correoResponsable, String telefonoResponsable, int idEstudiante, int idProyecto) {
        this.nombreCompleto = nombreCompleto;
        this.matricula = matricula;
        this.nombreProyecto = nombreProyecto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.nombreOrganizacion = nombreOrganizacion;
        this.nombreResponsable = nombreResponsable;
        this.correoResponsable = correoResponsable;
        this.telefonoResponsable = telefonoResponsable;
        this.idEstudiante = idEstudiante;
        this.idProyecto = idProyecto;
    }

    public DatosDocumentoAsignacion() {
    }
    
    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
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

    public String getNombreOrganizacion() {
        return nombreOrganizacion;
    }

    public void setNombreOrganizacion(String nombreOrganizacion) {
        this.nombreOrganizacion = nombreOrganizacion;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getCorreoResponsable() {
        return correoResponsable;
    }

    public void setCorreoResponsable(String correoResponsable) {
        this.correoResponsable = correoResponsable;
    }

    public String getTelefonoResponsable() {
        return telefonoResponsable;
    }

    public void setTelefonoResponsable(String telefonoResponsable) {
        this.telefonoResponsable = telefonoResponsable;
    }
}
