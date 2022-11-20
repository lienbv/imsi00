package com.vibee.model.response.export;

import com.vibee.model.item.UnitItem;
import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExportResponse extends BaseResponse {
    private int exportId;
}
