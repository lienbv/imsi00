package com.vibee.model.request.user;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

@Data
public class CheckAccountRequest extends BaseRequest {
    private String username;
    private  String cccd;
    private  String numberPhone;
    private  String email;
    private  int idUser;
}
