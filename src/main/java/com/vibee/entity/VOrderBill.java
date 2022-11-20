package com.vibee.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Getter
@Setter
@Entity
@Table(name = "v_order_bill")
public class VOrderBill {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "ID_BILL")
    private int billId;
    @Column(name ="ID_ORDER")
    private int orderId;
}
