package com.vibee.service.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vibee.model.item.ExportBill;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Log4j2
public class ExportPDFBill {
    public void export(String nameStore, String address, String idBill, String date, List<ExportBill> exportBills) {
        String now = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime());
        String title = idBill+now+".pdf";
        try (OutputStream outputStream = new FileOutputStream(title)) {
            Document document = new Document(PageSize.A5);
            PdfPTable table = new PdfPTable(4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            table.getDefaultCell().setBorder(2);

            writeHeader(table);
            writeBody(table, exportBills);

            document.add(table);
            document.close();
        } catch (Exception e) {
            log.error("Invalid export file PDF");
        }
    }

    public void writeHeader(PdfPTable table) {
        try {
            PdfPCell pdfPCell = new PdfPCell();
            pdfPCell.setPadding(5);

            BaseFont baseFont = BaseFont.createFont("src/main/resources/static/font/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = FontFactory.getFont(baseFont.toString(), 10);

            pdfPCell.setPhrase(new Phrase("Sản phẩm", font));
            table.addCell(pdfPCell);
            pdfPCell.setPhrase(new Phrase("Giá", font));
            table.addCell(pdfPCell);
            pdfPCell.setPhrase(new Phrase("Số lượng", font));
            table.addCell(pdfPCell);
            pdfPCell.setPhrase(new Phrase("Thành tiền", font));
            table.addCell(pdfPCell);
        } catch (Exception e) {
            log.error("Invalid Font");
        }
    }

    public void writeBody(PdfPTable table, List<ExportBill> exportBills) {
        for (ExportBill exportBill : exportBills) {
            table.addCell(exportBill.getNameProduct());
            table.addCell(Utiliies.convertMoney().format(exportBill.getPrice().doubleValue()));
            table.addCell(exportBill.getAmount()+"");
            table.addCell(Utiliies.convertMoney().format(exportBill.getTotal().doubleValue()));
        }
    }
}
