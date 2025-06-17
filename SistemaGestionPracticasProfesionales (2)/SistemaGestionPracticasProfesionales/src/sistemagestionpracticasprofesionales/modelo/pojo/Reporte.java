/**
 * Nombre del archivo: Reporte.java
 * Autor: reino
 * Fecha: 17/06/2025
 * Descripción: Clase POJO que representa un reporte de prácticas profesionales,
 * con detalles como fecha de entrega, horas trabajadas, archivo adjunto y estado.
 */
package sistemagestionpracticasprofesionales.modelo.pojo;

import java.sql.Date;

/**
 * Representa un reporte relacionado con un expediente dentro del sistema
 * de gestión de prácticas profesionales.
 */
public class Reporte {
    private int idReporte;
    private Date fechaEntrega;
    private int numeroHorasTrabajadas;
    private int idExpediente;
    private String nombre;
    private byte[] archivo;
    private String estado;

    /**
     * Constructor completo con todos los atributos del reporte.
     *
     * @param idReporte Identificador único del reporte
     * @param fechaEntrega Fecha en que se entregó el reporte
     * @param numeroHorasTrabajadas Número de horas trabajadas registradas en el reporte
     * @param idExpediente Identificador del expediente asociado
     * @param nombre Nombre del reporte o archivo
     * @param archivo Contenido binario del archivo adjunto
     * @param estado Estado actual del reporte (por ejemplo, aprobado, pendiente, rechazado)
     */
    public Reporte(int idReporte, Date fechaEntrega, int numeroHorasTrabajadas, int idExpediente, String nombre, byte[] archivo, String estado) {
        this.idReporte = idReporte;
        this.fechaEntrega = fechaEntrega;
        this.numeroHorasTrabajadas = numeroHorasTrabajadas;
        this.idExpediente = idExpediente;
        this.nombre = nombre;
        this.archivo = archivo;
        this.estado = estado;
    }


    public Reporte() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public int getNumeroHorasTrabajadas() {
        return numeroHorasTrabajadas;
    }

    public void setNumeroHorasTrabajadas(int numeroHorasTrabajadas) {
        this.numeroHorasTrabajadas = numeroHorasTrabajadas;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }
    
    
}
