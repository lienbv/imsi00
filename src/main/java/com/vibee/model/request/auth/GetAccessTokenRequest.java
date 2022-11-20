package com.vibee.model.request.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetAccessTokenRequest {
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String grantType;
}
