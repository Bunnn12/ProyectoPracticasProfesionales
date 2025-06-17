/**
 * Nombre del archivo: INotificacion.java  
 * Autor: Astrid Azucena Torres Lagunes 
 * Fecha: 09/06/2025
 * Descripción: Interfaz que define el método de notificación para operaciones exitosas
 * en componentes que implementen esta funcionalidad.
 */
package sistemagestionpracticasprofesionales.interfaz;


/**
 * Interfaz funcional para notificar el éxito de una operación.
 */
public interface INotificacion {
    /**
     * Notifica que una operación se ha realizado con éxito.
     *
     * @param tipo   Tipo de operación realizada.
     * @param nombre Nombre del elemento afectado por la operación.
     */
    public void operacionExitosa(String tipo, String nombre);
}
