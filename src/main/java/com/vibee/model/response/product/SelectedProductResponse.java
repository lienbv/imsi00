package com.vibee.model.response.product;

import com.vibee.model.item.SelectExportItem;
import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SelectedProductResponse extends BaseResponse {
    private SelectedProductResult result;
}
