package com.vibee.model.response.expired;

import com.vibee.model.item.CloseToExpirationItem;
import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class CloseToExpiresResponse extends BaseResponse {
    private List<CloseToExpirationItem> closeToExpirationItems;
}
