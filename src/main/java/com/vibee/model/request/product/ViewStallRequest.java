package com.vibee.model.request.product;

import com.vibee.model.item.FilterItem;
import com.vibee.model.item.PageItem;
import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewStallRequest extends BaseRequest {
    private String searchValue="";
}
