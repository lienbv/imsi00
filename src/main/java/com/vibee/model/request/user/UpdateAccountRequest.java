package com.vibee.model.request.user;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateAccountRequest extends BaseRequest {

//    @NotNull(message = "msg.fullname.blank")
//    @Size(max = 255, min = 2, message = "msg.fullname.length")
////    @Pattern(regexp = "^[a-zA-ZàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ]+(\\s[a-zA-ZàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ]+)*$",message = "msg.fullname.format")
//    private  String fullname;
//
//    @NotNull(message = "msg.cccd.blank")
//    @Size(min = 12, max = 12 , message = "msg.cccd.length")
//    private  String cccd;
//
//    @NotNull(message = "msg.address.blank")
//    private  String address;
//
//    @NotNull(message = "msg.phonenumber.blank")
//    @Pattern(regexp = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$", message = "Số điện thoại không đúng định dạng")
//    private  String numberPhone;
//
//    @NotNull(message="msg.email.blank")
//    @Email(message="msg.email.format")
//    private  String email;
    private String fullname;
    private String statusName;
    private String phoneNumber;
    private String email;
    private String nameRole;
    private String username;
    private String cccd;
    private int idUser;
    private String address;
}
