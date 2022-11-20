package com.vibee.model.response.warehouse;

import com.vibee.model.item.*;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.result.GetProductResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetWereHousesResponse extends BaseResponse {
    private List<WarehouseItem> getWarehouseItems;
    private PageItem pageItem=new PageItem();
    private FilterItem filterItem;
    private GetProductResult getProductResult;
    private GetLastImportItem getLastImportItem;
    private List<GetCharWareHouseItem> getCharWareHouseItems;
}
