package com.vibee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Entity
@Table(name = "v_timeline")
public class VTimeline {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "ID_ORDER")
    private int orderId;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "CREATOR")
    private int creator;
    @Column(name = "DESCRIPTION")
    private int description;
}
