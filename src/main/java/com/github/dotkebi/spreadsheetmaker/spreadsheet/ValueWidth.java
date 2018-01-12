package com.github.dotkebi.spreadsheetmaker.spreadsheet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValueWidth {

    private int width;
    private String value;

    public ValueWidth width(int width) {
        this.width = width;
        return this;
    }

    public ValueWidth value(String value) {
        this.value = value;
        return this;
    }
}
