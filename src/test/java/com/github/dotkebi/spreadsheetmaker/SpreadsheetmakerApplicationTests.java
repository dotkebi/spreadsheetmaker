package com.github.dotkebi.spreadsheetmaker;

import com.github.dotkebi.spreadsheetmaker.domain.TestModel;
import com.github.dotkebi.spreadsheetmaker.spreadsheet.ExcelService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpreadsheetmakerApplicationTests {

    @Autowired
    private ExcelService excelService;

    @Test
    public void createExcelTest() {
        List<TestModel> testDatas = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            testDatas.add(
                    TestModel
                            .builder()
                            .no(i)
                            .email("test@test.com")
                            .mobile("010-xxxx-xxxx")
                            .name("test user").build()
            );
        }

        XSSFWorkbook workbook = excelService.makeWorkBook("testSheet", TestModel.class, testDatas);

        File file = new File("test.xlsx");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
