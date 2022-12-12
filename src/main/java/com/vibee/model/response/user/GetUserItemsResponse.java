package com.vibee.model.response.user;

import com.vibee.model.item.UserItems;
import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class GetUserItemsResponse extends BaseResponse {
    private List<UserItems> items ;
    private int countStatus;
    private int totalItems;
    private int totalPages;
    private int page;
    private int pageSize;
}
