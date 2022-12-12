package com.vibee.controller.ExportPF;

import com.vibee.model.item.ExportBill;
import com.vibee.model.item.StatisticTotalPriceOfBill;
import com.vibee.service.pdf.ExportPDFBill;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExportBillController {
    public static void main(String[] args) {
        ExportPDFBill exportPDFBill = new ExportPDFBill();
        List<ExportBill> exportBills = Arrays.asList(
                new ExportBill("1", new BigDecimal("10000"), 10, new BigDecimal("20000")),
                new ExportBill("2", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("3", new BigDecimal("10000"), 12, new BigDecimal("20000")),
                new ExportBill("4", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("1", new BigDecimal("10000"), 10, new BigDecimal("20000")),
                new ExportBill("2", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("3", new BigDecimal("10000"), 12, new BigDecimal("20000")),
                new ExportBill("4", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("1", new BigDecimal("10000"), 10, new BigDecimal("20000")),
                new ExportBill("2", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("3", new BigDecimal("10000"), 12, new BigDecimal("20000")),
                new ExportBill("4", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("1", new BigDecimal("10000"), 10, new BigDecimal("20000")),
                new ExportBill("2", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("3", new BigDecimal("10000"), 12, new BigDecimal("20000")),
                new ExportBill("4", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("1", new BigDecimal("10000"), 10, new BigDecimal("20000")),
                new ExportBill("2", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("3", new BigDecimal("10000"), 12, new BigDecimal("20000")),
                new ExportBill("4", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("1", new BigDecimal("10000"), 10, new BigDecimal("20000")),
                new ExportBill("2", new BigDecimal("10000"), 11, new BigDecimal("20000")),
                new ExportBill("3", new BigDecimal("10000"), 12, new BigDecimal("20000")),
                new ExportBill("4", new BigDecimal("10000"), 11, new BigDecimal("20000"))
        );
        StatisticTotalPriceOfBill statisticTotalPriceOfBill = new StatisticTotalPriceOfBill();
        statisticTotalPriceOfBill.setTotalPriceOfBill(new BigDecimal(10000));
//        statisticTotalPriceOfBill.setTotal(new BigDecimal(10000));
        statisticTotalPriceOfBill.setPayingCustomer(new BigDecimal(10000));
//        statisticTotalPriceOfBill.setDiscountBill(new BigDecimal(10000));
        statisticTotalPriceOfBill.setRefunds(new BigDecimal(10000));
//        statisticTotalPriceOfBill.setTax(new BigDecimal(10000));
        statisticTotalPriceOfBill.paymentTypeOfCustomer(true);
        exportPDFBill.export("Huyền Trang", "số nhà 18, Cầu Giẽ, xã Đại Xuyên, huyện Phú Xuyên, Hà Nội", "ID-1234", exportBills, statisticTotalPriceOfBill, "Vũ Văn Lanh", 10);
    }
}
