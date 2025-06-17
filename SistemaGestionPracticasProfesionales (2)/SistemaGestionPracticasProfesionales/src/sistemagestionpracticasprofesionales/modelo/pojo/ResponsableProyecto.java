/**
 * Nombre del archivo: ResponsableProyecto.java
 * Autor: Juan Pablo Silva Miranda
 * Fecha: 09/06/2025
 * Descripción: Clase POJO que representa a un responsable de proyecto
 * con sus datos personales y la organización vinculada.
 */
package sistemagestionpracticasprofesionales.modelo.pojo;


/**
 * Representa a un responsable asignado a un proyecto,
 * incluyendo su información de contacto y la organización a la que pertenece.
 */
public class ResponsableProyecto {
    private int idResponsable;
    private String nombre;
    private String correo;
    private String telefono;
    private int idOrganizacionVinculada;
    private String nombreOrganizacionVinculada;

    public ResponsableProyecto() {
    }
    
    /**
    * Constructor completo que inicializa todos los atributos de un ResponsableProyecto.
    *
    * @param idResponsable Identificador único del responsable
    * @param nombre Nombre completo del responsable
    * @param correo Correo electrónico del responsable
    * @param telefono Número telefónico del responsable
    * @param idOrganizacionVinculada Identificador de la organización vinculada
    * @param nombreOrganizacionVinculada Nombre de la organización vinculada
    */
    public ResponsableProyecto(int idResponsable, String nombre, String correo, String telefono, int idOrganizacionVinculada, String nombreOrganizacionVinculada) {
        this.idResponsable = idResponsable;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.idOrganizacionVinculada = idOrganizacionVinculada;
        this.nombreOrganizacionVinculada = nombreOrganizacionVinculada;
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdOrganizacionVinculada() {
        return idOrganizacionVinculada;
    }

    public void setIdOrganizacionVinculada(int idOrganizacionVinculada) {
        this.idOrganizacionVinculada = idOrganizacionVinculada;
    }

    public String getNombreOrganizacionVinculada() {
        return nombreOrganizacionVinculada;
    }

    public void setNombreOrganizacionVinculada(String nombreOrganizacionVinculada) {
        this.nombreOrganizacionVinculada = nombreOrganizacionVinculada;
    }
     
}
