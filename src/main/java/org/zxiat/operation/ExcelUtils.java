package org.zxiat.operation;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Created by fu on 2019/4/10.
 */

@Component
public class ExcelUtils {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public Map<String, List> excel2json(MultipartFile file, String[] cellNames, int sheetnum) throws Exception {
        // 返回的map
        Map<String, List> excelMap = new LinkedHashMap<>();

        // Excel列的样式，主要是为了解决Excel数字科学计数的问题
        CellStyle cellStyle;
        // 根据Excel构成的对象
        Workbook wb;
        // 如果是2007及以上版本，则使用想要的Workbook以及CellStyle
        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith("xlsx")) {
            logger.info("是2007及以上版本  xlsx");
            wb = new XSSFWorkbook(file.getInputStream());
            XSSFDataFormat dataFormat = (XSSFDataFormat) wb.createDataFormat();
            cellStyle = wb.createCellStyle();
            // 设置Excel列的样式为文本
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        } else {
            logger.info("是2007以下版本  xls");
            POIFSFileSystem fs = new POIFSFileSystem(file.getInputStream());
            wb = new HSSFWorkbook(fs);
            HSSFDataFormat dataFormat = (HSSFDataFormat) wb.createDataFormat();
            cellStyle = wb.createCellStyle();
            // 设置Excel列的样式为文本
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        }
        Sheet sheet = wb.getSheetAt(sheetnum);

        List list = new LinkedList();

        // 从第二行起遍历每一行
        int rowNum = sheet.getLastRowNum();
        logger.info("总共有 " + rowNum + " 行");
        if (rowNum == 0) {
            list.add("no data of the sheet");
            excelMap.put("message", list);
            return excelMap;
        }
        // 一个sheet表对于一个List
        for (int j = 1; j <= rowNum; j++) {
            // 一行数据对于一个Map
            Map rowMap = new LinkedHashMap();
            // 取得某一行
            Row row = sheet.getRow(j);
            // 遍历每一列
            boolean isNotEmpty = false;

            for (int k = 0; k < cellNames.length; k++) {
                Cell cell = row.getCell(k);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                if (cell!=null && !"".equals(cell.getStringCellValue())) {
                    isNotEmpty = true;
                    break;
                }
            }
            if (isNotEmpty) {
                for (int k = 0; k < cellNames.length; k++) {
                    Cell cell = row.getCell(k);
                    if (cell != null) {
                        cell.setCellStyle(cellStyle);
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        // 保存该单元格的数据到该行中
                        if (!"".equals(cellNames[k])) {
                            rowMap.put(cellNames[k], cell.getStringCellValue());
                        }
                    }
                }
            }
            // 保存该行的数据到该表的List中
            if (rowMap.size() > 0) {
                list.add(rowMap);
            }
        }
        // 将该sheet表的表名为key，List转为json后的字符串为Value进行存储
        excelMap.put(sheet.getSheetName(), list);
        return excelMap;
    }
        //获取文件中每个表，与表中的列 的map
    public Map<String, String[]> getKeys(MultipartFile file) throws Exception {
        logger.info("把文件的第一行作为Key值");
        Map<String, String[]> map = new LinkedHashMap<>();
        // Excel列的样式，主要是为了解决Excel数字科学计数的问题
        CellStyle cellStyle;
        // 根据Excel构成的对象
        Workbook wb;
        if (file.getOriginalFilename().endsWith("xlsx")) {
            logger.info("是2007及以上版本  xlsx");
            wb = new XSSFWorkbook(file.getInputStream());
            XSSFDataFormat dataFormat = (XSSFDataFormat) wb.createDataFormat();
            cellStyle = wb.createCellStyle();
            // 设置Excel列的样式为文本
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        } else {
            logger.info("是2007以下版本  xls");
            POIFSFileSystem fs = null;
            fs = new POIFSFileSystem(file.getInputStream());
            wb = new HSSFWorkbook(fs);
            HSSFDataFormat dataFormat = (HSSFDataFormat) wb.createDataFormat();
            cellStyle = wb.createCellStyle();
            // 设置Excel列的样式为文本
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        }
        // sheet表个数
        int sheetsCounts = wb.getNumberOfSheets();
        for (int i = 0; i < sheetsCounts; i++) {
            Sheet sheet = wb.getSheetAt(i);
            logger.info("第" + i + "个sheet:" + sheet.toString());
            // 将第一行的列值作为正个json的key
            String[] cellNames;
            // 取第一行列的值作为key
            Row fisrtRow = sheet.getRow(0);
            // 如果第一行就为空，则是空sheet表，该表跳过
            if (null == fisrtRow) {
                continue;
            }
            // 得到第一行有多少列
            int curCellNum = 0;
            for (int k = 0; k < fisrtRow.getLastCellNum(); k++) {
                Cell cell = fisrtRow.getCell(k);
                if (cell!=null && !"".equals(cell.getStringCellValue())) {
                    curCellNum = k+1;
                }
            }
            logger.info("第一行的列数：" + curCellNum);
            // 根据第一行的列数来生成列头数组
            cellNames = new String[curCellNum];
            // 单独处理第一行，取出第一行的每个列值放在数组中，就得到了整张表的JSON的key
            for (int m = 0; m < curCellNum; m++) {
                Cell cell = fisrtRow.getCell(m);
                // 设置该列的样式是字符串
                cell.setCellStyle(cellStyle);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                // 取得该列的字符串值
                cellNames[m] = cell.getStringCellValue();
            }
            map.put(sheet.getSheetName(), cellNames);
        }
        return map;
    }
}
