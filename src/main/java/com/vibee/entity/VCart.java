package com.vibee.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Component
@Getter
@Setter
@Entity
@Table(name = "v_cart")
public class VCart implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "CREATOR")
	private int creator;
	@Column(name = "PRICE")// total
	private BigDecimal price;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "STATUS")
	private int status;
	@Column(name="SHIPPING_FEE")
	private BigDecimal shippingFee;
	@Column(name="PROMOTION")
	private BigDecimal promotion;
}
