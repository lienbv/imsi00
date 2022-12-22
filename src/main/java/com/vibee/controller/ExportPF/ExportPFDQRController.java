package com.vibee.controller.ExportPF;

import com.vibee.model.response.BaseResponse;
import com.vibee.service.pdf.ExportPDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/vibee/api/v1/file")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExportPFDQRController {
    private ExportPDFService exportPDFService;
    public static final String ATTACHMENT_FILENAME = "attachment; filename=";
    @Autowired
    public ExportPFDQRController(ExportPDFService exportPDFService) {
        this.exportPDFService = exportPDFService;
    }

    @GetMapping(value = "/pdf/download")
    public ResponseEntity<ByteArrayResource> printQRCodePDF(@RequestParam(value = "productCode") String productCode,
                                         @RequestParam(value = "amount") int amount,
                                         @RequestParam(value = "language", defaultValue = "vi") String language) {
        ByteArrayResource resource = exportPDFService.printQRCodePDF(productCode, amount, language);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT_FILENAME + "qr-code.pdf")
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }
}
