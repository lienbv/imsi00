package com.vibee.model.response;

import com.vibee.model.Status;
import com.vibee.model.response.v_import.ImportWarehouseItemsResponse;
import lombok.Data;

@Data
public class BaseResponse {
    private Status status=new Status();
}
