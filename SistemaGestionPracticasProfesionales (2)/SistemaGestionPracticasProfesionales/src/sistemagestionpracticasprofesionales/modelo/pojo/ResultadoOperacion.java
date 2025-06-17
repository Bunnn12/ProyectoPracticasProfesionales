/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 *
 * @author reino
 */
public class ResultadoOperacion {
    private boolean error;
    private String mensaje;
      private int idGenerado;

    public ResultadoOperacion() {
    }

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
    // Getter y Setter para idGenerado
    public int getIdGenerado() {
        return idGenerado;
    }

    public void setIdGenerado(int idGenerado) {
        this.idGenerado = idGenerado;
    }
}
