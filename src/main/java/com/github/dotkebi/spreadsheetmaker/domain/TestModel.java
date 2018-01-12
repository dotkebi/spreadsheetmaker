package com.github.dotkebi.spreadsheetmaker.domain;

import com.github.dotkebi.spreadsheetmaker.spreadsheet.SpreadSheetColumnField;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TestModel implements Serializable {

    @SpreadSheetColumnField(fieldName = "No", width = 6)
    private int no;

    @SpreadSheetColumnField(fieldName = "Name", width = 30)
    private String name;

    @SpreadSheetColumnField(fieldName = "Mobile", width = 15)
    private String mobile;

    @SpreadSheetColumnField(fieldName = "Email", width = 23)
    private String email;


}
