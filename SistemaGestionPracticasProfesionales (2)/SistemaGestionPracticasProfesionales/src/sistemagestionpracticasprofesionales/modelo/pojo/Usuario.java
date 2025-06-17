/*
 * Nombre del archivo: Usuario.java
 * Autor: Rodrigo Santa Bárbara Murrieta
 * Fecha: 08/06/2025
 * Descripción: Clase POJO que representa un usuario del sistema,
 * con atributos para datos personales y credenciales de acceso.
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 * Representa un usuario del sistema con su información personal,
 * credenciales y rol (coordinador, profesor EE o evaluador)
 */
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String usuario;
    private String contrasenia;
    private String rol;

    public Usuario() {}

    /**
     * Constructor que inicializa todos los atributos del usuario.
     * 
     * @param idUsuario Identificador único del usuario.
     * @param nombre Nombre del usuario.
     * @param apellidoPaterno Apellido paterno del usuario.
     * @param apellidoMaterno Apellido materno del usuario.
     * @param usuario Nombre de usuario para acceso.
     * @param contrasenia Contraseña del usuario.
     * @param rol Rol asignado al usuario.
     */
    public Usuario(int idUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String usuario, String contrasenia, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
