package com.vibee.model.info;

import com.vibee.model.item.UnitItem;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ImportWarehouseInfor {

    //id!: number;
    //productId!: string;
    //  productName!: string;
    //  inAmount!: double;
    //  type!: string;
    //  image!: string;
    //  description!: string;
    //  status!: int;
    //  supplierId!: int;
    //  img!: int;
    //  inPrice!: bigDecimal;
    //  typeProduct!: GetTypeProductResult;
    //  barcode!: string;
    //  unitId!: int;
    //  exports!:[
    //		{
    //			"unitId":int
    //			"inPrice":bigDecimal
    //			"outPrice":bigDecimal
    //}
    //]
    //  rangeDates!: string;
    private int id;
    private int productId;
    private String image;
    private String productName;
    private int status;
    private String barcode;
    private int img;
    private BigDecimal inPrice;
    private int typeProductId;
    private int inAmount;
    private int unitId;
    private int supplierId;
    private Date rangeDate;
    private String description;
    private List<UnitItem> exportsItems;
}
