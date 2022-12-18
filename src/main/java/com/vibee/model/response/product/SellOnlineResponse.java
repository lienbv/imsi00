package com.vibee.model.response.product;

import com.vibee.model.item.FilterItem;
import com.vibee.model.item.PageItem;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.result.SellOnlineResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SellOnlineResponse extends BaseResponse {
    private PageItem pageItem;
    private List<SellOnlineResult>  sellOnlineResults;
}
