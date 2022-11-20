package com.vibee.model.request.category;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

@Data
public class CreateTypeProductRequest extends BaseRequest {
    private String name;
    private String description;
    private String parentId;
}
