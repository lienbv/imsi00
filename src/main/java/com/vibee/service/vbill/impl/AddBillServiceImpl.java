package com.vibee.service.vbill.impl;

import com.vibee.config.redis.RedisAdapter;
import com.vibee.entity.VBill;
import com.vibee.entity.VDetailBill;
import com.vibee.entity.VExport;
import com.vibee.entity.VWarehouse;
import com.vibee.model.Status;
import com.vibee.model.request.bill.TransactionBillRequest;
import com.vibee.model.request.bill.ViewBillRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.product.SelectedProductResult;
import com.vibee.model.result.CreateDetailBillResult;
import com.vibee.model.result.TransactionBillResult;
import com.vibee.repo.VBillRepo;
import com.vibee.repo.VDetailBillRepo;
import com.vibee.repo.VExportRepo;
import com.vibee.repo.VWarehouseRepo;
import com.vibee.service.vbill.AddBillService;
import com.vibee.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AddBillServiceImpl implements AddBillService {

    private final VBillRepo billRepo;
    private final VDetailBillRepo detailBillRepo;
    private final VWarehouseRepo warehouseRepo;
    private final VExportRepo exportRepo;
    private final RedisAdapter redisAdapter;

    @Autowired
    public AddBillServiceImpl(VBillRepo billRepo,
                              VDetailBillRepo detailBillRepo,
                              VWarehouseRepo warehouseRepo,
                              VExportRepo exportRepo, RedisAdapter redisAdapter){
        this.billRepo=billRepo;
        this.detailBillRepo=detailBillRepo;
        this.warehouseRepo=warehouseRepo;
        this.exportRepo=exportRepo;
        this.redisAdapter = redisAdapter;
    }

    @Override
    public BaseResponse add(TransactionBillRequest request) {
        BaseResponse response=new BaseResponse();
        BigDecimal inPrice=request.getInPrice();
        BigDecimal sumPrice= BigDecimal.valueOf(0);
        String creator="";
        String paymentMethod= request.getPaymentMethod();
        String transactionType= request.getTransactionType();
        String language=request.getLanguage();
        VBill bill=new VBill();
        bill.setCreatedDate(new Date());
        bill.setCreator(creator);
        bill.setPrice(sumPrice);
        bill.setInPrice(inPrice);
        bill.setPaymentMethods(paymentMethod);
        bill.setStatus(1);
        bill.setTransactionType(transactionType);
        bill.setTotalPriceDebt(BigDecimal.valueOf(0));
        bill=billRepo.save(bill);
        List<VDetailBill> detailBills=new ArrayList<>();
        for (SelectedProductResult result:request.getViewStallResults()){
            VDetailBill detailBill=new VDetailBill();
            BigDecimal price=result.getExportSelected().getOutPrice().divide(BigDecimal.valueOf(result.getAmount()));
            sumPrice=sumPrice.add(price);
            detailBill.setPrice(price);
            detailBill.setBillId(bill.getId());
            detailBill.setAmount(result.getAmount());
            detailBill.setCreator(creator);
            detailBill.setUnitId(result.getExportSelected().getUnitId());
            detailBill.setStatus(1);
            detailBill.setCreatedDate(new Date());
            detailBill.setImportId(result.getImportId());
            detailBills.add(detailBill);
            VExport export=this.exportRepo.getById(result.getExportSelected().getExportId());
            export.setOutAmount(export.getOutAmount()+result.getAmount());
            this.exportRepo.save(export);
            VWarehouse warehouse=this.warehouseRepo.getWarehouseByImportId(result.getImportId());
            warehouse.setOutPrice(warehouse.getOutPrice().add(price));
            warehouse.setOutAmount(warehouse.getOutAmount()+result.getAmount());
            this.warehouseRepo.save(warehouse);
        }
        if (request.getPaymentMethod().equals("payment")){
            bill.setInPrice(sumPrice);
        }
        bill.setPrice(sumPrice);
        this.billRepo.save(bill);
        this.detailBillRepo.saveAll(detailBills);
        //xoa cache redis theo cartCode
        this.redisAdapter.delete(request.getCartCode());
        //ket thuc chuc nang
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success.transaction.bill"));
        response.getStatus().setStatus(Status.Success);
        return response;
    }

    @Override
    public BaseResponse saveRedis(TransactionBillRequest request) {
        BaseResponse response=new BaseResponse();
        String language=request.getLanguage();
        String cartCode=request.getCartCode();
        boolean isExist=this.redisAdapter.exists(cartCode);
        if (isExist){
            this.redisAdapter.delete(cartCode);
        }
        this.redisAdapter.set(cartCode,84600,request);
//        this.redisAdapter.sets(cartCode,84600,results);
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success.save.redis"));
        response.getStatus().setStatus(Status.Success);
        return response;
    }
}
