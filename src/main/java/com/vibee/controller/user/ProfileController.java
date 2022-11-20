package com.vibee.controller.user;

import com.vibee.model.request.user.ChangePasswordRequest;
import com.vibee.model.request.user.UpdateAccountRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.user.ProfileResponse;
import com.vibee.service.vuser.IprofileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/vibee/api/v1/auth")
@CrossOrigin("*")
public class ProfileController {

    private final IprofileService iprofileService;

    @Autowired
    public ProfileController(IprofileService iprofileService) {
        this.iprofileService = iprofileService;
    }
    @GetMapping("/profile")
    public ProfileResponse profile(){
        return iprofileService.profile();
    }
    @PostMapping("/profile/update")
    public BaseResponse updateAccount(@Valid @RequestBody UpdateAccountRequest request, BindingResult bindingResult){
        return iprofileService.updateAccount(request, bindingResult);
    }
    @PostMapping(value = "/change")
    public BaseResponse change(@Valid @RequestBody ChangePasswordRequest request, BindingResult bindingResult){
        return this.iprofileService.changePassword(request, bindingResult);
    }
}
