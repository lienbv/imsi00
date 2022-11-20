package com.vibee.model.response;

import com.vibee.model.Status;
import lombok.Data;

@Data
public class BaseResponse {
    private Status status=new Status();
}
