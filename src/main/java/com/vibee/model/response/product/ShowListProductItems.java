package com.vibee.model.response.product;

import com.vibee.model.item.ProductItems;
import lombok.Data;

import java.util.List;

@Data
public class ShowListProductItems {
    List<ProductItems> items;
}
