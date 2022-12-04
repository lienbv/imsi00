package com.vibee.model.response.v_supplier;

import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSupplierResponse extends BaseResponse {
    private int id;
    private String nameSup;

}