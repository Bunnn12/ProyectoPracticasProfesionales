/**
    * Nombre del archivo: CriterioEvaluacion.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 16/06/2025
    * Descripción: Clase POJO que representa los criterios de evaluación establecidos
    * para evaluar las presentaciones y organizaciones vinculadas.
*/
package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 *
 * Representa los criterios de evaluación, tanto para evaluar presentaciones, como para evaluar organizaciones vinculadas. 
 */
public class CriterioEvaluacion {
    int idCriterio;
    String nombreCriterio;
    String descripcion;
    
    public CriterioEvaluacion() {
    }

    /**
     * Constructor completo con todos los atributos de un criterio de evaluación.
     * @param idCriterio Identificador del criterio.
     * @param nombreCriterio Descripción rápido del criterio.
     * @param descripcion Descripción detalla de en qué consiste el criterio.
     */
    public CriterioEvaluacion(int idCriterio, String nombreCriterio, String descripcion) {
        this.idCriterio = idCriterio;
        this.nombreCriterio = nombreCriterio;
        this.descripcion = descripcion;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public String getNombreCriterio() {
        return nombreCriterio;
    }

    public void setNombreCriterio(String nombreCriterio) {
        this.nombreCriterio = nombreCriterio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
