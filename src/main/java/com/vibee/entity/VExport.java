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
@Entity(name = "export")
@Table(name = "v_export")
public class VExport {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "ID_UNIT")
    private int unitId;
    @Column(name = "OUT_PRICE")
    private BigDecimal outPrice;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "IN_PRICE")
    private BigDecimal inPrice;
    @Column(name = "CREATOR")
    private String creator;
    @Column(name = "IMPORT_ID")
    private int importId;
    @Column(name = "OUT_AMOUNT")
    private int outAmount;
    @Column(name = "STATUS")
    private int status;
}
