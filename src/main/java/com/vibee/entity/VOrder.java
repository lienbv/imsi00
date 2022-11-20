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
@Table(name = "v_order")
public class VOrder implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "MESSAGE")
	private String message;
	@Column(name = "CREATOR")
	private int creator;
	@Column(name = "VERIFIED_DATE")
	private Date verifiedDate;
	@Column(name = "ID_CART")
	private int cartId;
	@Column(name = "STATUS")
	private int status;
	@Column(name = "CREATED_DATE")
	private Date created;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "PRICE")
	private BigDecimal price;
	@Column(name = "AMOUNT")
	private int amount;
	@Column(name = "NUMBER_PHONE")
	private String numberPhone;


}
