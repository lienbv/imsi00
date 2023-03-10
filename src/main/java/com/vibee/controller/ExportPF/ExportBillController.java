package com.vibee.controller.ExportPF;

import com.vibee.model.item.ExportBill;
import com.vibee.model.item.StatisticTotalPriceOfBill;
import com.vibee.model.request.exportbill.ExportBillRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.service.pdf.ExportPDFBill;
import org.codehaus.jackson.map.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        statisticTotalPriceOfBill.setPayingCustomer(new BigDecimal(10000));
        statisticTotalPriceOfBill.setRefunds(new BigDecimal(10000));
        statisticTotalPriceOfBill.paymentTypeOfCustomer(true);
        exportPDFBill.export("Huy???n Trang", "s??? nh?? 18, C???u Gi???, x?? ?????i Xuy??n, huy???n Ph?? Xuy??n, H?? N???i", "ID-1234", exportBills, statisticTotalPriceOfBill, "V?? V??n Lanh", 10);
    }


    @GetMapping("/export-bill")
    public BaseResponse exportBill(@RequestBody ExportBillRequest request) {
        ExportPDFBill exportPDFBill = new ExportPDFBill();
        BaseResponse response = new BaseResponse();
        exportPDFBill.export("Huy???n Trang", "s??? nh?? 18, C???u Gi???, x?? ?????i Xuy??n, huy???n Ph?? Xuy??n, H?? N???i", request.getIdBill()+"", request.getExportBills(), request.getStatisticTotalPriceOfBill(), "V?? V??n Lanh", request.getTotalAmountProductOfBill());
        return response;
    }

}
