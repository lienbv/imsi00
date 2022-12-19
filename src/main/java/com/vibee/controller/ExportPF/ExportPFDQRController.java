package com.vibee.controller.ExportPF;

import com.vibee.model.response.BaseResponse;
import com.vibee.service.pdf.ExportPDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/vibee/api/v1/file")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExportPFDQRController {
    private ExportPDFService exportPDFService;

    @Autowired
    public ExportPFDQRController(ExportPDFService exportPDFService) {
        this.exportPDFService = exportPDFService;
    }

    @GetMapping(value = "/pdf/download", produces = "application/pdf")
    public ResponseEntity printQRCodePDF(@RequestParam(value = "productCode") String productCode,
                                         @RequestParam(value = "amount") int amount,
                                         @RequestParam(value = "language", defaultValue = "vi") String language) {
        ByteArrayResource resource = exportPDFService.printQRCodePDF(productCode, amount, language);
        return ResponseEntity.ok().contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=qr-code.pdf")
                .body(resource);
    }
}
