package com.vibee.controller.debit;


import com.vibee.model.item.FilterItem;
import com.vibee.model.request.debit.DebitPageRequest;
import com.vibee.model.request.debit.DebitRequest;
import com.vibee.model.request.debit.PayRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.debit.DebitDetailResponse;
import com.vibee.model.response.debit.DebitItemsResponse;
import com.vibee.model.response.debit.GetDetailBill;
import com.vibee.service.debit.DebitImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/vibee/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DebitController {
    private final DebitImpl debit;

    public DebitController(DebitImpl debit) {
        this.debit = debit;
    }

    @PostMapping("createDebit")
    public BaseResponse createDebit(@Valid @RequestBody DebitRequest request, BindingResult bindingResult){
       return this.debit.createDebit(request, bindingResult);
    }
    @GetMapping("findAll")
    public DebitItemsResponse findAll(@RequestParam(name = "pagenumber") int pageNumberReq,
                                      @RequestParam(name = "pagesize") int pageSizeReq,
                                      @RequestParam(name = "typefilter") String typeFilterReq,
                                      @RequestParam(name = "valuefilter") String valueFilterReq,
                                      @RequestParam(name= "language") String languageReq,
                                      @RequestParam(name = "search") String searchReq){
        DebitPageRequest request = new DebitPageRequest();
        request.setPageNumber(pageNumberReq);
        request.setPageSize(pageSizeReq);
        request.setFilter(new FilterItem(typeFilterReq, valueFilterReq));
        request.setLanguage(languageReq);
        request.setSearchText(searchReq);
        return this.debit.findAll(request);
    }
    @PostMapping("pay/{idDebit}")
    public BaseResponse pay(@PathVariable(name = "idDebit") int idDebit , @RequestParam(name = "inPrice")  BigDecimal inPrice,
                            @RequestParam(name = "language")  String language, BindingResult bindingResult){
       return this.debit.pay(idDebit, inPrice, language, bindingResult);
    }

    @GetMapping("/getDetailByBill/{bill}")
    public List<GetDetailBill> getDetailBill(@PathVariable(name = "bill")int bill){
    return this.debit.getDetailBill(bill);
    }
    @GetMapping("findById/{id}")
    public DebitDetailResponse findByIdDebitOfDebitDetail(@PathVariable(name = "id") int id){
        return this.debit.findByIdDebitOfDebitDetail(id);
    }
    @PostMapping("payDebit/{idDebit}")
    public BaseResponse payDebit(@Valid @RequestBody PayRequest request, @PathVariable(name = "idDebit") int idDebit, BindingResult bindingResult){
        return this.debit.payDebit(idDebit,request, bindingResult);
    }

}
