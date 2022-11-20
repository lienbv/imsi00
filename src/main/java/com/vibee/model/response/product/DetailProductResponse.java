package com.vibee.model.response.product;

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
public class DetailProductResponse extends BaseResponse {
	private int id;
	private String name;
	private String description;
	private String image;
	private double sumInAmount;
	private BigDecimal sumOutAmount;
	private String statusName;
	private int statusCode;
	private String barCode;
	private String categoryName;
	private List<String> supplierNames;
	private String createdDate;
	private String creator;
	private String importDate;
	private double inventory;
	private long sumImport;
	private String unitName;
}
