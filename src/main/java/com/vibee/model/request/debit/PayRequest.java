package com.vibee.model.request.debit;

import com.vibee.model.request.BaseRequest;
import com.vibee.model.response.debit.GetDetailBill;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PayRequest extends BaseRequest {
    private BigDecimal inPrice;

}
