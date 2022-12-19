package com.vibee.service.closetoexpired;

import com.vibee.model.response.expired.CloseToExpiresResponse;

public interface CloseToExpiredService {
    public CloseToExpiresResponse getAll(String nameSearch,int page, int record);
}
