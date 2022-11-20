package com.vibee.service.vimport.impl;

import com.vibee.model.Status;
import com.vibee.model.request.warehouse.DeleteWarehouseRequest;
import com.vibee.model.response.warehouse.DeleteWarehouseResponse;
import com.vibee.repo.VImportRepo;
import com.vibee.service.vimport.DeleteWareHouseService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DeleteWareHouseServiceImpl implements DeleteWareHouseService {

    private final VImportRepo importRepo;

    public DeleteWareHouseServiceImpl(VImportRepo importRepo){
        this.importRepo = importRepo;
    }

    @Override
    public DeleteWarehouseResponse deleteWarehouse(DeleteWarehouseRequest request) {
        log.info("deleteWarehouse :: Start");
        DeleteWarehouseResponse response = new DeleteWarehouseResponse();
        int wereHouseId = request.getWereHouseId();
        String language = request.getLanguage();
//        Warehouse detailProduct=this.importRepo.getById(wereHouseId);
//        if (detailProduct==null){
//            log.error("deleteWereHouse :: DetailProduct not found");
//            response.getStatus().setStatus(Status.Fail);
//            response.getStatus().setMessage(MessageUtils.get(language,"werehouse.not.found"));
//            return response;
//        }
//        detailProduct.setStatus(2);
//        Warehouse dp=this.importRepo.save(detailProduct);
//        if(dp==null){
//            log.error("deleteWereHouse :: DetailProduct not found");
//            response.getStatus().setStatus(Status.Fail);
//            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.delete.werehouse"));
//            return response;
//        }
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success.delete.werehouse"));
//        response.setWereHouseId(dp.getId());
        log.info("deleteWarehouse :: End");
        return response;
    }
}
