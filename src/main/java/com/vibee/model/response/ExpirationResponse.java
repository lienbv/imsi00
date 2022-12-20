package com.vibee.model.response;

import com.vibee.model.item.ExpirationItem;
import lombok.Data;

import java.util.List;

@Data
public class ExpirationResponse extends BaseResponse{
    private List<ExpirationItem> list;
}
