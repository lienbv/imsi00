package com.vibee.model.response.export;

import com.vibee.model.response.BaseResponse;
import com.vibee.model.result.ExportResult;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetExportsByUnitSelectResponse extends BaseResponse {
    private List<ExportResult> results;
}
