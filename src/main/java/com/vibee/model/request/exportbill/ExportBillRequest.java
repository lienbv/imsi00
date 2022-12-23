package com.vibee.model.request.exportbill;

import com.vibee.model.item.ExportBill;
import com.vibee.model.item.StatisticTotalPriceOfBill;
import com.vibee.model.request.BaseRequest;
import lombok.Data;

import java.util.List;

@Data
public class ExportBillRequest extends BaseRequest {
    private int idBill;
    private List<ExportBill> exportBills;
    private StatisticTotalPriceOfBill statisticTotalPriceOfBill;
    private int totalAmountProductOfBill;
}
