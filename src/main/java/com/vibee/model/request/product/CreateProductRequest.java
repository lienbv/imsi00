package com.vibee.model.request.product;

import com.vibee.model.item.UnitItem;
import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateProductRequest extends BaseRequest {
	private String nameProd;
	private String unit;
	private String description;
	private int amount;
	private int categoryId;
	private int supplierId;
	private String barCode;
	private int unitId;
	private List<UnitItem> units;
	private BigDecimal inPrice;
	private int fileId;
}
