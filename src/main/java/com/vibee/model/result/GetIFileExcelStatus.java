package com.vibee.model.result;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetIFileExcelStatus {
    List<GetImportFileExcel> list = new ArrayList<>();
    String message = "";
}
