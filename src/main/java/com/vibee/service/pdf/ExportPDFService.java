package com.vibee.service.pdf;

import com.vibee.model.response.BaseResponse;
import org.springframework.core.io.ByteArrayResource;

public interface ExportPDFService {
    ByteArrayResource printQRCodePDF(String productCode, int amount, String language);
}
