/*
 * Nombre del archivo: Sesion.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 15/06/2025
 * Descripción: Clase que maneja la sesión actual en la aplicación,
 * almacenando el estudiante y usuario seleccionados de forma estática,
 * con métodos para gestionar y consultar el estado de la sesión.
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 * Clase para gestionar la sesión actual de la aplicación, permitiendo
 * almacenar y recuperar el estudiante y usuario seleccionados,
 * así como limpiar la sesión y verificar el tipo de sesión activa.
 */
public class Sesion {
    
    private static Estudiante estudianteSeleccionado;
    private static Usuario usuarioSeleccionado;

    public Sesion() {
    }

    /**
     * Establece el estudiante seleccionado en la sesión.
     * @param estudiante Estudiante que se asigna a la sesión.
     */
    public static void setEstudianteSeleccionado(Estudiante estudiante) {
        estudianteSeleccionado = estudiante;
    }

    /**
     * Obtiene el estudiante actualmente seleccionado en la sesión.
     * @return Estudiante seleccionado o null si no hay ninguno.
     */
    public static Estudiante getEstudianteSeleccionado() {
        return estudianteSeleccionado;
    }
    
    /**
     * Establece el usuario seleccionado en la sesión.
     * @param usuario Usuario que se asigna a la sesión.
     */
    public static void setUsuarioSeleccionado(Usuario usuario) {
        usuarioSeleccionado = usuario;
    }

    /**
     * Obtiene el usuario actualmente seleccionado en la sesión.
     * @return Usuario seleccionado o null si no hay ninguno.
     */
    public static Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }
    
    /**
     * Limpia la sesión actual eliminando los datos del estudiante
     * y usuario seleccionados.
     */
    public static void limpiarSesion() {
        estudianteSeleccionado = null;
        usuarioSeleccionado = null;
    }
    
    /**
     * Indica si la sesión actual corresponde a un estudiante.
     * @return true si hay un estudiante seleccionado; false en caso contrario.
     */
    public static boolean esSesionEstudiante() {
        return estudianteSeleccionado != null;
    }
    
    /**
     * Indica si la sesión actual corresponde a un usuario.
     * @return true si hay un usuario seleccionado; false en caso contrario.
     */
    public static boolean esSesionUsuario() {
        return usuarioSeleccionado != null;
    }

}
