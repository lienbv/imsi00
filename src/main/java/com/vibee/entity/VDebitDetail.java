package com.vibee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Entity
@Table(name = "v_debit_detail")
public class VDebitDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "PRODUCT_NAME")
    private String productName;
    @Column(name = "AMOUNT")
    private int amount;
    @Column(name = "UNIT_ID")
    private int unitId;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "DEBIT_DATE")
    private Date debitDate;
    @Column(name = "DEBIT_ID")
    private int debitId;
    @Column(name = "STATUS")
    private int status;
}
