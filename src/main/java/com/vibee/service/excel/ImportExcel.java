package com.vibee.service.excel;

import com.vibee.model.result.GetImportFileExcel;
import com.vibee.utils.Utiliies;
import org.apache.poi.hpsf.Decimal;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

public class ImportExcel {
    private static final int COLUMN_INDEX_NAME_PRODUCT = 0;
    private static final int COLUMN_INDEX_BARCODE = 1;
    private static final int COLUMN_INDEX_SUPPLIER = 2;
    private static final int COLUMN_INDEX_EXPIRE_DATE = 3;
    private static final int COLUMN_INDEX_AMOUNT = 4;
//    private static final int COLUMN_INDEX_TYPE_PRODUCT = 5;
    private static final int COLUMN_INDEX_PRICE = 6;


    public static List<GetImportFileExcel> readExcel(String excelFilePath) throws IOException, ParseException {
        List<GetImportFileExcel> listImportFileExcelList = new ArrayList<>();

        // Get file
        InputStream inputStream = new FileInputStream(new File(excelFilePath));

        // Get workbook
        Workbook workbook = getWorkbook(inputStream, excelFilePath);

        // Get sheet
        Sheet sheet = workbook.getSheetAt(0);

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
                        product.setNameProduct((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_BARCODE:
                        product.setBarcode((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_SUPPLIER:
                        product.setSupplier((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_EXPIRE_DATE:
                        product.setExpireDate(new SimpleDateFormat("yyyy-MM-dd").parse(Utiliies.convertDoubleToLocalDate((double) getCellValue(cell)).toString()));
                        break;
                    case COLUMN_INDEX_AMOUNT:
                        product.setInAmount(new BigDecimal((double) cellValue).intValue());
                        break;
//                    case COLUMN_INDEX_TYPE_PRODUCT:
//                        product.setType((String) getCellValue(cell));
//                        break;
                    case COLUMN_INDEX_PRICE:
                        product.setPrice((double) getCellValue(cell));
                        break;
                    default:
                        break;
                }

            }
            listImportFileExcelList.add(product);
        }

        workbook.close();
        inputStream.close();

        return listImportFileExcelList;
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
