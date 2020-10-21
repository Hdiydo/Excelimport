package org.zxiat;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zxiat.entity.target.ControlPoint;
import org.zxiat.entity.target.ControlPointTree;
import org.zxiat.entity.target.Gene;

import java.io.*;
import java.util.*;

public class MainTest2 {
    public static void main(String[] args) throws FileNotFoundException {
        String target = "5f470f2fb5f3795247b6be37-c";
        String url = "http://172.16.19.71:9999/api/common/entity/import?bucket=" + target + "&userId=admin";
        String path ="C:/Users/fanbao/Desktop/ss/新建 Microsoft Excel 工作表 (3).xlsx";
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
            for (int i=1; i < sheetsCounts; i++){

                List<String> tags = new ArrayList<>();

                //从第三个sheet开始
                Sheet sheet = wb.getSheetAt(i);

                tags.add(sheet.getSheetName());
                //把一个sheet的内容读取出来 封装成一个  ControlPointTree
                ControlPoint controlPoint = new ControlPoint();
                String uuid = getOrderIdByUUId();
               controlPoint.setId(uuid);
                controlPoint.setType("控制点");
                controlPoint.setTags(tags);
                ControlPointTree firstC = getFirstC(sheet);
                ControlPointTree  [] trees = new ControlPointTree [] {firstC};
                controlPoint.setTree(trees);
                controlPoint.setMod(new Date());
                List<Gene> genes = new ArrayList<>();
                Gene gg = new Gene(target,"控制点",new Date());
                genes.add(gg);
                controlPoint.setGenes(genes);
                controlPoint.setVersion(1);
             saveEntity(controlPoint, url);
              //  JSON.toJSONString(controlPoint)
            //    System.out.println(JSON.toJSONString(controlPoint));
             //  System.out.println(controlPoint);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getOrderIdByUUId() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0     
        // 4 代表长度为4     
        // d 代表参数为正数型
        return String.format("%015d", hashCodeV);
    }



    public static boolean saveEntity(ControlPoint controlPoint, String url) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(JSON.toJSONString(controlPoint), "UTF-8");
            params.setContentEncoding("UTF-8");
            request.addHeader("content-type", "application/json;charset=UTF-8");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println("Post 返回结果" + response);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static ControlPointTree  getFirstC(Sheet sheet) {
        Row row1 = sheet.getRow(0);
        String title = getMergedRegionValue(sheet, 0, row1.getCell(0).getColumnIndex());
        ControlPointTree tree1 = new ControlPointTree();
        tree1.setName(title);

        Row row = null;

        String s1 = null;
        String s2 = null;
        String s3 = null;
        String s4 = null;
        String s5 = null;

        List<ControlPointTree>  list =new LinkedList<>();
        List<ControlPointTree>  list1 =null;

        ControlPointTree tree2 = null;
        List<ControlPointTree>  list2 =null;

        ControlPointTree tree3 = null;

        for(int j=2; j<sheet.getLastRowNum()+1; j++) {  //便利sheet的每一行
            String rs1=null;  //第j行  第1列 的值
            String rs2=null;
            String rs3=null;
            String rs4=null;
            String rs5=null;

            row = sheet.getRow(j);   //获取第j行
            boolean isMerge1 = isMergedRegion(sheet, j, 0);  //判断第一列第j行是否具有合并单元格

            //获取第一列的单元格的值
            if(isMerge1) {
               rs1 = getMergedRegionValue(sheet, row.getRowNum(), 0);
              //  System.out.println(rs1);
            }else {
              rs1 = getCellValue( row.getCell(0));
               // System.out.println(rs1);
            }

            boolean isMerge2 = isMergedRegion(sheet, j, 1);   //判断第二列第j行是否具有合并单元格

            //获取第二列单元格的值
            if(isMerge2) {
                rs2 = getMergedRegionValue(sheet, row.getRowNum(), 1);
               // System.out.println(rs2);
            }else {
              rs2 = getCellValue( row.getCell(1));
              //  System.out.println(rs2);
            }

            boolean isMerge3 = isMergedRegion(sheet, j, 2); //判断第三列第j行是否具有合并单元格

            //获取第三列单元格的值
            if(isMerge3) {
                rs3 = getMergedRegionValue(sheet, row.getRowNum(), 2);
             //   System.out.println(rs3);
            }else {
                rs3 = getCellValue( row.getCell(2));
             //   System.out.println(rs3);
            }

            boolean isMerge4 = isMergedRegion(sheet, j, 3);    //判断第四列第j行是否具有合并单元格

            //获取第四列单元格的值
            if(isMerge4) {
               rs4 = getMergedRegionValue(sheet, row.getRowNum(), 3);
            //    System.out.println(rs4);
            }else {
                rs4 = getCellValue( row.getCell(3));
             //   System.out.println(rs4);
            }

            boolean isMerge5 = isMergedRegion(sheet, j, 4);  //判断第五列第j行是否具有合并单元格
            //获取第五列单元格的值
            if(isMerge5) {
              rs5 = getMergedRegionValue(sheet, row.getRowNum(), 4);
             //   System.out.println(rs5);
            }else {
              rs5 = getCellValue( row.getCell(4));
             //   System.out.println(rs5);
            }




            if (s1!=rs1){
                if (tree2!=null){
                    list.add(tree2);

                }
               // System.out.println(tree2);
                list1 = new ArrayList<>();
                s1= rs1;
              tree2 = new ControlPointTree();
                tree2.setName(s1);
           System.out.println();
            }


            if (s2!=rs2){
                list2 = new ArrayList<>();
                s2= rs2;
              tree3 = new ControlPointTree();
                tree3.setName(s2);
                tree3.setRemarks(rs3);
                list1.add(tree3);
                tree2.setChildren(list1);
              //  System.out.println(tree2);
            }

            if (s4!=rs4){
                s4= rs4;
                ControlPointTree tree4 = new ControlPointTree();
                tree4.setName(s4);
                tree4.setRemarks(rs5);
                list2.add(tree4);
                tree3.setChildren(list2);
              //  System.out.println(tree4);
            }



        }
        tree1.setChildren(list);
       // System.out.println(tree1);
        return tree1;
    }

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
