/*
 * Nombre del archivo: ResultadoOperacion.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 09/06/2025
 * Descripción: Clase POJO que representa el resultado de una operación,
 * indicando si hubo un error y proporcionando un mensaje descriptivo.
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 * Representa el resultado de una operación, indicando si ocurrió un error
 * y proporcionando un mensaje descriptivo relacionado.
 */
public class ResultadoOperacion {
    private boolean error;
    private String mensaje;

    public ResultadoOperacion() {
    }

    /**
     * Constructor que inicializa el resultado con el estado de error y mensaje.
     * 
     * @param error Indica si hubo un error (true) o no (false).
     * @param mensaje Mensaje descriptivo del resultado de la operación.
     */
    public ResultadoOperacion(boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
