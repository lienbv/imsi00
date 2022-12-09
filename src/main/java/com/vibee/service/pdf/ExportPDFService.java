package com.vibee.service.pdf;

import com.vibee.model.response.BaseResponse;

public interface ExportPDFService {
    public BaseResponse printQRCodePDF( String productCode,int amount,String language);
}
