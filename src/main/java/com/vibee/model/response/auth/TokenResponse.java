package com.vibee.model.response.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponse {
    private String access_token;
    private String scope;
    private String session_state;
    private int expires_in;
    private int refresh_expires_in;
    private String refresh_token;
    private String token_type;
}
