package com.vibee.service.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vibee.model.item.ExportBill;
import com.vibee.model.item.StatisticTotalPriceOfBill;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Log4j2
public class ExportPDFBill {
    public void export(String nameStore, String address, String idBill, List<ExportBill> exportBills, StatisticTotalPriceOfBill statisticTotalPriceOfBill, String employee, int totalAmountProductOfBill) {
        String now = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime());
        String nowTitle = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss").format(Calendar.getInstance().getTime());
        String title = "src/main/resources/static/pdf/bill/"+idBill+now+".pdf";
        try (OutputStream outputStream = new FileOutputStream(title)) {
            Document document = new Document(PageSize.A5);
            PdfPTable table = new PdfPTable(4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            table.getDefaultCell().setBorder(2);
            table.setWidths(new int[]{100,30,10,30});

            writeHeader(table, idBill, nowTitle, nameStore, address);
            writeBody(table, exportBills, statisticTotalPriceOfBill, employee, address, totalAmountProductOfBill);

            document.add(table);
            document.close();
        } catch (Exception e) {
            log.error("Invalid export file PDF");
        }
    }


    public void writeHeader(PdfPTable table, String idBill, String date, String store, String address) {
        try {
            //table title
            PdfPCell pdfPCellLineTitle = new PdfPCell();
            BaseFont baseFontTitle = BaseFont.createFont("src/main/resources/static/font/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(baseFontTitle);
            fontTitle.setSize(6);
            fontTitle.setStyle(Font.BOLD);

            pdfPCellLineTitle.setColspan(4);
            pdfPCellLineTitle.setBorder(0);

            pdfPCellLineTitle.setPhrase(new Phrase("\n\nTẠP HÓA " + store.toUpperCase(), fontTitle));
            pdfPCellLineTitle.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(pdfPCellLineTitle);

            Font fontAddress = new Font(baseFontTitle);
            fontAddress.setSize(4);
            pdfPCellLineTitle.setColspan(4);
            pdfPCellLineTitle.setBorder(2);
            pdfPCellLineTitle.setPhrase(new Phrase(address, fontAddress));
            pdfPCellLineTitle.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(pdfPCellLineTitle);

            Font fontTitle2 = new Font(baseFontTitle);
            fontTitle2.setSize(4);
            fontTitle2.setStyle(Font.BOLD);

            pdfPCellLineTitle.setColspan(4);
            pdfPCellLineTitle.setBorder(0);
            pdfPCellLineTitle.setPhrase(new Phrase("Mã hóa đơn: "+idBill+"\nNgày: "+date +"\n", fontTitle2));
            pdfPCellLineTitle.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            table.addCell(pdfPCellLineTitle);

            //table bill
            BaseFont baseFont = BaseFont.createFont("src/main/resources/static/font/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont);
            PdfPCell pdfPCell = new PdfPCell();
            pdfPCell.setPaddingTop(3);
            pdfPCell.setPaddingBottom(3);
            pdfPCell.setBorder(3);
            font.setSize(6);
            font.setStyle(Font.BOLD);
            pdfPCell.setPhrase(new Phrase("Sản phẩm", font));
            pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            table.addCell(pdfPCell);
            pdfPCell.setPhrase(new Phrase("Giá", font));
            pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(pdfPCell);
            pdfPCell.setPhrase(new Phrase("Sl", font));
            pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(pdfPCell);
            pdfPCell.setPhrase(new Phrase("Thành tiền", font));
            pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table.addCell(pdfPCell);
        } catch (Exception e) {
            log.error("Invalid Font");
        }
    }

    public void writeBody(PdfPTable table, List<ExportBill> exportBills, StatisticTotalPriceOfBill statisticTotalPriceOfBill, String employee, String address, int totalAmountProductOfBill) {
        try {
            PdfPCell pdfPCell = new PdfPCell();
            pdfPCell.setBorder(3);
            BaseFont baseFont = BaseFont.createFont("src/main/resources/static/font/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont);
            font.setSize(6);

            for (ExportBill exportBill : exportBills) {
                pdfPCell.setPhrase(new Phrase(exportBill.getNameProduct(), font));
                pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                table.addCell(pdfPCell);
                pdfPCell.setPhrase(new Phrase(Utiliies.convertMoney().format(exportBill.getPrice().doubleValue()), font));
                pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(pdfPCell);
                pdfPCell.setPhrase(new Phrase(exportBill.getAmount()+"", font));
                pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(pdfPCell);
                pdfPCell.setPhrase(new Phrase(Utiliies.convertMoney().format(exportBill.getTotal().doubleValue())+"", font));
                pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                table.addCell(pdfPCell);
            }

            String lineStatus = "Cộng:\n\n" +
//                    "Chiểu khấu hóa đơn:\n\n" +
//                    "Thuế:\n\n" +
//                    "TỔNG CỘNG:\n\n" +
                    "Khác trả:\n\n" +
                    "Tiền trả lại:\n\n" +
                    "Thanh toán:";

            String lineResult = Utiliies.convertMoney().format(statisticTotalPriceOfBill.getTotalPriceOfBill().doubleValue())+"\n\n" +
//                    Utiliies.convertMoney().format(statisticTotalPriceOfBill.getDiscountBill().doubleValue())+"\n\n" +
//                    Utiliies.convertMoney().format(statisticTotalPriceOfBill.getTax().doubleValue())+"\n\n" +
//                    Utiliies.convertMoney().format(statisticTotalPriceOfBill.getTotal().doubleValue())+"\n\n" +
                    Utiliies.convertMoney().format(statisticTotalPriceOfBill.getPayingCustomer().doubleValue())+"\n\n" +
                    Utiliies.convertMoney().format(statisticTotalPriceOfBill.getRefunds().doubleValue())+"\n\n" +
                    statisticTotalPriceOfBill.getPaymentType()+"\n\n";

            PdfPCell pdfPCellLineStatus = new PdfPCell();
            pdfPCellLineStatus.setPhrase(new Phrase(lineStatus, font));
            pdfPCellLineStatus.setColspan(2);
            pdfPCellLineStatus.setBorder(3);
            pdfPCellLineStatus.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table.addCell(pdfPCellLineStatus);
            pdfPCell.setColspan(0);
            pdfPCell.setPhrase(new Phrase(totalAmountProductOfBill+"", font));
            pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(pdfPCell);
            pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            pdfPCell.setPhrase(new Phrase(lineResult, font));
            table.addCell(pdfPCell);

            Font fontFooter = new Font(baseFont);
            fontFooter.setSize(4);

            PdfPCell pdfPCellLineFooter = new PdfPCell();
            pdfPCellLineFooter.setColspan(4);
            pdfPCellLineFooter.setBorder(3);
            pdfPCellLineFooter.setPhrase(new Phrase("CHI NHÁNH: "+address.toUpperCase()+"\n\nTHU NGÂN: "+employee.toUpperCase(), fontFooter));
            pdfPCellLineFooter.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            table.addCell(pdfPCellLineFooter);

            pdfPCellLineFooter.setColspan(4);
            pdfPCellLineFooter.setBorder(0);
            pdfPCellLineFooter.setPhrase(new Phrase("\nHẹn gặp lại quý khách", fontFooter));
            pdfPCellLineFooter.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(pdfPCellLineFooter);
        } catch (Exception e) {
            log.error("Invalid Font");
        }
    }
}
