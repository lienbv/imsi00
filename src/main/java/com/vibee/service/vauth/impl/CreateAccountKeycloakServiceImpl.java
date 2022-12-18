package com.vibee.service.vauth.impl;

import com.vibee.model.Status;
import com.vibee.model.item.CredentialItem;
import com.vibee.model.request.auth.CreateAccountRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.service.vauth.CreateAccountKeycloakService;
import com.vibee.service.vcall.CallRealmAdminCli;
import com.vibee.utils.CommonUtil;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Log4j2
@Service
public class CreateAccountKeycloakServiceImpl implements CreateAccountKeycloakService {
    @Autowired
    private final CallRealmAdminCli callRealmAdminCli;
    public CreateAccountKeycloakServiceImpl(CallRealmAdminCli callRealmAdminCli) {
        this.callRealmAdminCli = callRealmAdminCli;
    }
    @Override
    public BaseResponse createAccount(CreateAccountRequest request, String language){
        BaseResponse response = new BaseResponse();
        try {
            log.info("CreateUserService :: CallRealmAdminCli :: Start :: createAccount :: {}", CommonUtil.beanToString(request));
            this.callRealmAdminCli.createAccount(request);
            response.getStatus().setStatus(Status.Success);
            response.getStatus().setMessage(MessageUtils.get(language,"create.account.success"));
            log.info("CreateUserService :: CallRealmAdminCli :: End :: createAccount :: {}", response);
            return response;
        }catch (Exception e){
            log.error("CreateUserService :: Error", e);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.error"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
    }

    @Override
    public void setRoleByUser(String userId, Map<String, ?> request) throws Exception {

    }
}
