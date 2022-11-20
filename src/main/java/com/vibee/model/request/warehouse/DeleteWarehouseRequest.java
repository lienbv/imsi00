package com.vibee.model.request.warehouse;

import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteWarehouseRequest extends BaseRequest {
    private int wereHouseId;
}
