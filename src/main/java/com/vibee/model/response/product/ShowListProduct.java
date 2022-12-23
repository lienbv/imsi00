package com.vibee.model.response.product;

import com.vibee.model.item.ShowProductItems;
import lombok.Data;

import java.util.List;
@Data
public class ShowListProduct {
    List<ShowProductItems> items;

}
