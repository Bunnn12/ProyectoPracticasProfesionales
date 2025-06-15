/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemagestionpracticasprofesionales.utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import sistemagestionpracticasprofesionales.modelo.pojo.DatosDocumentoAsignacion;

/**
 *
 * @author reino
 */
public class DocumentoGenerador {
    public static File generarOficioAsignacion(DatosDocumentoAsignacion datos) {
        try {
            InputStream is = DocumentoGenerador.class.getResourceAsStream("/sistemagestionpracticasprofesionales/recursos/plantilla/oficio_asignacion.docx");
            if (is == null) {
                throw new FileNotFoundException("No se encontró la plantilla dentro del JAR");
            }

            XWPFDocument doc = new XWPFDocument(is);

            // Reemplazo de marcadores
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
