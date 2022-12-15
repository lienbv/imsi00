package com.vibee.service.excel;

import com.vibee.model.result.GetIFileExcelStatus;
import com.vibee.model.result.GetImportFileExcel;
import com.vibee.utils.Utiliies;
import org.apache.poi.hpsf.Decimal;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
@Service
public class ImportExcel {
    private static final int COLUMN_INDEX_NAME_PRODUCT = 0;
    private static final int COLUMN_INDEX_BARCODE = 1;
    private static final int COLUMN_INDEX_SUPPLIER = 2;
    private static final int COLUMN_INDEX_EXPIRE_DATE = 3;
    private static final int COLUMN_INDEX_AMOUNT = 4;
//    private static final int COLUMN_INDEX_TYPE_PRODUCT = 5;
    private static final int COLUMN_INDEX_PRICE = 5;


    public static GetIFileExcelStatus readExcel(String excelFilePath) throws IOException, ParseException {
        List<GetImportFileExcel> listImportFileExcelList = new ArrayList<>();

        // Get file
        InputStream inputStream = new FileInputStream(new File(excelFilePath));

        // Get workbook
        Workbook workbook = getWorkbook(inputStream, excelFilePath);

        // Get sheet
        Sheet sheet = workbook.getSheetAt(0);
        StringBuilder stringBuilder = new StringBuilder();
        // Get all rows
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            if (nextRow.getRowNum() == 0) {
                // Ignore header
                continue;
            }

            // Get all cells
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            // Read cells and set value for book object
            GetImportFileExcel product = new GetImportFileExcel();
            while (cellIterator.hasNext()) {
                //Read cell
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (cellValue == null || cellValue.toString().isEmpty()) {
                    continue;
                }
                // Set value for book object
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case COLUMN_INDEX_NAME_PRODUCT:
                        try {
                            product.setNameProduct((String) getCellValue(cell));
                        } catch (Exception e) {
                            if (getCellValue(cell) instanceof Boolean) {
                                boolean value = (Boolean) getCellValue(cell);
                                product.setNameProduct(value+"");
                            } else if (getCellValue(cell) instanceof Double) {
                                double value = (double) getCellValue(cell);
                                BigDecimal i = new BigDecimal(value);
                                product.setNameProduct(i+"");
                            }
                        }
                        break;
                    case COLUMN_INDEX_BARCODE:
                        try {
                            product.setBarcode((String) getCellValue(cell));
                        } catch (Exception e) {
                            if (getCellValue(cell) instanceof Boolean) {
                                boolean value = (Boolean) getCellValue(cell);
                                product.setBarcode(value+"");
                            } else if (getCellValue(cell) instanceof Double) {
                                double value = (double) getCellValue(cell);
                                BigDecimal i = new BigDecimal(value);
                                product.setBarcode(i+"");
                            }
                        }
                        break;
                    case COLUMN_INDEX_SUPPLIER:
                        try {
                            product.setSupplier((String) getCellValue(cell));
                        } catch (Exception e) {
                            if (getCellValue(cell) instanceof Boolean) {
                                boolean value = (Boolean) getCellValue(cell);
                                product.setSupplier(value+"");
                            } else if (getCellValue(cell) instanceof Double) {
                                double value = (Double) getCellValue(cell);
//                                int i = (int) value;
                                product.setSupplier(value+"");
                            }
                        }
                        break;
                    case COLUMN_INDEX_EXPIRE_DATE:
                        if (getCellValue(cell) instanceof String) {
                            stringBuilder.append("Kiểm tra ngày dòng: "+cell.getRowIndex()+"\n");
                        } else {
                            product.setExpireDate(new SimpleDateFormat("yyyy-MM-dd").parse(Utiliies.convertDoubleToLocalDate((double) getCellValue(cell)).toString()));
                        }
                        break;
                    case COLUMN_INDEX_AMOUNT:
                        try {
                            product.setInAmount(new BigDecimal((Double) cellValue).intValue());
                        } catch (Exception e) {
                            stringBuilder.append("Kiểm tra số lượng dòng: "+cell.getRowIndex()+"\n");
                        }
                        break;
//                    case COLUMN_INDEX_TYPE_PRODUCT:
//                        product.setType((String) getCellValue(cell));
//                        break;
                    case COLUMN_INDEX_PRICE:
                        try{
                            product.setPrice(new BigDecimal((Double) getCellValue(cell)));
                        } catch (Exception e) {
                            stringBuilder.append("Kiểm tra giá tiền dòng: "+cell.getRowIndex()+"\n");
                        }
                        break;
                    default:
                        break;
                }

            }
            if (product.getNameProduct() != null && product.getPrice() != null && product.getInAmount() != 0 && product.getSupplier() != null && product.getBarcode() != null && product.getExpireDate() != null) {
                listImportFileExcelList.add(product);
            }
        }

        workbook.close();
        inputStream.close();
        GetIFileExcelStatus getIFileExcelStatus = new GetIFileExcelStatus();
        if (stringBuilder.length() < 1) {
            getIFileExcelStatus.setList(listImportFileExcelList);
        } else {
            getIFileExcelStatus.setList(new ArrayList<>());
            getIFileExcelStatus.setMessage(stringBuilder.toString());
        }
        return getIFileExcelStatus;
    }

    // Get Workbook
    private static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    // Get cell value
    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            default:
                break;
        }

        return cellValue;
    }


}
