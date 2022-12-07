package com.vibee.model.response.category;

import com.vibee.model.response.BaseResponse;
import com.vibee.model.result.GetTypeProductResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetTypeProductResponse extends BaseResponse {
    private List<GetTypeProductResult> results;
}
