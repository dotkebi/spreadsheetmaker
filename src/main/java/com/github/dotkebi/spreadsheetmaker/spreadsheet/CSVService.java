package com.github.dotkebi.spreadsheetmaker.spreadsheet;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Qualifier("csvService")
public class CSVService implements SpreadSheetService {

    protected final Logger log = getLogger(this.getClass());

    @Override
    public <T> void convert(HttpServletResponse response, Class<T> klass, List<T> datas, String fileName) {
        response.addHeader("Content-disposition", fileName);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try (
                OutputStream outputStream = response.getOutputStream();
                CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream, "UTF-8"))
        ) {
            //add BOM
            outputStream.write(UTF8_BOM.getBytes("UTF-8"));
            writer.writeNext(getHeader(klass).stream().map(ValueWidth::getValue).toArray(String[]::new));
            writer.writeAll(transform(datas));
            response.flushBuffer();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String extension() {
        return ".csv";
    }
}
