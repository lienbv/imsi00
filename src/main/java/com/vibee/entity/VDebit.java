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
@Table(name = "v_debit")
public class VDebit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "DEBIT_DATE")
    private Date debitDate;
    @Column(name = "CREATOR_DEBTOR")
    private String creatorDebtor;
    @Column(name = "USER_ID")
    private int userId;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "TOTAL_AMOUNT_OWED")
    private BigDecimal totalAmountOwed;
    @Column(name = "CREATOR_PAYER")
    private String creatorPayer;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "BILL_ID")
    private int billId;
    @Column(name = "TYPE_OF_DEBTOR")
    private int typeOfDebtor;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
}
