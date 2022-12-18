package com.vibee.model.request.product;

import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest extends BaseRequest {
	private int productId;
	private String productName;
	private int statusCode;
	private String barCode;
	private String description;
	private int categoryId;
}
