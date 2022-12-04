package com.vibee.model.item;


import com.vibee.model.request.BaseRequest;
import lombok.Data;

import java.util.Date;

@Data
public class CreateItemsUser extends BaseRequest {
    private  int id;
    private  String username;
    private  String fullname;
    private  String password;
    private  String cccd;
    private  String address;
    private  String numberPhone;
    private  String email;
    private  int status_code;
    private Date birthday;
    private  int idUserRole;
}