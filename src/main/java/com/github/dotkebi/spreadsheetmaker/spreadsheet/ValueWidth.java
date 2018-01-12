package com.github.dotkebi.spreadsheetmaker.spreadsheet;

public class ValueWidth {

    private int width;
    private String value;

    public ValueWidth() {
    }

    public ValueWidth width(int width) {
        this.width = width;
        return this;
    }

    public ValueWidth value(String value) {
        this.value = value;
        return this;
    }

    public ValueWidth(int width, String value) {
        this.width = width;
        this.value = value;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
