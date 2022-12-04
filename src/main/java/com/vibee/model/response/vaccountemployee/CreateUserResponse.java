package com.vibee.model.response.vaccountemployee;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.util.Date;

@Data
public class CreateUserResponse extends BaseResponse {

    private String fullname;
    private String statusName;
    private String phoneNumber;
    private String email;
    private int role;
    private String nameRole;
    private Date birthday;
}
