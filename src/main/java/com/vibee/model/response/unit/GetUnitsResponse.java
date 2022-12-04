package com.vibee.model.response.unit;

import com.vibee.model.item.UnitsItem;
import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUnitsResponse extends BaseResponse {
    private List<UnitsItem> list = new ArrayList<>();

    private int totalItems;
    private int totalPages;
    private int page;
    private int pageSize;
}
