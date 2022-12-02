package com.vibee.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountItem {
    private int idCustomer;
    private String fullname;
    private String numberphone;
    private String cccd;
    private String email;
    private String address;
    private long totalPrice;
    private int status;
    private String statusName;
}
