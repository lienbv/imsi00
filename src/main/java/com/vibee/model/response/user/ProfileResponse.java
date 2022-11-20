package com.vibee.model.response.user;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

@Data
public class ProfileResponse extends BaseResponse {
    private String fullname;
    private String numberPhone;
    private String email;
    private String address;
    private String username;
    private String cccd;
}
