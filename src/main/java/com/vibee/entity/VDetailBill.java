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
@Table(name = "v_detail_bill")
public class VDetailBill implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "ID_BILL")
	private int billId;
	@Column(name = "PRICE")
	private BigDecimal price;
	@Column(name = "AMOUNT")
	private int amount;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "STATUS")
	private int status;
	@Column(name = "IMPORT_ID")
	private int importId;
	@Column(name = "CREATOR")
	private String creator;
	@Column(name = "UNIT_ID")
	private int unitId;
}
