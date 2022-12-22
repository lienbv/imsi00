package com.vibee.service.expired;

import com.vibee.model.response.ExpirationResponse;
import com.vibee.model.response.expired.CloseToExpiresResponse;

public interface ExpiredServer {
    public ExpirationResponse getAll(String nameSearch, int page, int record);
}
