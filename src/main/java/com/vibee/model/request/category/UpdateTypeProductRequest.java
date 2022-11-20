package com.vibee.model.request.category;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

@Data
public class UpdateTypeProductRequest extends BaseRequest {
    private String name;
    private String description;
    private int parentId;
    private int id;
}
