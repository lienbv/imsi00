package com.vibee.model.response.supplier;

import com.vibee.model.response.BaseResponse;
import com.vibee.model.result.GetSuppliersResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetSuppliersResponse extends BaseResponse {
   private List<GetSuppliersResult> suppliers;
}
