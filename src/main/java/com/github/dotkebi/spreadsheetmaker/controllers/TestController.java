package com.github.dotkebi.spreadsheetmaker.controllers;

import com.github.dotkebi.spreadsheetmaker.domain.TestModel;
import com.github.dotkebi.spreadsheetmaker.spreadsheet.SpreadSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    @Qualifier("excelService")
    private SpreadSheetService excelService;

    @Autowired
    @Qualifier("csvService")
    private SpreadSheetService csvService;

    private List<TestModel> testDatas;

    @PostConstruct
    public void testData() {
        testDatas = new ArrayList<>();

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
    }

    @GetMapping("/csv")
    public void csv(
            HttpServletResponse response
            , HttpServletRequest request
    ) {
        String name = "csv_download" + csvService.extension();

        String userAgent = request.getHeader("User-Agent");
        csvService.convert(response, TestModel.class, testDatas, csvService.getAttachmentFileName(userAgent, name));
    }

    @GetMapping("/excel")
    public void excel(
            HttpServletResponse response
            , HttpServletRequest request
    ) {
        String name = "excel_download" + excelService.extension();

        String userAgent = request.getHeader("User-Agent");
        excelService.convert(response, TestModel.class, testDatas, excelService.getAttachmentFileName(userAgent, name));
    }

}
