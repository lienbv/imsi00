package com.vibee.service.vauth.impl;

import com.vibee.model.Status;
import com.vibee.model.item.CredentialItem;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.auth.UserInfoResponse;
import com.vibee.service.vauth.UpdatePasswordKeycloakService;
import com.vibee.service.vcall.CallKeycloakService;
import com.vibee.service.vcall.CallRealmAdminCli;
import com.vibee.utils.CommonUtil;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Log4j2
public class UpdatePasswordKeycloakServiceImpl implements UpdatePasswordKeycloakService {
    private final CallRealmAdminCli callRealmAdminCli;
    private final HttpServletRequest servletRequest;
    private final CallKeycloakService callKeycloakService;

    public UpdatePasswordKeycloakServiceImpl(CallRealmAdminCli callRealmAdminCli,
                                             HttpServletRequest servletRequest,
                                             CallKeycloakService callKeycloakService) {
        this.callRealmAdminCli = callRealmAdminCli;
        this.servletRequest = servletRequest;
        this.callKeycloakService = callKeycloakService;
    }
    @Override
    public BaseResponse updatePassword(CredentialItem password,String language){
        log.info("UpdatePasswordKeycloakServiceImpl :: CallRealmAdminCli :: Start :: updatePassword :: {}", password);
        BaseResponse response = new BaseResponse();
        try {
            UserInfoResponse userInfoResponse = this.getUserInfo();
            if (userInfoResponse==null){
                response.getStatus().setStatus(Status.Fail);
                response.getStatus().setMessage(MessageUtils.get(language,"error.update.password"));
                return response;
            }
            String userId = userInfoResponse.getSub();
            callRealmAdminCli.resetPassword(userId,password);
        } catch (Exception e) {
            log.error("UpdatePasswordKeycloakServiceImpl :: CallRealmAdminCli :: Error :: updatePassword :: {}", e.getMessage());
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"update.password.fail"));
        }
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"update.password.success"));
        log.info("UpdatePasswordKeycloakServiceImpl :: CallRealmAdminCli :: End :: updatePassword :: {}", CommonUtil.beanToString(response));
        return response;
    }

    private UserInfoResponse getUserInfo(){
        log.info("UpdatePasswordKeycloakServiceImpl :: CallRealmAdminCli :: Start");
        String token=servletRequest.getHeader("Authorization");
        UserInfoResponse userInfoResponse = callKeycloakService.getUserInfo(token);
        log.info("UpdatePasswordKeycloakServiceImpl :: CallRealmAdminCli :: End :: getUserInfo :: {}", CommonUtil.beanToString(userInfoResponse));
        return userInfoResponse;
    }
}
