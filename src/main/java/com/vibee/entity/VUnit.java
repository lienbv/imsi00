package com.vibee.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="v_unit")
public class VUnit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "UNIT_NAME")
    private String unitName;
    @Column(name = "CREATE_DATE")
    private Date createdDate;
    @Column(name = "CREATOR")
    private String creator;
    @Column(name = "PARENT_ID")
    private int parentId;
    @Column(name = "Description")
    private String description;
    @Column(name = "AMOUNT")
    private int amount;
    @Column(name = "STATUS")
    private int status;
}
