package com.vibee.model.response.warehouse;

import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWarehouseResponse extends BaseResponse {
    private int warehouseId;
}
