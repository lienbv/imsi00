package com.vibee.model.response.product;

import com.vibee.model.item.FilterItem;
import com.vibee.model.item.GetProductItem;
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
public class GetProductResponse extends BaseResponse {
	private List<GetProductItem> items;
	private int totalItems;
	private int totalPages;
	private int page;
	private int pageSize;
	private FilterItem filter;
}
