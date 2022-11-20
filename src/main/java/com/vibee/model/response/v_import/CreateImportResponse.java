package com.vibee.model.response.v_import;

import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateImportResponse extends BaseResponse {
    private int importId;
}
