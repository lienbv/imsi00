package com.vibee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Entity
@Table(name = "v_supplier")
public class VSupplier implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "NAME_SUPPLIER")
	private String nameSup;
	@Column(name = "CREATOR")
	private String creator;
	@JsonFormat(pattern ="yyyy-MM-dd")
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "NUMBER_PHONE")
	private String numberPhone;
	@Column(name = "STATUS")
	private int status;
}
