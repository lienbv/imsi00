package com.vibee.service.vuser;

import com.vibee.model.request.user.UpdateAccountRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.request.user.ChangePasswordRequest;
import com.vibee.model.request.user.EmailRequest;
import com.vibee.model.response.user.ProfileResponse;
import org.springframework.validation.BindingResult;

public interface IprofileService {
    ProfileResponse profile();
    BaseResponse updateAccount(UpdateAccountRequest request, BindingResult bindingResult);
    BaseResponse sendEmail(EmailRequest emailRequest, BindingResult bindingResult);
    BaseResponse changePassword(ChangePasswordRequest request, BindingResult bindingResult);
}
