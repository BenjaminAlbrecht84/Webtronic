package io;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class OrderParser {

    public static ArrayList<Object[]> run(String FILE_NAME) {

        ArrayList<Object[]> result = new ArrayList<>();

        try {

            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = datatypeSheet.iterator();

            rowIterator.next();
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                int[] indices = {0, 1, 3, 5};

                boolean isIncomplete = false;
                for (int index : indices) {
                    Cell cell = row.getCell(index);
                    if (cell == null)
                        isIncomplete = true;
                    else if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().isEmpty())
                        isIncomplete = true;

                }
                if (isIncomplete)
                    continue;

                String itemId = row.getCell(0).getStringCellValue();
                String warehouseId = row.getCell(1).getStringCellValue();
                Date date = row.getCell(3).getDateCellValue();
                int itemNumber = Integer.parseInt(row.getCell(5).getStringCellValue());

                Object[] o = {itemId, warehouseId, date, itemNumber};
                result.add(o);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

}
