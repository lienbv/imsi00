package com.vibee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Entity(name = "Product")
@Table(name = "v_product")
public class VProduct implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "NAME_PRODUCT")
	private String productName;
	@Column(name = "TYPE_PRODUCT")
	private int productType;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "STATUS")
	private int status;
	@Column(name = "BAR_CODE")
	private String barCode;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "FILE_ID")
	private int fileId;
	@Column(name ="CREATOR")
	private String creator;
	@Column(name="NAME_SUPPLIER")
	private String supplierName;
}
