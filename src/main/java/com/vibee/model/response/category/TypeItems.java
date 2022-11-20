package com.vibee.model.response.category;

import lombok.Data;

import java.util.List;

@Data
public class TypeItems {
    private List<TypeItemsDto> data ;
    private int totalItems;
    private int totalPages;
    private int page;
    private int pageSize;
}
