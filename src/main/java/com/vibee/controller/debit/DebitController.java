package com.vibee.controller.debit;


import com.vibee.model.item.FilterItem;
import com.vibee.model.request.debit.DebitPageRequest;
import com.vibee.model.request.debit.DebitRequest;
import com.vibee.model.request.debit.ListPayRequest;
import com.vibee.model.request.debit.PayRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.bill.GetTopTen;
import com.vibee.model.response.debit.DebitDetailResponse;
import com.vibee.model.response.debit.DebitItemsResponse;
import com.vibee.model.response.debit.DebitOfUserResponse;
import com.vibee.model.response.debit.GetDetailBill;
import com.vibee.service.debit.DebitImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("${vibee.config}/debit")
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
    @PostMapping("updateDebit/{idDebit}")
    public BaseResponse updateDebit(@PathVariable(name = "idDebit")int idDebit, @Valid @RequestBody DebitRequest request, BindingResult bindingResult){
        return this.debit.updateDebit( idDebit, request, bindingResult);
    }

    @GetMapping("findAll/{idUser}")
    public DebitItemsResponse findAll(@RequestParam(name = "pagenumber") int pageNumberReq,
                                      @RequestParam(name = "pagesize") int pageSizeReq,
                                      @RequestParam(name = "typefilter") String typeFilterReq,
                                      @RequestParam(name = "valuefilter") String valueFilterReq,
                                      @RequestParam(name= "language") String languageReq,
                                      @RequestParam(name = "search") String searchReq,
                                      @PathVariable(name = "idUser")int idUser){
        DebitPageRequest request = new DebitPageRequest();
        request.setPageNumber(pageNumberReq);
        request.setPageSize(pageSizeReq);
        request.setFilter(new FilterItem(typeFilterReq, valueFilterReq));
        request.setLanguage(languageReq);
        request.setSearchText(searchReq);
        return this.debit.findAll(idUser,request);
    }
    @GetMapping("listUserDebit")
    public DebitOfUserResponse listUserDebit(

                                      @RequestParam(name = "pagenumber") int pageNumberReq,
                                      @RequestParam(name = "pagesize") int pageSizeReq,
                                      @RequestParam(name = "typefilter") String typeFilterReq,
                                      @RequestParam(name = "valuefilter") String valueFilterReq,
                                      @RequestParam(name= "language") String languageReq,
                                      @RequestParam(name = "search") String searchReq){
        DebitPageRequest request =new DebitPageRequest();
        request.setPageNumber(pageNumberReq);
        request.setPageSize(pageSizeReq);
        request.setFilter(new FilterItem(typeFilterReq, valueFilterReq));
        request.setLanguage(languageReq);
        request.setSearchText(searchReq);
        return this.debit.listUserDebit(request);
    }

    @GetMapping("/topTen-bill")
    public GetTopTen getBill(){
        return this.debit.getBill();
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
