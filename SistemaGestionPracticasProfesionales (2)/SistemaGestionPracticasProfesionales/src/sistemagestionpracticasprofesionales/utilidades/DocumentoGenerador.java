/*
 * Nombre del archivo: DocumentoGenerador.java
 * Autor: Astrid Azucena Torres Lagunes
 * Fecha: 14/06/2025
 * Descripción: Clase utilitaria para generar documentos Word (.docx) de asignación
 * de prácticas profesionales a partir de una plantilla, reemplazando marcadores
 * por los datos reales proporcionados en un objeto DatosDocumentoAsignacion.
 */
package sistemagestionpracticasprofesionales.utilidades;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import sistemagestionpracticasprofesionales.modelo.pojo.DatosDocumentoAsignacion;

/**
 * Clase utilitaria para generar documentos Word (oficios) con base en una plantilla.
 * Usa Apache POI para manipular plantillas DOCX, remplazando marcadores por datos reales.
 */
public class DocumentoGenerador {
    
    /**
     * Genera un archivo de oficio de asignación Word (.docx) a partir de una plantilla.
     * Los marcadores en la plantilla se reemplazan por los datos del objeto proporcionado.
     * 
     * @param datos Objeto con la información para llenar el documento.
     * @return Archivo generado listo para usar o null si ocurrió un error.
     */
    public static File generarOficioAsignacion(DatosDocumentoAsignacion datos) {
        try {
            InputStream is = DocumentoGenerador.class.getResourceAsStream("/sistemagestionpracticasprofesionales/recursos/plantilla/oficio_asignacion.docx");
            if (is == null) {
                throw new FileNotFoundException("No se encontró la plantilla dentro del JAR");
            }

            XWPFDocument doc = new XWPFDocument(is);

            for (XWPFParagraph p : doc.getParagraphs()) {
                reemplazarMarcadores(p, datos);
            }
            for (XWPFTable tabla : doc.getTables()) {
                for (XWPFTableRow fila : tabla.getRows()) {
                    for (XWPFTableCell celda : fila.getTableCells()) {
                        for (XWPFParagraph p : celda.getParagraphs()) {
                            reemplazarMarcadores(p, datos);
                        }
                    }
                }
            }

            File carpeta = new File("oficios/");
            if (!carpeta.exists()) carpeta.mkdirs();

            String nombreArchivo = "Oficio_" + datos.getMatricula() + ".docx";
            File archivo = new File(carpeta, nombreArchivo);

            FileOutputStream fos = new FileOutputStream(archivo);
            doc.write(fos);
            fos.close();
            doc.close();
            is.close();

            return archivo; 

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Reemplaza los marcadores de texto dentro de un párrafo por los valores correspondientes.
     * 
     * @param parrafo Párrafo del documento donde se harán los reemplazos.
     * @param datos Datos que sustituyen a los marcadores.
     */
    private static void reemplazarMarcadores(XWPFParagraph parrafo, DatosDocumentoAsignacion datos) {
        String textoCompleto = parrafo.getText();

        if (textoCompleto != null && !textoCompleto.isEmpty()) {
            textoCompleto = textoCompleto.replace("${fechaActual}", LocalDate.now().format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy")));
            textoCompleto = textoCompleto.replace("${nombreCompleto}", datos.getNombreCompleto());
            textoCompleto = textoCompleto.replace("${matricula}", datos.getMatricula());
            textoCompleto = textoCompleto.replace("${nombreProyecto}", datos.getNombreProyecto());
            textoCompleto = textoCompleto.replace("${fechaInicio}", datos.getFechaInicio());
            textoCompleto = textoCompleto.replace("${fechaFin}", datos.getFechaFin());
            textoCompleto = textoCompleto.replace("${horaEntrada}", datos.getHoraEntrada());
            textoCompleto = textoCompleto.replace("${horaSalida}", datos.getHoraSalida());
            textoCompleto = textoCompleto.replace("${nombreOrganizacion}", datos.getNombreOrganizacion());
            textoCompleto = textoCompleto.replace("${nombreResponsable}", datos.getNombreResponsable());
            textoCompleto = textoCompleto.replace("${correoResponsable}", datos.getCorreoResponsable());
            textoCompleto = textoCompleto.replace("${telefonoResponsable}", datos.getTelefonoResponsable());

            int numRuns = parrafo.getRuns().size();
            for (int i = numRuns - 1; i >= 0; i--) {
                parrafo.removeRun(i);
            }
            parrafo.createRun().setText(textoCompleto, 0);
            }
        }
    }
