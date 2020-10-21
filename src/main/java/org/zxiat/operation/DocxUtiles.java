package org.zxiat.operation;

import java.util.*;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 读取word文档中表格数据，支持doc、docx
 */
@Component
public class DocxUtiles {

    private static Logger logger = LoggerFactory.getLogger(DocxUtiles.class);

    //获取Keys
    public Map<String, String[]> getKeys(MultipartFile file) throws Exception {
        logger.info("把文件的第一行作为Key值");
        //返回的文件数据
        Map<String, String[]> map = new LinkedHashMap<>();
        String[] cellNames;
        // 处理docx格式 即office2007以后版本
        if (file.getOriginalFilename().endsWith("docx")) {
            XWPFDocument xwpf = new XWPFDocument(file.getInputStream());//得到word文档的信息
            Iterator<XWPFTable> it = xwpf.getTablesIterator();//得到word中的表格
            int NUM = 0;
            while (it.hasNext()) {
                XWPFTable table = it.next();
                List<XWPFTableRow> rows = table.getRows();
                //取第一行作为列名
                XWPFTableRow row = rows.get(0);
                List<XWPFTableCell> cells = row.getTableCells();

                cellNames = new String[cells.size()];

                for (int i = 0; i < cells.size(); i++) {
                    cellNames[i] = cells.get(i).getText();
                }
                map.put("Table" + NUM, cellNames);//Key命名为Table+数字
                NUM++;
            }
        }
        return map;
    }

    //获取表中数据
    public Map<String, List> docx2json(MultipartFile file, String[] colName, int TableNum) throws Exception {
        // 返回的map
        Map<String, List> Table = new LinkedHashMap<>();
        if (file.getOriginalFilename().endsWith("docx")) {
            XWPFDocument xwpf = new XWPFDocument(file.getInputStream());//得到word文档的信息
            List<XWPFTable> it = xwpf.getTables();
            XWPFTable table = it.get(TableNum);
            List<XWPFTableRow> rows = table.getRows();
            List<Map> RowDatas = new ArrayList<>();
            for (int i = 1; i < rows.size(); i++) {
                XWPFTableRow row = rows.get(i);
                List<XWPFTableCell> cells = row.getTableCells();
                Map<String, String> RowData = new LinkedHashMap<>();
                for (int j = 0; j < cells.size(); j++) {
                    XWPFTableCell cell = cells.get(j);//i行j列单元格
                    RowData.put(colName[j], cell.getText());
                }
                RowDatas.add(RowData);
            }
            Table.put("Table" + TableNum, RowDatas);
        }
        return Table;
    }
}
