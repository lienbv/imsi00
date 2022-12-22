package com.vibee.model.request.category;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

@Data
public class CreateTypeProductDetailRequest extends BaseRequest {
    private String id;
    private String name;
    private String description;
}
