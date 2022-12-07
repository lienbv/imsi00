package com.vibee.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String convertStatusSupplier(int status) {
        switch (status) {
            case 1 :
                return "Hợp tác";
            case 2 :
                return "Hủy";
            default:
                return "không biết";
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return sdf.format(date);
    }

    public static boolean uploadFile(MultipartFile file){
        try {
            FileCopyUtils.copy(file.getBytes(), new File( url+ file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
