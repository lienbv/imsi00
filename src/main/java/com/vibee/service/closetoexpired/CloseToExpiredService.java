package com.vibee.service.closetoexpired;

import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.expired.CloseToExpiresResponse;
import com.vibee.model.request.expired.EditPriceExportRequest;

public interface CloseToExpiredService {
    public CloseToExpiresResponse getAll(String nameSearch,int page, int record);
    public BaseResponse editPriceExport(EditPriceExportRequest request);
}
