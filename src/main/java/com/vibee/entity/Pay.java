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
public class Pay implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "DEBIT_ID")
    private int debitId;
    @Column(name = "ACTUAL_DATE_OF_PAYMENT_OF_DEBT")
    private Date actualDateOfPaymentOfDebt;
    @Column(name = "amount_pay")
    private BigDecimal amountPay;
    @Column(name = "STATUS")
    private int status;

}
