package com.vibee.model.request.category;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

@Data
public class EditTypeProductResponse extends BaseResponse {
    private String name;
    private String description;
    private int parentid;
    private int id;
}
