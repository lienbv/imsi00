package com.vibee.model.response.auth;

import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse extends BaseResponse {
    private String username;
    private String accessToken;
    private String role;
    private String refreshToken;
}
