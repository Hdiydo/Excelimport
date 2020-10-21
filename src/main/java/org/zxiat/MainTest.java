package org.zxiat;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zxiat.entity.target.ControlPointTree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainTest {
    public static void main(String[] args) throws FileNotFoundException {

        MainTest controlPointTest = new MainTest();
        String path ="C:/Users/fanbao/Desktop/文件/网络安全等级保护三级通用技术+管理要求控制点描述.xlsx";
        File file = new File(path);
        InputStream inputStream  = new FileInputStream(file);
        boolean isE2007 = false;    //判断是否是excel2007格式
        if(path.endsWith("xlsx")){
            isE2007 = true;
        }
        try {
            InputStream input = inputStream;  //建立输入流
            Workbook wb  = null;
            //根据文件格式(2003或者2007)来初始化
            if(isE2007){
                wb = new XSSFWorkbook(input);
            }else{
                wb = new HSSFWorkbook(input);
            }
            int sheetsCounts = wb.getNumberOfSheets();
            for (int i=2; i < sheetsCounts; i++){                                      //从第三个sheet开始
                Sheet sheet = wb.getSheetAt(i);

             getFirstC(sheet);
              System.out.println();

 }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static ControlPointTree  getFirstC(Sheet sheet) {
        Row row1 = sheet.getRow(0);
        String title = getMergedRegionValue(sheet, 0, row1.getCell(0).getColumnIndex());
        ControlPointTree tree1 = new ControlPointTree();
        tree1.setName(title);
        List<ControlPointTree> list1 = new ArrayList<>();
        Row row = null;
        for(int j=2; j<sheet.getLastRowNum()+1; j++) {
            row = sheet.getRow(j);
            isMergedRegion(sheet, j, 0);

        }
        tree1.setChildren(list1);

        return tree1;

    }


//
//    public static ControlPointTree  getFirstC(Sheet sheet) {
//        Row row1 = sheet.getRow(0);
//        String title = getMergedRegionValue(sheet, 0, row1.getCell(0).getColumnIndex());
//        ControlPointTree tree1 = new ControlPointTree();
//        tree1.setName(title);
//        List<ControlPointTree> list1 = new ArrayList<>();
//        Row row = null;
//        for(int j=2; j<sheet.getLastRowNum()+1; j++) {
//            row = sheet.getRow(j);
////                rsclumn1 = getMergedRegionValue(sheet, row.getRowNum(), 0);
////                tree2.setName(rsclumn1);
////                list1.add(tree2);
//            Stack stack1=new Stack();
//            List<ControlPointTree> list2 = new ArrayList<>();
//
//            String rsclumn1 = null;
//            String rsclumn2 = null;
//            String rsclumn3 = null;
//            String rsclumn4 = null;
//            String rsclumn5 = null;
//                    if (isMergedRegion(sheet, j, 0)){   //判断是合并单元格
//                        if (isMergedRegionFirstRow(sheet, j, 0)){   //判断是否是一级合并单元格的第一行   是第一行新建一个树   放入栈中
//                            ControlPointTree tree2 = new ControlPointTree();
//                            rsclumn1 = getMergedRegionValue(sheet, row.getRowNum(), 0);
//                            tree2.setName(rsclumn1);
//                            List<ControlPointTree> list22 = new ArrayList<>();
//
//                            if (isMergedRegionFirstRow(sheet, j, 1)) {
//
//                                List<ControlPointTree> list3= new ArrayList<>();
//                                List<ControlPointTree> list33= new ArrayList<>();
//                                    ControlPointTree tree3 = new ControlPointTree();
//                                    rsclumn2 = getMergedRegionValue(sheet, row.getRowNum(), 1);
//                                    rsclumn3 = getMergedRegionValue(sheet, row.getRowNum(), 2);
//                                    List<ControlPointTree> list4 = new ArrayList<>();
//                                    ControlPointTree tree4 = new ControlPointTree();
//                                    rsclumn4 =getCellValue(row.getCell(3));
//                                    rsclumn5 =getCellValue(row.getCell(4));
//                                    tree4.setName(rsclumn4);
//                                    tree4.setRemarks(rsclumn5);
//                                list3.add(tree4);
//                                    tree3.setName(rsclumn2);
//                                    tree3.setRemarks(rsclumn3);
//                                tree3.setChildren(list3);
//                                list33.add(tree3);
//                                list22.addAll(list33);
//
//                            }else {
//
//                                List<ControlPointTree> list3= new ArrayList<>();
//                                ControlPointTree tree3 = new ControlPointTree();
//                                rsclumn2 = getCellValue(row.getCell(1));
//                                rsclumn3 = getCellValue(row.getCell(2));
//                                List<ControlPointTree> list4 = new ArrayList<>();
//                                ControlPointTree tree4 = new ControlPointTree();
//                                rsclumn4 =getCellValue(row.getCell(3));
//                                rsclumn5 =getCellValue(row.getCell(4));
//                                tree4.setName(rsclumn4);
//                                tree4.setRemarks(rsclumn5);
//                                list3.add(tree4);
//                                tree3.setName(rsclumn2);
//                                tree3.setRemarks(rsclumn3);
//                                tree3.setChildren(list3);
//                                list4.add(tree3);
//                                list22.addAll(list4);
//                            }
//                            tree2.setChildren(list22);
//                            stack1.push(tree2);
//
//                          }
//
//
//
//                    }else{
//                        ControlPointTree tree2 = new ControlPointTree();
//                         rsclumn1 =getCellValue( row.getCell(0));
//
//
//                        ControlPointTree tree3 = new ControlPointTree();
//                        rsclumn2 =getCellValue(row.getCell(1));
//                        rsclumn3 =getCellValue(row.getCell(2));
//                        rsclumn4 =getCellValue(row.getCell(3));
//                        rsclumn5 =getCellValue(row.getCell(4));
//                        tree3.setName(rsclumn2);
//                        tree3.setRemarks(rsclumn3);
//
//                        ControlPointTree tree4 = new ControlPointTree();
//                        List<ControlPointTree> list4 = new ArrayList<>();
//                        tree4.setName(rsclumn4);
//                        tree4.setRemarks(rsclumn5);
//                        list4.add(tree4);
//                        tree3.setChildren(list4);
//                        List<ControlPointTree> list3 = new ArrayList<>();
//                        list3.add(tree3);
//                        tree2.setChildren(list3);
//                        tree2.setName(rsclumn1);
//                        stack1.push(tree2);
//                    }    //完全不是合并单元格
//            list1.addAll(stack1);
//        }
//        tree1.setChildren(list1);
//
//        return tree1;
//
//    }
//    public static List<ControlPointTree> getSecondC(Sheet sheet) {
//        List<ControlPointTree> list1 = new ArrayList<>();
//        Row row = null;
//        for(int j=2; j<sheet.getLastRowNum()+1; j++) {
//            row = sheet.getRow(j);
//            ControlPointTree tree2 = new ControlPointTree();
//            List<ControlPointTree> list2 = new ArrayList<>();
//
//                    String rsclumn1;
//                    if (isMergedRegion(sheet, j, 0)){
//                        if ( !isFirstRowOf(sheet, row.getRowNum(), 0)){
//                            continue;
//                        }
//                         rsclumn1 = getMergedRegionValue(sheet, row.getRowNum(), 0);
//                    }else{
//                         rsclumn1 = row.getCell(0).getRichStringCellValue().getString();
//
//                    }
//            list1.add(tree2);
//        }
//        return list1;
//
//    }
    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column){
        int sheetMergeCount = sheet.getNumMergedRegions();
        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell) ;
                }
            }
        }
        return null ;
    }


    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell){

        if(cell == null) return "";

        if(cell.getCellType() == Cell.CELL_TYPE_STRING){

            return cell.getStringCellValue();

        }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){

            return String.valueOf(cell.getBooleanCellValue());

        }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){

            return cell.getCellFormula() ;

        }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){

            return String.valueOf(cell.getNumericCellValue());

        }
        return "";
    }
    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 判断该行是不是指定的单元格的第一行
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private static Boolean isMergedRegionFirstRow(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstRow = range.getFirstRow();
            int lastColumn = range.getLastColumn();
            if (row==firstRow){
                if(lastColumn==column){
                    return true;
                }
            }
        }
        return false;
    }




}
