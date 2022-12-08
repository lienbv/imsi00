package com.vibee.model.request.debit;
import com.vibee.model.request.BaseRequest;
import com.vibee.model.response.debit.GetDetailBill;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DebitRequest extends BaseRequest {

    @NotNull
    private String fullName;
    @NotNull
    private String phoneNumber;

    private BigDecimal totalAmountOwed;
    @NotNull
    private String creatorPayer;
    @NotNull
    private int billId;
    @NotNull
    private String address;
    @NotNull
    private int typeOfDebtor;
    @NotNull
    private String expectedDateOfPaymentOfDebt;
    private List<GetDetailBill> debitItems;


}
