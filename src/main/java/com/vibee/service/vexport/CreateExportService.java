package com.vibee.service.vexport;

import com.vibee.model.request.v_export.CreateExportRequest;
import com.vibee.model.response.export.CreateExportResponse;

public interface CreateExportService {
    CreateExportResponse create(CreateExportRequest request);
}
