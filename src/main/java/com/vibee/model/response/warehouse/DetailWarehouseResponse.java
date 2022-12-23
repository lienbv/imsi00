package com.vibee.model.response.warehouse;

import com.vibee.model.item.ExportItems;
import com.vibee.model.item.GetExportItems;
import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetailWarehouseResponse extends BaseResponse {
    private String supplierName;
    private String expireDate;
    private String productCode;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private Double inAmount;
    private Double outAmount;
    private String unitName;
    private List<GetExportItems> exportItems;
}
