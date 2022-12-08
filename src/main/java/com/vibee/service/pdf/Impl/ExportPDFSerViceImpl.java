package com.vibee.service.pdf.Impl;

import com.vibee.entity.*;
import com.vibee.model.Status;
import com.vibee.model.response.BaseResponse;
import com.vibee.repo.*;
import com.vibee.service.pdf.ExportPDFQR;
import com.vibee.service.pdf.ExportPDFService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ExportPDFSerViceImpl implements ExportPDFService {
    @Autowired
    private VImportRepo importRepo;

    @Autowired
    private VUploadFileRepo uploadFileRepo;

    @Autowired
    private VWarehouseRepo warehouseRepo;

    @Autowired
    private VProductRepo vProductRepo;

    @Autowired
    private VUnitRepo vUnitRepo;

    @Override
    public BaseResponse printQRCodePDF(String productCode, int amount, String language) {
        VImport vImport = importRepo.findByProductCode(productCode);
        VWarehouse vWarehouse = warehouseRepo.getById(vImport.getWarehouseId());
        VProduct vProduct = vProductRepo.getById(vWarehouse.getProductId());
        VUnit vUnit = vUnitRepo.findById(vImport.getUnitId()).get();
        VUploadFile uploadFile = uploadFileRepo.getById(vImport.getFileId());
        ExportPDFQR.export(amount,productCode,vWarehouse.getOutPrice(), vProduct.getProductName(), uploadFile.getUrl(), vUnit.getUnitName());
        BaseResponse response = new BaseResponse();
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("Success");
        return  response;
    }
}
