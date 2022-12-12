package com.vibee.model.response.user;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

@Data
public class CreateUserResponse extends BaseResponse {

    private String fullname;
    private String statusName;
    private String phoneNumber;
    private String email;
    private int role;
    private String nameRole;
    private String username;
    private String cccd;
    private int idUser;
    private String address;

}
