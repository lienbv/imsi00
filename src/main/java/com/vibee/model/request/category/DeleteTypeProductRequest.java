package com.vibee.model.request.category;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

@Data
public class DeleteTypeProductRequest extends BaseRequest {
    private int id;
}
