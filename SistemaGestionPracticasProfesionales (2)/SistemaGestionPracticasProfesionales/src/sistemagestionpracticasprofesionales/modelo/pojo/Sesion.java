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

    public static void setEstudianteSeleccionado(Estudiante estudiante) {
        estudianteSeleccionado = estudiante;
    }

    public static Estudiante getEstudianteSeleccionado() {
        return estudianteSeleccionado;
    }

    public static void limpiarSesion() {
        estudianteSeleccionado = null;
    }
}
