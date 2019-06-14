package io;

import model.warehouse.Item;
import model.warehouse.OrderBins;
import model.warehouse.Warehouse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WarehouseWriter {

    public static void run(String outFile, HashMap<String, Warehouse> warehouses, OrderBins.TIME_INTERVAL timeInterval) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        for (String id : warehouses.keySet()) {
            Warehouse warehouse = warehouses.get(id);
            XSSFSheet sheet = workbook.createSheet("Lager " + warehouse.getIdentifier());

            ArrayList<OrderBins.TimeBin> bins = OrderBins.binItemStocks(warehouse, timeInterval);

            Row firstRow = sheet.createRow(0);
            firstRow.createCell(0).setCellValue("BG-Nummer");
            firstRow.createCell(1).setCellValue("");
            int columnIndex = 2;
            for (OrderBins.TimeBin bin : bins)
                firstRow.createCell(columnIndex++).setCellValue(bin.getTimeStamp());

            int rowIndex = 1;
            for (Item item : warehouse.getItem2order().keySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(item.getIdentifier());
                row.createCell(1);
                columnIndex = 2;
                for (OrderBins.TimeBin bin : bins)
                    row.createCell(columnIndex++).setCellValue(bin.getStock(item.getIdentifier()));
            }

        }

        try {
            FileOutputStream outputStream = new FileOutputStream(outFile);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
