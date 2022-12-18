package com.vibee.model.response.debit;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class DebitDetailResponse extends BaseResponse {
    private String fullName;
    private String phoneNumber;
    private BigDecimal totalAmountOwed;
    private String creatorPayer;
    private int billId;
    private Date createDate;
    private BigDecimal total;
    private String address;
    private int typeOfDebtor;
    private int idDebit;
    private String description;


}
