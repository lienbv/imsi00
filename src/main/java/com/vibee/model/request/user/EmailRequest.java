package com.vibee.model.request.user;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class EmailRequest extends BaseRequest {
    @NotNull(message="msg.email.blank")
    @Email(message="msg.email.format")
    private String email;

    @NotNull(message="msg.username.blank")
    private String username;

}
