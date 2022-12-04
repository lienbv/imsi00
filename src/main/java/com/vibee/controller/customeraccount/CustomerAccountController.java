package com.vibee.controller.customeraccount;

import com.vibee.model.request.v_customeraccount.CustomerAccontRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.v_customeraccount.CreateCustomerAccountResponse;
import com.vibee.model.response.v_customeraccount.CustomerAccountsResponse;
import com.vibee.service.v_customeraccount.CustomerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vibee/api/v1/admins/customers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CustomerAccountController {
    private CustomerAccountService customerAccountSer;

    @Autowired
    public CustomerAccountController(CustomerAccountService customerAccountSer) {
        this.customerAccountSer = customerAccountSer;
    }

    @GetMapping("")
    public CustomerAccountsResponse homeCustomerAccount(
            @RequestParam(value="pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value="size", defaultValue = "10") int size,
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "status", defaultValue = "1") int status,
            @RequestParam(value = "fullname", defaultValue = "") String fullname,
            @RequestParam(value = "address", defaultValue = "") String address,
            @RequestParam(value = "numberPhone", defaultValue = "") String numberPhone,
            @RequestParam(value = "cccd", defaultValue = "") String cccd,
            @RequestParam(value = "email", defaultValue = "") String email) {
        return customerAccountSer.listAll(search, status , pageNumber, size, fullname, address, numberPhone, cccd, email);
    }

    @PostMapping("")
    public CreateCustomerAccountResponse save(@Valid @RequestBody CustomerAccontRequest request, BindingResult bindingResult) {
        return customerAccountSer.save(request, bindingResult);
    }

    @DeleteMapping("/{id}")
    public BaseResponse unlockAndLockAccount(@PathVariable(name = "id") int id, @RequestParam(name = "language", defaultValue = "vi") String language) {
        return customerAccountSer.unlockAndLockAccount(id, language);
    }
}
