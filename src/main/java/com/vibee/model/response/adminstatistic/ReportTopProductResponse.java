package com.vibee.model.response.adminstatistic;

import com.vibee.model.item.GetProductItem;
import com.vibee.model.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportTopProductResponse extends BaseResponse {
    private List<GetProductItem> items;
}
