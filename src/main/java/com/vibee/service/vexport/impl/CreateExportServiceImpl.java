package com.vibee.service.vexport.impl;

import com.vibee.entity.VExport;
import com.vibee.model.Status;
import com.vibee.model.item.UnitItem;
import com.vibee.model.request.v_export.CreateExportRequest;
import com.vibee.model.response.export.CreateExportResponse;
import com.vibee.repo.VExportRepo;
import com.vibee.repo.VImportRepo;
import com.vibee.service.vexport.CreateExportService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class CreateExportServiceImpl implements CreateExportService {

    private final VImportRepo importRepo;
    private final VExportRepo exportRepo;

    @Autowired
    public CreateExportServiceImpl(VImportRepo importRepo,
                                   VExportRepo exportRepo){
        this.importRepo=importRepo;
        this.exportRepo=exportRepo;
    }
    @Override
    public CreateExportResponse create(CreateExportRequest request) {
        log.info("Create export warehouse service:: BEGIN");
        int importId=request.getImportId();
        String language=request.getLanguage();
        List<UnitItem> unitItems=request.getUnitItems();
        String creator="vibeefirst1910";
        CreateExportResponse response=new CreateExportResponse();
        Boolean isExistImportProduct=this.importRepo.existsById(importId);
        if (Boolean.FALSE.equals(isExistImportProduct)){
            log.error("export warehouse :: Import warehouse not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"product.not.found"));
            return response;
        }
        List<VExport> exports=new ArrayList<>();
        for (UnitItem unitItem:unitItems){
            VExport export=new VExport();
            export.setStatus(1);
            export.setCreator(creator);
            export.setCreatedDate(new Date());
            export.setInPrice(unitItem.getInPrice());
            export.setOutAmount(0);
            export.setOutPrice(unitItem.getOutPrice());
            export.setUnitId(unitItem.getUnitId());
            export.setImportId(importId);
            exports.add(export);
        }
        exports=this.exportRepo.saveAll(exports);
        if (exports.isEmpty()){
            log.error("export warehouse :: Import warehouse not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.create.export.is.failed"));
            return response;
        }
        response.setExportId(request.getImportId());
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success.create.export.product"));
        return response;
    }
}
