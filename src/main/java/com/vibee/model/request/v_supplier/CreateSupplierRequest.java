package com.vibee.model.request.v_supplier;

import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSupplierRequest extends BaseRequest {
    private int id;

    @NotBlank(message ="msg.nameSup.blank")
    @Size(max = 255, min = 2, message = "msg.nameSup.length")
    private String nameSup;
    @NotNull(message = "msg.address.blank")
    private String address;

    @NotBlank(message ="msg.email.blank")
    @Email(message="msg.email.format")
    private String email;

    @NotBlank(message ="msg.phonenumber.blank")
    private String numberPhone;
}
