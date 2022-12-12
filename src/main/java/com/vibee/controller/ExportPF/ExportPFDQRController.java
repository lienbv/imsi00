package com.vibee.controller.ExportPF;

import com.vibee.model.response.BaseResponse;
import com.vibee.service.pdf.ExportPDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/vibee/api/v1/export-qr")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExportPFDQRController {
    private ExportPDFService exportPDFService;

    @Autowired
    public ExportPFDQRController(ExportPDFService exportPDFService) {
        this.exportPDFService = exportPDFService;
    }

    @PostMapping("")
    public BaseResponse printQRCodePDF(@RequestParam(value = "product-code") String productCode,
                                       @RequestParam(value = "amount") int amount,
                                       @RequestParam(value = "language", defaultValue = "vi") String language) {
        return this.exportPDFService.printQRCodePDF(productCode, amount, language);
    }
}
