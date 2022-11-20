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
@Entity
@Table(name = "v_feedback")
public class VFeedBack implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "CONTENT")
	private String content;
	@Column(name = "CREATOR")
	private int creator;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "RATE")
	private int rate;
	@Column(name = "TYPE_RATE")
	private int typerate;
	@Column(name = "STATUS")
	private int status;
}
