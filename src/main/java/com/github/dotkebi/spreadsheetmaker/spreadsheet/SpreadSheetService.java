package com.github.dotkebi.spreadsheetmaker.spreadsheet;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface SpreadSheetService {

    String UTF8_BOM = "\uFEFF";

    <T> void convert(HttpServletResponse response, Class<T> klass, List<T> datas, String fileName);

    String extension();

    default ReflectionUtils.FieldFilter filter() {
        return field -> field.getAnnotation(SpreadSheetColumnField.class) != null;
    }

    default <T> List<ValueWidth> getHeader(Class<T> klass) {
        final List<ValueWidth> headers = new ArrayList<>();
        ReflectionUtils.doWithFields(klass
                , field -> {
                    SpreadSheetColumnField mark = field.getAnnotation(SpreadSheetColumnField.class);

                    String fieldName = mark.fieldName();
                    if (StringUtils.isEmpty(fieldName)) {
                        headers.add(new ValueWidth().value(field.getName()).width(mark.width()));
                    } else {
                        headers.add(new ValueWidth().value(fieldName).width(mark.width()));
                    }
                }
                , filter());
        return headers;
    }

    default List<ValueWidth> getColumns(Object dto, int i, Collection<Field> fields) {
        List<ValueWidth> columns = new ArrayList<>();
        for (Field field : fields) {
            SpreadSheetColumnField mark = field.getAnnotation(SpreadSheetColumnField.class);

            ReflectionUtils.makeAccessible(field);
            Object source = ReflectionUtils.getField(field, dto);
            String value = "";
            if (source != null) {
                value = String.valueOf(source);
            }
            columns.add(new ValueWidth().value(value).width(mark.width()));
            ++i;
        }
        return columns;
    }

    default <T> List<String[]> transform(List<T> objects) {
        List<String[]> dataToWrite = new ArrayList<>();

        int i;
        for (Object dto : objects) {
            i = 0;
            final List<Field> fields = new ArrayList<>();
            ReflectionUtils.doWithFields(dto.getClass()
                    , fields::add
                    , filter());

            List<ValueWidth> values = getColumns(dto, i, fields);
            dataToWrite.add(values.stream().map(ValueWidth::getValue).toArray(String[]::new));
        }
        return dataToWrite;
    }

    default String getAttachmentFileName(String header, String fileName) {
        try {
            if (header.contains("MSIE")) {
                String docName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                return "attachment;filename=" + docName + ";";
            } else if (header.contains("Firefox")) {
                String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                return "attachment; filename=\"" + docName + "\"";
            } else if (header.contains("Opera")) {
                String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                return "attachment; filename=\"" + docName + "\"";
            } else if (header.contains("Chrome")) {
                String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                return "attachment; filename=\"" + docName + "\"";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "attachment;filename=" + fileName + ";";
    }


}
