package com.vibee.model.request.debit;

import lombok.Data;

import java.util.List;
@Data
public class ListPayRequest {
   private List<PayRequest> data;
}
