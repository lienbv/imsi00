package com.vibee.utils;

import com.vibee.model.item.ProductStatusItem;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ProductUtils {
    public static String getstatusname(int status,String lang) {
        switch (status) {
            case 1:
                return MessageUtils.get(lang, "msg.active");
            case 2:
                return MessageUtils.get(lang, "msg.inactive");
            case 3:
                return MessageUtils.get(lang, "msg.lock");
            case 4:
                return MessageUtils.get(lang, "msg.deleted");
            default:
                return MessageUtils.get(lang, "msg.active");
        }
    }
    public static String getOrderStatusName(int status,String lang) {
        switch (status) {
            case 1:
                return MessageUtils.get(lang, "Packing");
            case 2:
                return MessageUtils.get(lang, "Pay");
            case 3:
                return MessageUtils.get(lang, "Completion");
            case 4:
                return MessageUtils.get(lang, "Completion");
            case 5:
                return MessageUtils.get(lang, "Completion");
            case 6:
                return MessageUtils.get(lang, "Fail");
            default:
                return MessageUtils.get(lang, "Unconfimred");

        }
    }
    public static String statusname(int status){
        if(status==1){
            return "hoạt động";
        }else if( status == 2 ){
            return "đã đóng";
        }
        return null;
    }

    public static String convertWereHouse(int status,String lang) {
        switch (status) {
            case 1:
                return MessageUtils.get(lang, "msg.active");
            case 2:
                return MessageUtils.get(lang, "msg.inactive");
            case 3:
                return MessageUtils.get(lang, "msg.deleted");
            default:
                return MessageUtils.get(lang, "msg.active");
        }
    }
    public static String convertStatus(int status, String language) {

        switch (status) {
            case 1:
                return MessageUtils.get(language, "msg.paid");
            case 2:
                return MessageUtils.get(language, "msg.unpaid");
            case 3:
                return MessageUtils.get(language, "msg.out-of-date");
            default:
                return "";
        }
    }
    public static String convertTypeOfDebtor(int status, String language) {
        switch (status) {
            case 1:
                return MessageUtils.get(language, "msg.customer-buy-odd");
            case 2:
                return MessageUtils.get(language, "msg.customer-wholesale");
            default:
                return "0";
        }
    }

    public static List<ProductStatusItem> getStatuss(String languge){
        List<ProductStatusItem> productStatusItems=new ArrayList<>();
        productStatusItems.add(new ProductStatusItem(1,getstatusname(1,languge)));
        productStatusItems.add(new ProductStatusItem(2,getstatusname(2,languge)));
        productStatusItems.add(new ProductStatusItem(3,getstatusname(3,languge)));
        productStatusItems.add(new ProductStatusItem(4,getstatusname(4,languge)));
        return productStatusItems;
    }
}
