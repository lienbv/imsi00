package com.vibee.model.result;

import com.vibee.model.item.SelectExportItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionBillResult {
    private int productId;
    private int importId;
    private int amount;
    private SelectExportItem item;
}
