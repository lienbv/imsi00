package com.vibee.service.vproduct.impl;

import com.vibee.entity.VProduct;
import com.vibee.entity.VUploadFile;
import com.vibee.model.Status;
import com.vibee.model.item.GetProductItem;
import com.vibee.model.request.product.DeleteProductRequest;
import com.vibee.model.request.product.LockRequest;
import com.vibee.model.response.product.DeleteProductResponse;
import com.vibee.model.response.product.LockResponse;
import com.vibee.repo.VFileUploadRepo;
import com.vibee.repo.VProductRepo;
import com.vibee.service.vproduct.CloseProductService;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.ProductUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;

@Service
@Log4j2
public class CloseProductServiceImpl implements CloseProductService {

    private final VProductRepo productRepo;
    private final VFileUploadRepo fileUploadRepo;
    @Autowired
    public CloseProductServiceImpl(VProductRepo productRepo,
                                   VFileUploadRepo fileUploadRepo){
        this.productRepo=productRepo;
        this.fileUploadRepo=fileUploadRepo;
    }
    @Override
    public LockResponse lock(LockRequest Request){
        log.info("LockService:: START");
        int productId=Request.getProductId();
        String language=Request.getLanguage();
        LockResponse response=new LockResponse();
        VProduct product=this.productRepo.getById(productId);
        if(product==null){
            log.error("Product is not exits");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.not.already.exit"));
            return response;
        }
        int lockProduct=this.productRepo.lock(productId);
        if(lockProduct==0){
            log.error("Update product is failed");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.update.failed"));
            return response;
        }
        GetProductItem item=new GetProductItem();
        item.setProductCode(product.getId());
        item.setStatusName(ProductUtils.getstatusname(3,language));
        item.setStatusCode(3);
        response.setItem(item);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.product.lock.success"));
        log.info("LockService:: END");
        return response;
    }

    @Override
    public LockResponse unLock(LockRequest Request){
        log.info("LockService:: START");
        int productId=Request.getProductId();
        String language=Request.getLanguage();
        LockResponse response=new LockResponse();
        VProduct product=this.productRepo.getById(productId);
        if(product==null){
            log.error("Product is not exits");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.not.already.exit"));
            return response;
        }
        int lockProduct=this.productRepo.unLock(productId);
        if(lockProduct==0){
            log.error("Update product is failed");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.update.failed"));
            return response;
        }
        GetProductItem item=new GetProductItem();
        item.setStatusName(ProductUtils.getstatusname(1,language));
        item.setStatusCode(1);
        response.setItem(item);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.product.unlock.success"));
        return response;
    }

    @Override
    public DeleteProductResponse DeleteProduct(DeleteProductRequest request, BindingResult result) {
        log.info("DeleteProductService:: START");
        DeleteProductResponse response=new DeleteProductResponse();
        int id=request.getProductId();
        String language=request.getLanguage();
        if(result.hasErrors()) {
            log.error("DELETE Product Request is Blank");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, result.getNestedPath()));
            return response;
        }
        VProduct checkProd=this.productRepo.getById(id);
        if(checkProd==null) {
            log.error("Product Request is not already exits");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.not.already.exit"));
            return response;
        }
        int delete = this.productRepo.delete(id);
        if(delete==0) {
            log.error("Delete Product is failed");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.delete.failed"));
            return response;
        }
        GetProductItem item=new GetProductItem();

        VUploadFile uploadFile=this.fileUploadRepo.getById(checkProd.getId());
        item.setImg(uploadFile.getUrl());
        item.setProductName(checkProd.getProductName());
        item.setProductCode(checkProd.getId());
//        item.setPrice(BigDecimal.valueOf(0));
        item.setStatusName(ProductUtils.getstatusname(4,language));
        item.setStatusCode(4);
        response.setItem(item);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.product.delete.success"));
        log.info("DeleteProductService:: END");
        return response;
    }
}
