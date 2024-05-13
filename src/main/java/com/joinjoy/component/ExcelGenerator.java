package com.joinjoy.component;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.joinjoy.model.bean.AcSignForm;

@Component
public class ExcelGenerator {
	

	
    public static void createExcelFile(List<AcSignForm> acSignForms, String filePath) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	Workbook workbook = new XSSFWorkbook();  
        
    	CellStyle headerStyle = workbook.createCellStyle();
    	headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
    	headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 創建粗體字體
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

    	
    	Sheet sheet = workbook.createSheet("AcSignForms"); // 創建工作表

        Row headerRow = sheet.createRow(0);
        // 創建表頭
        String[] headers = new String[] {
        	    "姓名", "Email", "電話", "性別", "生日", 
        	    "地區", "報名日期", "付費狀態", "付費日期", "報名狀態"
        	};

        	for (int i = 0; i < headers.length; i++) {
        	    Cell headerCell = headerRow.createCell(i);
        	    headerCell.setCellValue(headers[i]);
        	    headerCell.setCellStyle(headerStyle);
        	}


        int rowNum = 1;
        for (AcSignForm form : acSignForms) {
            Row row = sheet.createRow(rowNum++);
            String gender = form.getAsfGender();
            String genderDisplay = "未知"; // 如果性別不是F或M，顯示為"未知"
            if ("F".equals(gender)) {
                genderDisplay = "女";
            } else if ("M".equals(gender)) {
                genderDisplay = "男";
            }
         
            String formattedDate = form.getAsfBirthday() != null ? dateFormat.format(form.getAsfBirthday()) : "";
            String asdDate = form.getAsfDate() != null ? dateFormat.format(form.getAsfDate()) : "";
            String paidDate = form.getAsfPaidDate() != null ? dateFormat.format(form.getAsfPaidDate()) : "";
            
            String paidStatus ;
            if (form.getAsfPaidStatus() == 0) {
                paidStatus = "未付款";
            } else if (form.getAsfPaidStatus() == 1){
                paidStatus = "已付款";
            }   else {
            	paidStatus = "";
            }
            
            String signStatus ;
			if (form.getAsfSignStatus() == 0) {
				signStatus="取消報名";
			}else if (form.getAsfSignStatus() == 1){
				signStatus="已報名";
			}else {
				signStatus="完成報名";
			}
            row.createCell(0).setCellValue(form.getAsfName());
            row.createCell(1).setCellValue(form.getAsfEmail());
            row.createCell(2).setCellValue(form.getAsfTel());
            row.createCell(3).setCellValue(genderDisplay);
            row.createCell(4).setCellValue(formattedDate);
            row.createCell(5).setCellValue(form.getAsfArea());
            row.createCell(6).setCellValue(asdDate);
            row.createCell(7).setCellValue(paidStatus);
            row.createCell(8).setCellValue(paidDate);
            row.createCell(9).setCellValue(signStatus);
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
