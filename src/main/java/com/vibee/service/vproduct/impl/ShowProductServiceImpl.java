package com.vibee.service.vproduct.impl;

import com.vibee.entity.VUploadFile;
import com.vibee.model.ObjectResponse.SelectExportStallObject;
import com.vibee.model.ObjectResponse.ShowProductStaff;
import com.vibee.model.item.ShowProductItems;
import com.vibee.model.response.product.ShowListProduct;
import com.vibee.repo.*;
import com.vibee.service.vproduct.ShowListProductImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class ShowProductServiceImpl implements ShowListProductImp {
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

    @Override
    public ShowListProduct showProduct() {
        List<ShowProductStaff> showProduct = this.vProductRepo.showProduct();
        List<ShowProductItems> items = new ArrayList<>();
        ShowListProduct response = new ShowListProduct();
        for (ShowProductStaff item: showProduct){
//            List<SelectExportStallObject> exportStalls=this.vExportRepo.getExportsId(item.getImportID());
            ShowProductItems req = new ShowProductItems();
            req.setId(item.getId());
            req.setExpired(item.getExpired());
            req.setBarcode(item.getBarcode());
            req.setDescription(item.getDescription());
            req.setProductName(item.getProductName());
            req.setFiles(item.getFiles());
            VUploadFile vUploadFile = this.fileUploadRepo.findById(item.getFiles());
            req.setImg(vUploadFile.getFileName());
            req.setFileImport(item.getFileImport());
            req.setQrCode(item.getQrCode());
            req.setImportID(item.getImportID());
//            req.setUnit(exportStalls);
            items.add(req);
        }
        response.setItems(items);
        return response;
    }
    public SelectExportStallObject getUnit(int id, int unitId) {
        SelectExportStallObject exportStalls=this.vExportRepo.getExportsUnitId(id, unitId);
        return exportStalls;
    }


}
