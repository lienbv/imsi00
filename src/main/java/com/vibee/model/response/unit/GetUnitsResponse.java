package com.vibee.model.response.unit;

import com.vibee.model.item.FilterItem;
import com.vibee.model.item.UnitsItem;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.result.GetUnitResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetUnitsResponse extends BaseResponse {
    private int totalItems;
    private int totalPages;
    private List<UnitsItem> list;
    private int page;
    private int pageSize;
    private List<GetUnitResult> results;
}
