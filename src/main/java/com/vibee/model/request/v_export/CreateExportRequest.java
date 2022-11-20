package com.vibee.model.request.v_export;

import com.vibee.model.item.UnitItem;
import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExportRequest extends BaseRequest {
    private int importId;
    private List<UnitItem> unitItems;
}
