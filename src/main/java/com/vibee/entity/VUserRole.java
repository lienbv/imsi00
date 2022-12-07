package com.vibee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Entity(name = "userRole")
@Table(name = "v_user_role")
public class VUserRole implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "ID_USER")
	private int userId;
	@Column(name = "ID_ROLE")
	private int roleId;

}
