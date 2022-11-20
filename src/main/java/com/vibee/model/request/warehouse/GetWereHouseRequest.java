package com.vibee.model.request.warehouse;

import com.vibee.model.item.FilterItem;
import com.vibee.model.item.PageItem;
import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetWereHouseRequest extends BaseRequest {
    private PageItem pageItem=new PageItem();
    private FilterItem filterItem=new FilterItem();
    private int productId;
}
