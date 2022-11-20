package com.vibee.model.response.warehouse;

import com.vibee.model.item.FilterItem;
import com.vibee.model.item.PageItem;
import com.vibee.model.item.WarehouseItem;
import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilterWarehouseResponse extends BaseResponse {
    private PageItem pageItem=new PageItem();
    private FilterItem filterItem;
    private List<WarehouseItem> getWarehouseItems;
}
