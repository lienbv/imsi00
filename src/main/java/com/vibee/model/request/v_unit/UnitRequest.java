package com.vibee.model.request.v_unit;

import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnitRequest extends BaseRequest {
    private int id;
    private String unitName;
    private int parentId;
    private int childId;
    private String description;
    private int amount;
}
