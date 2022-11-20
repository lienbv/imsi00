package com.vibee.controller.bill;

import com.vibee.model.request.bill.TransactionBillRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.service.vbill.AddBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/vibee/api/v1/bill")
@RestController
public class TransactionController {

    private final AddBillService addBillService;
    @Autowired
    public TransactionController(AddBillService addBillService){
        this.addBillService=addBillService;
    }

    @PostMapping("/transaction")
    public BaseResponse create(@RequestBody TransactionBillRequest request){
        return this.addBillService.add(request);
    }
}
