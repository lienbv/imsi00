package com.vibee.model.request.user;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRequest extends BaseRequest {

    @NotNull(message = "msg.password.blank")
    private String oldPassword;
    @NotNull(message = "msg.password.blank1")
  //  @Pattern(regexp = "((?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!^*?&~]).{6,20})", message = "msg.password.format")
    @Size(min = 6, max= 20,message = "msg.password.length")
    private String newPassword;
    @NotNull(message = "msg.password.blank2")
 //   @Pattern(regexp = "((?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!^*?&~]).{6,20})", message = "msg.password.format")
    @Size(min = 6, max= 20,message = "msg.password.length")
    private String reEnterPassword;
}
