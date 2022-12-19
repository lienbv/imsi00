package com.vibee.service.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.RenderedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Log4j2
public class ExportPDFQR {
    public static ByteArrayResource  export(int count, String productCode, BigDecimal price, String productName, String urlQR, String unit) {
        log.info("ExportPDFQR.export start with count: " + count + ", productCode: " + productCode + ", price: " + price + ", productName: " + productName + ", urlQR: " + urlQR + ", unit: " + unit);
        String now = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime());
        String title = "src/main/resources/static/qrcode/"+productCode+now+".pdf";
        try (OutputStream outputStream = new FileOutputStream(title)) {
            Document document = new Document(PageSize.A4, 0,0,0,0);
            document.left(0);
            int page = (int) (count/5)/9;
            document.setPageCount(page);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA);
            font.setSize(18);
            font.setColor(BaseColor.BLACK);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(70f);
            table.setSpacingBefore(10);
//            String url = urlQR;
//            String[] parts = url.split(",");
//            byte[] data = DatatypeConverter.parseBase64Binary(parts[1]);
            Image image = Image.getInstance(urlQR);
            image.scaleToFit(50, 50);
            Chunk chunk = new Chunk(image, 0, 0, true);

            BaseFont baseFont = BaseFont.createFont("src/main/resources/static/font/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontvn = new Font(baseFont);

            Phrase phrase = new Phrase();
            phrase.setFont(fontvn);
            phrase.getFont().setSize(8);
            phrase.add(chunk);
            String line1 = "\n"+productName;
            String line2 = "\n"+ Utiliies.convertMoney().format(price.doubleValue())+"/"+unit;
            phrase.add(line1);
            phrase.add(line2);
            PdfPCell pdfPCell = new PdfPCell(phrase);
            pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            pdfPCell.setPaddingTop(10);
            pdfPCell.setPaddingBottom(10);
            pdfPCell.setBorderWidthTop(2);
            pdfPCell.setBorderWidthBottom(2);
            pdfPCell.setBorderWidthLeft(2);
            pdfPCell.setBorderWidthRight(2);
            List<String> a = new ArrayList<>();
            int result = Math.round((float) count/5);
            for (int i = 0; i <= result; i++) {
                int indexCell = 0;
                for (int j = 0; j < 5; j++) {
                    if (count > 0) {
                        table.addCell(pdfPCell);
                        indexCell++;
                        count--;
                    } else  {
                        indexCell = 5 - indexCell;
                        cellNull(table,indexCell);
                        break;
                    }
                }
            }
            document.add(table);
            document.close();
        } catch (Exception e) {
            log.error("Invalid export file pdf");
//            e.printStackTrace();
        }
        try {
            Path path = Paths.get(title);
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            log.info("ExportPDFQR.export end");
            return resource;
        }catch (Exception e){
            log.error("error export pdf");
        }
        log.info("ExportPDFQR.export end");
//        return new ByteArrayResource(title.getBytes());
        return null;
    }

    private static void cellNull(PdfPTable table, int count) {
        PdfPCell pdfPCell = new PdfPCell(new Phrase());
        pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pdfPCell.setPaddingTop(10);
        pdfPCell.setPaddingBottom(10);
        pdfPCell.setBorderWidthTop(2);
        pdfPCell.setBorderWidthBottom(2);
        pdfPCell.setBorderWidthLeft(2);
        pdfPCell.setBorderWidthRight(2);
        switch (count) {
            case 1 :
                table.addCell(pdfPCell);
                break;
            case 2:
                table.addCell(pdfPCell);
                table.addCell(pdfPCell);
                break;
            case 3:
                table.addCell(pdfPCell);
                table.addCell(pdfPCell);
                table.addCell(pdfPCell);
                break;
            case 4:
                table.addCell(pdfPCell);
                table.addCell(pdfPCell);
                table.addCell(pdfPCell);
                table.addCell(pdfPCell);
                break;
            default:
        }
    }
}
