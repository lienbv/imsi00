package com.vibee.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="warehouse")
@Table(name="v_warehouse")
public class VWarehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "PRODUCT_ID")
    private int productId;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "OUT_PRICE")
    private BigDecimal outPrice;
    @Column(name = "CREATOR")
    private String creator;
    @Column(name = "IN_AMOUNT")
    private Double inAmount;
    @Column(name = "OUT_AMOUNT")
    private Double outAmount;
    @Column(name = "IN_PRICE")
    private BigDecimal inPrice;
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;
    @Column(name = "MODIFIED_Date")
    private Date modifiedDate;
    @Column(name = "UNIT_ID")
    private int unitId;
    @Column(name = "NUMBER_OF_ENTRIES")
    private int numberOfEntries;
}
