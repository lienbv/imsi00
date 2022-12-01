package com.vibee.service.vstaffstatistic;

import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.staffstatistic.StaffStatisticResponse;

public interface StaffStatisticService {
    public StaffStatisticResponse displaỵ(String language, int numberPage, int sizePage,
                                           String idBill, String status, String createdDate,
                                           String price, String creator, String date);
    public BaseResponse export(String language, int numberPage, int sizePage,
                               String idBill, String status, String createdDate,
                               String price, String creator, String date);
}
