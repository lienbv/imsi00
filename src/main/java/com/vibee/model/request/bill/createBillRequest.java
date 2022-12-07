package com.vibee.model.request.bill;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

@Data
public class createBillRequest extends BaseRequest {
    private String name;
    private String phoneNumber;
    private String address;
    private int productId;
}
