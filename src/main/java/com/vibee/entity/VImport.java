package com.vibee.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="import")
@Table(name="v_import")
public class VImport {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "WAREHOUSE_ID")
    private int warehouseId;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "CREATOR")
    private String creator;
    @Column(name = "IN_AMOUNT")
    private Double inAmount;
    @Column(name = "IN_MONEY")
    private BigDecimal inMoney;
    @Column(name = "UPDATER")
    private int updater;
    @Column(name = "UPDATE_DATE")
    private Date updatedDate;
    @Column(name = "ID_SUPPLIER")
    private int supplierId;
    @Column(name = "NAME_SUPPLIER")
    private String supplierName;
    @Column(name = "ID_UNIT")
    private int unitId;
    @Column(name = "FILE_ID")
    private int fileId;
    @Column(name="EXPIRED_DATE")
    private Date expiredDate;
    @Column(name = "PRODUCT_CODE")
    private String productCode;
    @Column(name = "URL_UPLOAd")
    private String urlUpload;
    @Column(name = "NUMBER_OF_ENTRIES")
    private int numberOfEntries;
//    @Column
//    private Date rangeDate;
}
