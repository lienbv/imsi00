package com.vibee.service.vauth;

import com.vibee.model.item.CredentialItem;
import com.vibee.model.response.BaseResponse;

public interface UpdatePasswordKeycloakService {
    BaseResponse updatePassword(CredentialItem password ,String language);
}
