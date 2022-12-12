package com.vibee.service.vuser;

import com.vibee.model.request.user.UpdateAccountRequest;
import com.vibee.model.request.vaccountemployee.CreateUserRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.request.user.ChangePasswordRequest;
import com.vibee.model.request.user.EmailRequest;
import com.vibee.model.response.user.CreateUserResponse;
import com.vibee.model.response.user.ProfileResponse;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

public interface IprofileService {
    ProfileResponse profile();
    BaseResponse updateAccount(UpdateAccountRequest request, BindingResult bindingResult);
    BaseResponse sendEmail(EmailRequest emailRequest, BindingResult bindingResult);
    BaseResponse changePassword(ChangePasswordRequest request, BindingResult bindingResult);

}
