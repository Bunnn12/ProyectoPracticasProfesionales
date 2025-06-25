/**
    * Nombre del archivo: EvaluacionPresentacion.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 16/06/2025
    * Descripción: Clase POJO que representa la evaluación de la presentación de un estudiante.
*/
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 *
 * Representa las evaluaciones de las presentaciones de los estudiantes.
 */
public class EvaluacionPresentacion {
    private int idEvaluacionPresentacion;
    private double puntajeTotalObtenido;
    private int idExpediente;
    private String retroalimentacion;
    
    public EvaluacionPresentacion() {
    }

    /**
     * Constructor completo con todos los atributos de una evaluación de una presenteación.
     * @param idEvaluacionPresentacion Identificador de la evaluación de la presentación.
     * @param puntajeTotalObtenido Calificación total obtenida en la presentación.
     * @param idExpediente Identificador del expediente del estudiante.
     */
    public EvaluacionPresentacion(int idEvaluacionPresentacion, double puntajeTotalObtenido, int idExpediente) {
        this.idEvaluacionPresentacion = idEvaluacionPresentacion;
        this.puntajeTotalObtenido = puntajeTotalObtenido;
        this.idExpediente = idExpediente;
    }

    public int getIdEvaluacionPresentacion() {
        return idEvaluacionPresentacion;
    }

    public void setIdEvaluacionPresentacion(int idEvaluacionPresentacion) {
        this.idEvaluacionPresentacion = idEvaluacionPresentacion;
    }

    public double getPuntajeTotalObtenido() {
        return puntajeTotalObtenido;
    }

    public void setPuntajeTotalObtenido(double puntajeTotalObtenido) {
        this.puntajeTotalObtenido = puntajeTotalObtenido;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }
    public String getRetroalimentacion() {
        return retroalimentacion;
    }

    public void setRetroalimentacion(String retroalimentacion) {
        this.retroalimentacion = retroalimentacion;
    }
    
}
