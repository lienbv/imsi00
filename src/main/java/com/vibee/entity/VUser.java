package com.vibee.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "user")
@Table(name = "v_user")
public class VUser implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Basic
	@Column(name = "USERNAME")
	private String username;

	@Basic
	@Column(name = "FULLNAME")
	private String fullname;

	@Basic
	@Column(name = "PASSWORD")
	private String password;

	@Basic
	@Column(name = "CCCD")
	private String cccd;

	@Basic
	@Column(name = "ADDRESS")
	private String address;

	@Basic
	@Column(name = "NUMBER_PHONE")
	private String numberPhone;

	@Basic
	@Column(name = "EMAIL")
	private String email;

	@Basic
	@Column(name = "CREATED_DATE")
	private Date created;

	@Basic
	@Column(name = "STATUS")
	private int status;
}
