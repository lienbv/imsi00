package com.vibee.service.vproduct.impl;

import com.vibee.repo.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ShowProductServiceImpl {
    private final VSupplierRepo vSupplierRepo;
    private final VProductRepo vProductRepo;
    private final VTypeProductRepo vTypeProductRepo;
    private final VUnitRepo vUnitRepo;
    private final VFileUploadRepo fileUploadRepo;
    private final VImportRepo vImportRepo;
    private final ImportRedisRepo importRedisRepo;
    private final VWarehouseRepo vWarehouseRepo;
    private final VExportRepo vExportRepo;

    public ShowProductServiceImpl(VSupplierRepo vSupplierRepo, VProductRepo vProductRepo,
                                  VTypeProductRepo vTypeProductRepo, VUnitRepo vUnitRepo,
                                  VFileUploadRepo fileUploadRepo, VImportRepo vImportRepo,
                                  ImportRedisRepo importRedisRepo, VWarehouseRepo vWarehouseRepo,
                                  VExportRepo vExportRepo) {
        this.vSupplierRepo = vSupplierRepo;
        this.vProductRepo = vProductRepo;
        this.vTypeProductRepo = vTypeProductRepo;
        this.vUnitRepo = vUnitRepo;
        this.fileUploadRepo = fileUploadRepo;
        this.vImportRepo = vImportRepo;
        this.importRedisRepo = importRedisRepo;
        this.vWarehouseRepo = vWarehouseRepo;
        this.vExportRepo = vExportRepo;
    }

}
