package com.vibee.model.response.user;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.util.Map;

@Data
public class EmailResponse extends BaseResponse {
    private String to;
    private String subject;
    private String content;
    private Map<String, Object> props;
}
