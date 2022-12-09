package com.vibee.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utiliies {
    @Value("${vibee.url}")
    private static String url;

    public static String convertStatusUser(int status) {
        switch (status) {
            case 1 :
                return "đang hoạt động";
            case 2 :
                return "không hoạt động";
            default:
                return "không biết";
        }
    }

    public static String convertStatusStatistic(int status) {
        switch (status) {
            case 1 :
                return "Unresolved";
            case 2 :
                return "Processing";
            case 3 :
                return "Processed";
            case 4 :
                return "Shipping";
            case 5 :
                return "Completion";
            case 6 :
                return "Fail";
            default:
                return "không biết";
        }
    }

    public static String convertStatusSupplier(int status,String language) {
        if (language.equals("vi")) {
            switch (status) {
                case 1:
                    return "đang hoạt động";
                case 2:
                    return "không hoạt động";
                default:
                    return "không biết";
            }
        }else {
            switch (status) {
                case 1:
                    return "Active";
                case 2:
                    return "Inactive";
                default:
                    return "no information";
            }
        }
    }

    // 1 - Unresolved - đang đợi xử lý
    // 2 - Processing - đang xử lý
    // 3 - Processed - đã xử lý xong
    // 4 - Shipping - đang vận chuyển
    // 5 - Completion - Giao thành công
    // 6 - Fail - Giao thất bại

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static String formatDateTime(Date date){
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return sdf.format(date);
    }

    public static Date formatStringDate(String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            return sdf.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDate convertDoubleToLocalDate(double number) {
        double raw = number;
        long days = (long) raw;
        double fraction = raw - days;

        LocalDate epoch = LocalDate.of(1899, 12, 30);
        LocalDate date = epoch.plusDays(days);
        return date;
    }

    public static boolean uploadFile(MultipartFile file){
        try {
            FileCopyUtils.copy(file.getBytes(), new File(url + file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getFilePath(String fileName) {
        return url + fileName;
    }

    static String generateCode(int idProduct, int idWareHouse, int idImport) {
        String idProductSTR = idProduct+"";
        String idWareHouseSTR = idWareHouse+"";
        String idImportSTR = idImport+"";
        int idProductSize = 4;
        int idWareHouseSize = 4;
        int idImportSize = 6;
        char idProductChar[] = new char[idProductSize];
        char idWareHouseChar[] = new char[idWareHouseSize];
        char idImportChar[] = new char[idImportSize];

        Random getRDProduct =  new Random();
        for (int i = 0; i < idProductSize; i++) {
            idProductChar[i] = idProductSTR.charAt(getRDProduct.nextInt(idProductSTR.length()));
        }
        String codeProduct = "";
        for (int i = 0; i < idProductChar.length; i++) {
            codeProduct += idProductChar[i];
        }

        Random getRDWareHouse =  new Random();
        for (int i = 0; i < idWareHouseSize; i++) {
            idWareHouseChar[i] = idWareHouseSTR.charAt(getRDWareHouse.nextInt(idWareHouseSTR.length()));
        }
        String codeWareHouse = "";
        for (int i = 0; i < idWareHouseChar.length; i++) {
            codeWareHouse += idWareHouseChar[i];
        }

        Random getRDImport =  new Random();
        for (int i = 0; i < idImportSize; i++) {
            idImportChar[i] = idImportSTR.charAt(getRDImport.nextInt(idImportSTR.length()));
        }
        String codeImport = "";
        for (int i = 0; i < idImportChar.length; i++) {
            codeImport += idImportChar[i];
        }

        return "VB-"+codeProduct+codeWareHouse+codeImport;
    }

    public static String formatDateReverse(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static NumberFormat convertMoney() {
        Locale locale = new Locale("vi", "VN");
        Currency currency = Currency.getInstance("VND");

        DecimalFormatSymbols df = DecimalFormatSymbols.getInstance(locale);
        df.setCurrency(currency);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setCurrency(currency);
        return numberFormat;
    }
}
