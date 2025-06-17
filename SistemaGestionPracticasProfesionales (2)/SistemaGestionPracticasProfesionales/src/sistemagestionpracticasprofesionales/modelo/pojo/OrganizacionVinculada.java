/**
 * Nombre del archivo: OrganizacionVinculada.java
 * Autor: Juan Pablo Silva Miranda
 * Fecha: 09/06/2025
 * Descripción: Clase POJO que representa una organización vinculada en el sistema
 * de gestión de prácticas profesionales, incluyendo sus datos básicos.
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 * Representa una organización vinculada con la institución educativa para
 * fines de prácticas profesionales.
 */
public class OrganizacionVinculada {
    private int idOrganizacionVinculada;
    private String nombre;
    private String telefono;
    private String direccion;
    private String correo;

    public OrganizacionVinculada() {
    }

    /**
     * Constructor que inicializa todos los campos de la organización vinculada.
     *
     * @param idOrganizacionVinculada Identificador único.
     * @param nombre Nombre de la organización.
     * @param telefono Teléfono de contacto.
     * @param direccion Dirección física.
     * @param correo Correo electrónico.
     */
    public OrganizacionVinculada(int idOrganizacionVinculada, String nombre, String telefono, String direccion, String correo) {
        this.idOrganizacionVinculada = idOrganizacionVinculada;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
    }

    public int getIdOrganizacionVinculada() {
        return idOrganizacionVinculada;
    }

    public void setIdOrganizacionVinculada(int idOrganizacionVinculada) {
        this.idOrganizacionVinculada = idOrganizacionVinculada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    
}
