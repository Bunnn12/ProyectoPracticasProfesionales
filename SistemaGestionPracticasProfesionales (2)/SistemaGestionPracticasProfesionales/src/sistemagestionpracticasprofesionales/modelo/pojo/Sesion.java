/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 *
 * @author reino
 */
public class Sesion {
     private static Estudiante estudianteSeleccionado;
     private static Usuario usuarioSeleccionado;

    public static void setEstudianteSeleccionado(Estudiante estudiante) {
        estudianteSeleccionado = estudiante;
    }

    public static Estudiante getEstudianteSeleccionado() {
        return estudianteSeleccionado;
    }
    public static void setUsuarioSeleccionado(Usuario usuario) {
        usuarioSeleccionado = usuario;
    }

    public static Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public static void limpiarSesion() {
        estudianteSeleccionado = null;
        usuarioSeleccionado = null;
    }
    public static boolean esSesionEstudiante() {
        return estudianteSeleccionado != null;
    }

    public static boolean esSesionUsuario() {
        return usuarioSeleccionado != null;
    }

}
