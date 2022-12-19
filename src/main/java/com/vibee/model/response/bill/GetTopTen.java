package com.vibee.model.response.bill;

import com.vibee.model.item.BillItems;
import lombok.Data;

import java.util.List;

@Data
public class GetTopTen {
    List<BillItems> items;
}
