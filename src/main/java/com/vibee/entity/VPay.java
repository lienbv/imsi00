package com.vibee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Entity
@Table(name = "V_PAY")
public class VPay implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "BILL_ID")
    private int billId;
    @Column(name = "DATE_PAYMENT")
    private Date actualDateOfPaymentOfDebt;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "IN_PRICE")
    private BigDecimal in_Price;
    @Column(name = "NUMBER_OF_PAYOUTS")
    private int numberOfPayOuts;
    private String creator;
}
