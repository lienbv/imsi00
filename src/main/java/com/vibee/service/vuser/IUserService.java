package com.vibee.service.vuser;

import com.vibee.model.request.user.CheckAccountRequest;
import com.vibee.model.request.user.EditAccountRequest;
import com.vibee.model.request.user.UpdateAccountRequest;
import com.vibee.model.request.v_unit.GetOrderByStringRequest;
import com.vibee.model.request.vaccountemployee.CreateUserRequest;
import com.vibee.model.request.vaccountemployee.IdUserRoleRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.user.CreateUserResponse;
import com.vibee.model.response.user.GetUserItemsResponse;
import org.springframework.validation.BindingResult;


public interface IUserService {
    CreateUserResponse createAccount( CreateUserRequest request, BindingResult bindingResult);
    CreateUserResponse edit(EditAccountRequest request, String language);
    BaseResponse deleteAccount( IdUserRoleRequest request);
    BaseResponse unlockAccount(IdUserRoleRequest request);
    GetUserItemsResponse getAllAccount(GetOrderByStringRequest request);
    CreateUserResponse checkAccount( CheckAccountRequest request);
    CreateUserResponse checkAccountUpdate( CheckAccountRequest request);
    CreateUserResponse updateAccount( UpdateAccountRequest request, BindingResult bindingResult);
}
