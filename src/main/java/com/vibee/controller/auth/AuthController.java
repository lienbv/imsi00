package com.vibee.controller.auth;

import com.vibee.model.request.user.ChangePasswordRequest;
import com.vibee.model.request.user.EmailRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.auth.LoginResponse;
import com.vibee.service.vauth.LoginService;
import com.vibee.service.vauth.LogoutService;
import com.vibee.service.vauth.RefreshTokenService;
import com.vibee.service.vuser.IprofileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/vibee/api/v1/auth")
@CrossOrigin("*")
public class AuthController {
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final RefreshTokenService refreshTokenService;
    private final IprofileService iprofileService;

    @Autowired
    public AuthController(LoginService loginService,
                          LogoutService logoutService,
                          RefreshTokenService refreshTokenService, IprofileService iprofileService){
        this.loginService=loginService;
        this.logoutService=logoutService;
        this.refreshTokenService=refreshTokenService;
        this.iprofileService = iprofileService;
    }

    @GetMapping("/login")
    public LoginResponse login(@RequestHeader("Authorization") String tokenBasic,@RequestParam("language") String language){
        return this.loginService.login(tokenBasic,language);
    }

    @GetMapping("/refresh-token")
    public LoginResponse refreshToken(@RequestHeader("Authorization") String refreshToken,@RequestParam("token") String token,@RequestParam("language") String language){
        return this.loginService.login(refreshToken,language);
    }

    @GetMapping("/logout")
    public BaseResponse logout(@RequestHeader("Authorization") String refreshToken,@RequestParam("token") String token,@RequestParam("language") String language){
        return null;
    }
    @PostMapping(value = "/forgot", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse forgotPass(@Valid @RequestBody EmailRequest emailRequest, BindingResult bindingResult) {
        return this.iprofileService.sendEmail(emailRequest, bindingResult);

    }

}
