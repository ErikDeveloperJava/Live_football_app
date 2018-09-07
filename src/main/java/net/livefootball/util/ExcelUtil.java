package net.livefootball.util;

import net.livefootball.model.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelUtil {


    public void create(List<User> users, HttpServletResponse response) {
        int rowIndex = 0;
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("users");
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Id");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Username");
            row.createCell(3).setCellValue("password");
            for (User user : users) {
                row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getName());
                row.createCell(2).setCellValue(user.getUsername());
                row.createCell(3).setCellValue(user.getPassword());
            }
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if(workbook != null){
                try {
                    workbook.close();
                } catch (IOException e) {
                    //***
                }
            }
        }
    }
}