package com.vibee.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Getter
@Setter
@Entity
@Table(name = "v_type_product")
public class VTypeProduct {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "NAME")
	private String name;
	@Column(name ="CREATOR")
	private String creator;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "STATUS")
	private int status;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "PARENT_ID")
	private int parentId;
}
