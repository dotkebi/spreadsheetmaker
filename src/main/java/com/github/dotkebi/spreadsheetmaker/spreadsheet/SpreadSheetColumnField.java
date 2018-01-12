package com.github.dotkebi.spreadsheetmaker.spreadsheet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SpreadSheetColumnField {
    String fieldName() default "";

    SpreadSheetColumnType fieldType() default SpreadSheetColumnType.String;

    int width() default 13;
}
