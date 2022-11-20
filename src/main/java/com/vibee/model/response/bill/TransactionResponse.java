package com.vibee.model.response.bill;

import com.vibee.model.response.BaseResponse;
import com.vibee.model.result.TransactionResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse extends BaseResponse {
    private List<TransactionResult> results;
}
