package com.github.dotkebi.spreadsheetmaker.spreadsheet;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Qualifier("excelService")
public class ExcelService implements SpreadSheetService {

    protected final Logger log = getLogger(this.getClass());

    @Override
    public <T> void convert(HttpServletResponse response, Class<T> klass, List<T> datas, String fileName) {
        response.addHeader("Content-disposition", fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");

        try (
                OutputStream outputStream = response.getOutputStream()
        ) {
            XSSFWorkbook workbook = makeWorkBook("testSheet", klass, datas);
            workbook.write(outputStream);
            workbook.close();

            response.flushBuffer();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String extension() {
        return ".xlsx";
    }

    public <T> XSSFWorkbook makeWorkBook(String sheetName, Class<T> klass, List<T> lists) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        int rowIndex = 0;

        int cellIndex = 0;
        XSSFRow headerRow = sheet.createRow(rowIndex);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerStyle = getStyle(workbook);
        headerStyle.setFont(headerFont);

        for (ValueWidth header : getHeader(klass)) {
            XSSFCell cell = headerRow.createCell(cellIndex);
            sheet.setColumnWidth(cellIndex, header.getWidth() * 255);

            cell.setCellValue(header.getValue());
            cell.setCellStyle(headerStyle);
            ++cellIndex;
        }

        List<String[]> datas = transform(lists);

        CellStyle cellStyle = getStyle(workbook);

        for (String[] line : datas) {
            ++rowIndex;
            XSSFRow row = sheet.createRow(rowIndex);
            cellIndex = 0;
            for (String column : line) {
                XSSFCell cell = row.createCell(cellIndex);
                cell.setCellValue(column);
                cell.setCellStyle(cellStyle);
                ++cellIndex;
            }
        }
        return workbook;
    }

    private CellStyle getStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        return headerStyle;
    }


}
