/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 *
 * @author reino
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
