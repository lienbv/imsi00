package com.vibee.model.request.v_unit;

import com.vibee.model.item.UnitItemEdit;
import com.vibee.model.request.BaseRequest;
import lombok.Data;

@Data
public class UnitDeleteParentRequest extends BaseRequest {
    private int idDelete;
    private int idParent;
    private UnitItemEdit listEdit[];
}
