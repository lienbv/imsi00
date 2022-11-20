package com.vibee.model.response.warehouse;

import com.vibee.model.item.UnitItem;
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
public class InfoImportWarehouseResponse extends BaseResponse {
    private int productId;
    private String img;
    private List<UnitItem> unitItems;
    private String nameProd;
    private String barCode;
}
