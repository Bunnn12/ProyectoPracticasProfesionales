/**
    * Nombre del archivo: EvaluacionPresentacion.java
    * Autor: Rodrigo Santa Bárbara Murrieta
    * Fecha: 16/06/2025
    * Descripción: Esta clase representa una fila dentro de la tabla de evaluación de presentación.
    * Contiene los datos del criterio de evaluación y un grupo de checkboxes que permiten seleccionar
    * una calificación entre 5 y 10 puntos.
*/
package sistemagestionpracticasprofesionales.modelo.pojo;

import javafx.beans.property.*;
import javafx.scene.control.CheckBox;


/**
 *
 * Clase que modela una fila para la evaluación de un criterio, incluyendo su descripción
 * y un conjunto de checkboxes para seleccionar una calificación única entre 5 y 10.
 */
public class CriterioEvaluacionFila {
    private final IntegerProperty idCriterio = new SimpleIntegerProperty();
    private final StringProperty nombreCriterio = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();

    private final CheckBox cb10 = new CheckBox();
    private final CheckBox cb9 = new CheckBox();
    private final CheckBox cb8 = new CheckBox();
    private final CheckBox cb7 = new CheckBox();
    private final CheckBox cb6 = new CheckBox();
    private final CheckBox cb5 = new CheckBox();
    
    /**
     * Constructor que recibe un objeto CriterioEvaluacion y lo adapta para ser mostrado en una fila.
     * @param criterio Objeto que contiene los datos del criterio a evaluar.
     */
    public CriterioEvaluacionFila(CriterioEvaluacion criterio) {
        this.idCriterio.set(criterio.getIdCriterio());
        this.nombreCriterio.set(criterio.getNombreCriterio());
        this.descripcion.set(criterio.getDescripcion());
        configurarGrupoCheckBoxes();
    }
    
    /**
     * Configura el comportamiento de los checkboxes para que solo uno pueda estar seleccionado a la vez.
     */
    private void configurarGrupoCheckBoxes() {
        CheckBox[] grupo = { cb10, cb9, cb8, cb7, cb6, cb5 };
        for (CheckBox cb : grupo) {
            cb.setOnAction(e -> {
                for (CheckBox otro : grupo) {
                    if (otro != cb) {
                        otro.setSelected(false);
                    }
                }
            });
        }
    }
    
    /**
     * Obtiene el ID del criterio asociado a esta fila.
     * @return ID del criterio.
     */
    public int getIdCriterio() {
        return idCriterio.get();
    }
    
    /**
     * 
     * @return Propiedad del nombre del criterio.
     */
    public StringProperty nombreCriterioProperty() {
        return nombreCriterio;
    }
    
    /**
     * 
     * @return Propiedad de la descripción del criterio.
     */
    public StringProperty descripcionProperty() {
        return descripcion;
    }
    
    // Getters para obtener los checkboxes individuales
    
    public CheckBox getCb10() {
        return cb10;
    }
    
    public CheckBox getCb9() {
        return cb9;
    }
    
    public CheckBox getCb8() {
        return cb8;
    }
    
    public CheckBox getCb7() {
        return cb7;
    }
    
    public CheckBox getCb6() {
        return cb6;
    }
    
    public CheckBox getCb5() {
        return cb5;
    }
    
    /**
     * Devuelve la calificación seleccionada en una fila.
     * Si ningún checkbox está seleccionado, retorna 0.
     * 
     * @return Calificación seleccionada (de 5 a 10), o 0 si no hay selección.
     */
    public int getCalificacionSeleccionada() {
        if (cb10.isSelected()) return 10;
        if (cb9.isSelected()) return 9;
        if (cb8.isSelected()) return 8;
        if (cb7.isSelected()) return 7;
        if (cb6.isSelected()) return 6;
        if (cb5.isSelected()) return 5;
        return 0;
    }
}
