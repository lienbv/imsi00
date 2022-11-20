package com.vibee.service.vimport;

import com.vibee.model.request.v_import.CreateImportRequest;
import com.vibee.model.response.v_import.CreateImportResponse;

public interface CreateImportProductService {
    CreateImportResponse create(CreateImportRequest request);
}
