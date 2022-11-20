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
@Table(name = "v_detail_cart")
public class VDetailCart implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "ID_WAREHOUSE")
	private int warehouseId;
	@Column(name = "ID_CART")
	private int cartId;
	@Column(name = "PRICE") //in table unit
	private BigDecimal price;
	@Column(name = "AMOUNT") // by user
	private int amount;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "STATUS")
	private int status;
	@Column(name = "UNIT")
	private String unit;
}
