package com.vibee.service.v_customeraccount;

import com.vibee.model.request.v_customeraccount.CustomerAccontRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.v_customeraccount.CreateCustomerAccountResponse;
import com.vibee.model.response.v_customeraccount.CustomerAccountsResponse;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

public interface CustomerAccountService {
    public CustomerAccountsResponse listAll(String search, int status , int pageNumber, int size, String fullname, String address, String numberPhone, String cccd, String email);
    public CreateCustomerAccountResponse save(CustomerAccontRequest request, BindingResult bindingResult);
    public BaseResponse unlockAndLockAccount(int id, String language);
}
