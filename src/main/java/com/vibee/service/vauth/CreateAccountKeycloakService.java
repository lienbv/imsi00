package com.vibee.service.vauth;

import com.vibee.model.request.auth.CreateAccountRequest;
import com.vibee.model.response.BaseResponse;

import java.util.Map;

public interface CreateAccountKeycloakService {
    BaseResponse createAccount(CreateAccountRequest request, String language);
    void setRoleByUser(String userId, Map<String, ?> request) throws Exception;
}
