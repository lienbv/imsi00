package com.vibee.model.request.product;

import com.vibee.model.item.GetSupplierItem;
import com.vibee.model.item.GetTypeProductItem;
import com.vibee.model.item.InfoUnitItem;
import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoCreateProductResponse extends BaseResponse {
	private List<GetSupplierItem> items=new ArrayList<GetSupplierItem>();
	private List<GetTypeProductItem> typeProductItems=new ArrayList<GetTypeProductItem>();
	private List<InfoUnitItem> unitItems=new ArrayList<>();
}
