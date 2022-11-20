package com.vibee.service.vauth;

import com.vibee.model.response.auth.LoginResponse;

public interface LoginService {
    LoginResponse login(String token, String language);
}
