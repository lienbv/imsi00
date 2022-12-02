package com.vibee.model.request.v_customeraccount;

import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccontRequest extends BaseRequest {

    @NotNull(message = "msg.username.blank")
    @Pattern(regexp = "[a-z0-9_-]{6,12}$", message = "msg.username.format")
    private String username;

    @NotNull(message = "msg.fullname.blank")
    @Size(max = 255, min = 2, message = "msg.fullname.length")
    @Pattern(regexp = "^[a-zA-ZàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ]+(\\s[a-zA-ZàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ]+)*$",message = "msg.fullname.format")
    private  String fullname;

    @NotNull(message = "msg.password.blank")
    // @Pattern(regexp = "((?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!^*?&~]).{6,20})", message = "Mật khẩu không đúng định dạng")
    @Size(min = 6, max= 20,message = "msg.password.length")
    private  String password;

    @NotNull(message = "msg.cccd.blank")
    @Size(min = 12, max = 12 , message = "msg.cccd.length")
    private  String cccd;

    @NotBlank(message = "msg.address.blank")
    private  String address;

    @NotNull(message = "msg.phonenumber.blank")
    @Pattern(regexp = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$", message = "Số điện thoại không đúng định dạng")
    private  String numberPhone;

    @NotBlank(message="msg.email.blank")
    @Email(message="msg.email.format")
    private  String email;
}

