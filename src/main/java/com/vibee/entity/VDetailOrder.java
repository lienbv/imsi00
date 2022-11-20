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
@Table(name = "v_detail_order")
public class VDetailOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "ID_ORDER")
    private int orderId;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "QUATITY_OF_USER")
    private int quatityOfUser;
    @Column(name = "QUATITY_OUT")
    private int quatityOut;
    @Column(name = "ID_WAREHOUSE")
    private int warehouseId;
    @Column(name = "ID_DETAIL_CARD")
    private int detailCardId;
    @Column(name = "UNIT")
    private String unit;
}
