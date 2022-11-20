package com.vibee.model.response.product;

import com.vibee.model.item.GetProductItem;
import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LockResponse extends BaseResponse {
    private GetProductItem item;
}
