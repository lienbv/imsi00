package com.vibee.model.response.unit;

import com.vibee.model.item.InfoUnitItem;
import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetUnitChildReponse extends BaseResponse {
    private List<InfoUnitItem> unitItems=new ArrayList<>();
}
