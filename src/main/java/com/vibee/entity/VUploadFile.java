package com.vibee.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "UploadFile")
@Table(name="v_upload_file")
public class VUploadFile {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "CREATOR")
    private String creator;
    @Column(name = "type")
    private String type;
    @Column(name = "SUB_TYPE")
    private String subType;
    @Column(name = "SIZE")
    private BigDecimal size;
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;
    @Column(name = "MODIFIED_Date")
    private Date modifiedDate;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "URL")
    private String url;
}
