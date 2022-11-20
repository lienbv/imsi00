package com.vibee.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Entity
@Table(name = "v_bill")
public class VBill implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "PRICE")
	private BigDecimal price;
	@Column(name = "CREATOR")
	private String creator;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "STATUS")
	private int status;
	@Column(name = "MESSAGE")
	private String message;
	@Column(name = "transaction_type")
	private String transactionType;
	@Column(name = "payment_methods")
	private String paymentMethods;
	@Column(name="IN_PRICE")
	private BigDecimal inPrice;
}
