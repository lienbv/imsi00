package com.vibee.service.vauth;

import com.vibee.model.response.BaseResponse;

public interface LogoutService {
    BaseResponse logout(String refreshToken, String token, String language);
}
