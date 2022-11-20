package com.vibee.model.response.product;

import com.vibee.model.item.FilterItem;
import com.vibee.model.item.PageItem;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.category.CategoryItem;
import com.vibee.model.response.export.ExportStallItem;
import com.vibee.model.result.ViewStallResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewStallResponse extends BaseResponse {
    private List<ViewStallResult> results;
}
