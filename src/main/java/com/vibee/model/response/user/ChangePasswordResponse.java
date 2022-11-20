package com.vibee.model.response.user;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

@Data
public class ChangePasswordResponse extends BaseResponse {
    private String username;
}
