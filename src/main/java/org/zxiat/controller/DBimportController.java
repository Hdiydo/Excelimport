package org.zxiat.controller;

import com.mongodb.MongoClient;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.zxiat.entity.Resource;
import org.zxiat.operation.DocxUtiles;
import org.zxiat.operation.ExcelUtils;
import org.zxiat.service.ResourceService;
import org.zxiat.service.dataimport.MongoDBImportService;
import org.zxiat.transformer.Transformer;
//import org.zxiat.transformer.Transformer;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Future;
/**
 * @author Emiya
 */
@FXMLController
public class DBimportController implements Initializable {
    /**
     * @Description: 属性对应表单数据
     */
    public static List<LinkedHashMap> temporaryDeleteProp = new ArrayList<>();
    public static List<LinkedHashMap> remanentProp = new ArrayList<>();
    public static ObservableList<LinkedHashMap> propTableData = FXCollections.observableArrayList();
    public static List<String> valuesStore = new ArrayList<>();
    public static Object OldValue;
    private static Logger logger = LoggerFactory.getLogger(DBimportController.class);
    /**
     * @Description: 需要使用的服务以及处理方法
     */
    @Autowired
    private MongoDBImportService mongoDBImportService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ExcelUtils excelUtilsutils;
    @Autowired
    private DocxUtiles docxUtiles;
    @Autowired
    private Transformer transformer;

    /**
     * @Description: 页面控件
     */
    @FXML
    private TextField ChooseFileText, mLabelText, typeText, aliasText, desText;
    @FXML
    private TabPane myTabPane;
    @FXML
    private ComboBox<String>  SheetComboBox, inputTypeComboBox;
    @FXML
    private TableView RowtoClaimTable;
    @FXML
    private TableColumn OriginCol, PropCol, QlfCol;
    /**
     * @Description: //获取文件对象
     */
    private MultipartFile multipartFile = null;
    /**
     * @Description: 所选择用于保存的数组(根据表名作key ）
     */
    private Map<String, List> select = new HashMap<>();
    /**
     * @Description: 储存表和库的对应关系
     */
    private Map<String, String> resourcestoTable = new HashMap<>();
    /**
     * @Description: 储存表和库的对应关系
     */
    private Map<String, Resource> tabletoResources = new HashMap<>();
    /**
     * @Description: Excel列名数据
     */
    private Map<String, String[]> firstRowInDifferentSheet = new LinkedHashMap<>();
    /**
     * @Description: 选择文件类型
     */
    private String fileType = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //页面控件初始化
        inputTypeComboBox.setValue("输入方式");
        inputTypeComboBox.getItems().add("手动输入");
        inputTypeComboBox.getItems().add("原始列名");
       // TargetComboBox.setValue("选择目标数据表");
        SheetComboBox.setValue("选择表格");
        //属性映射表初始化
        OriginCol.setCellValueFactory(new MapValueFactory<>("原始列名"));
        PropCol.setCellValueFactory(new MapValueFactory<>("属性名"));
        QlfCol.setCellValueFactory(new MapValueFactory<>("限定配置"));
        RowtoClaimTable.setEditable(true);
        PropCol.setEditable(true);
        PropCol.setCellFactory(MyEditTableCell.forTableColumn());
        QlfCol.setEditable(true);
        QlfCol.setCellFactory(MyEditTableCell.forTableColumn());
        RowtoClaimTable.setItems(propTableData);
        //添加TargerCombox内目标表
      //  TargetComboBox.getItems().add("本地");
//        for (Resource resource : resourceService.getResource()) {
//            try {
//                //目标数据库内的所有表
//                Map<String, List<String>> resourceTables = getTables(resource);
//
//                System.out.println();
//                for (String table : resourceTables.get("tables")) {
//                    TargetComboBox.getItems().add("数据库s1：" + resource.getName() + "  数据表：" + table); //显示在下拉选中
//                    System.out.println(table);
//                    resourcestoTable.put("数据库s2：" + resource.getName() + "  数据表：" + table, table);
//                    tabletoResources.put(table, resource);
//                }
//            } catch (Exception e) {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("提示");
//                alert.setHeaderText("");
//                alert.setContentText("数据库加载失败");
//                alert.showAndWait();
//            }
//        }
        //添加属性映射表响应事件
        PropCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {
                OldValue = event.getOldValue();
                for (LinkedHashMap row : remanentProp) {
                    valuesStore.add((String) row.get("属性名"));
                }
                if (!valuesStore.contains(event.getNewValue())) {
                    ((LinkedHashMap) remanentProp.get(event.getTablePosition().getRow())).put("属性名", event.getNewValue());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("提示");
                    alert.setHeaderText("");
                    alert.setContentText("已有该属性名");
                    alert.showAndWait();
                }
                uploadPropTableData();
            }
        });
        QlfCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {
                String qlfsStr = (String) event.getNewValue();
                Set<String> qlfStrs = new HashSet<>(Arrays.asList(qlfsStr.split("[；;]")));
                List<String> props = new ArrayList<>(0);

                for (LinkedHashMap row : remanentProp) {
                    props.add((String) row.get("属性名"));
                }
                //输入检验
                boolean isAllContent = true;
                for (String qlfStr : qlfStrs) {
                    if (qlfStr.split("@").length<=1 || !props.contains(qlfStr.split("@")[1])) {
                        isAllContent = false;
                    }
                }
                if (isAllContent || qlfsStr == null || qlfsStr.equals("")) {
                    ((LinkedHashMap) remanentProp.get(event.getTablePosition().getRow())).put("限定配置", event.getNewValue());
                    TableController();
                }
                uploadPropTableData();
            }
        });
//        //为TargerCombox添加响应事件,切换数据库
//        TargetComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                Resource resource = tabletoResources.get(resourcestoTable.get(newValue));
////                String table = resourcestoTable.get(newValue);
//                if (resource != null) {
//           transformer.getTemplate(new MongoTemplate(new MongoClient(resource.getHost(), Integer.parseInt(resource.getPort())), resource.getDbName()));
//                }
//            }
//        });
        //为SheetComboBox添加响应事件
        SheetComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    cleanColsMappingTable();
                    String[] firstRowKeys = firstRowInDifferentSheet.get(newValue);
                    for (String key : firstRowKeys) {
                        LinkedHashMap<String, String> propsData = new LinkedHashMap<>();
                        propsData.put("原始列名", key);
                        propsData.put("属性名", key);
                        propsData.put("限定配置", "");
                        remanentProp.add(propsData);
                    }
                    uploadPropTableData();
                }
            }
        });
        //为输入框添加响应事件
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TableController();
                uploadPropTableData();
            }
        };
        mLabelText.textProperty().addListener(changeListener);
        typeText.textProperty().addListener(changeListener);
        aliasText.textProperty().addListener(changeListener);
        desText.textProperty().addListener(changeListener);
    }

    /**
     * @Description: 右侧表单修改响应事件空值函数（输入框和表格）
     * @Param: []
     * @return: void
     * @Author: Emiya
     * @Date: 2020/3/10
     */
    private void TableController() {
        //所有行的内容
        Set<String> allLine = new HashSet<>();
        allLine.addAll(Arrays.asList(mLabelText.getText().split("[；;]")));
        if (inputTypeComboBox.getValue().equals("原始列名")) {
            allLine.add("type@" + typeText.getText());
        }
        allLine.addAll(Arrays.asList(aliasText.getText().split("[；;]")));
        allLine.addAll(Arrays.asList(desText.getText().split("[；;]")));
        for (LinkedHashMap linkedHashMap : remanentProp) {
            allLine.addAll(Arrays.asList(linkedHashMap.get("限定配置").toString().split("[；;]")));
        }
        List<String> inTextProp = new ArrayList<>();
        List<LinkedHashMap> restoreRows = new ArrayList<>();
        List<LinkedHashMap> deleteRows = new ArrayList<>();
        for (String str : allLine) {
            if (str.split("@").length > 1) {
                for (LinkedHashMap row : remanentProp) {
                    if (row.get("原始列名").equals(str.split("@")[1])) {
                        deleteRows.add(row);
                    }
                }
                inTextProp.add(str.split("@")[1]);
            }
        }
        for (LinkedHashMap row : temporaryDeleteProp) {
            if (!inTextProp.contains(row.get("原始列名"))) {
                restoreRows.add(row);
            }
        }
        for (LinkedHashMap row : deleteRows) {
            row.put("限定配置", "");
            temporaryDeleteProp.add(row);
            remanentProp.remove(row);
        }
        for (LinkedHashMap row : restoreRows) {
            temporaryDeleteProp.remove(row);
            remanentProp.add(row);
        }
    }

    /**
     * @Description: 清理页面
     * @Param: []
     * @return: void
     * @Author: Emiya
     * @Date: 2019/11/21
     */
    private void cleanColsMappingTable() {
        remanentProp.clear();
        temporaryDeleteProp.clear();
        uploadPropTableData();
        mLabelText.clear();
        typeText.clear();
        aliasText.clear();
        desText.clear();
        inputTypeComboBox.setValue("输入方式");
    }

    /**
     * @Description: 更新属性映射表数据
     * @Param: []
     * @return: void
     * @Author: Emiya
     * @Date: 2019/11/22
     */
    private void uploadPropTableData() {
        propTableData.clear();
        for (LinkedHashMap row : remanentProp) {
            propTableData.add(row);
        }
    }

    /**
     * @Description: 更新页面
     * @Param: []
     * @return: void
     * @Author: Emiya
     * @Date: 2019/11/21
     */
//    private void updateDBinfo() {
//        TargetComboBox.getItems().clear();
//        resourcestoTable.clear();
//        tabletoResources.clear();
//        TargetComboBox.setValue("选择目标数据库");
//        //添加TargerCombox内目标表
//        TargetComboBox.getItems().add("本地");
//        for (Resource resource : resourceService.getResource()) {
//            try {
//                //目标数据库内的所有表
//                Map<String, List<String>> resourceTables = getTables(resource);
//
//                for (String table : resourceTables.get("tables")) {
//                    TargetComboBox.getItems().add("数据库1：" + resource.getName() + "  表：" + table);
//                    resourcestoTable.put("数据库2：" + resource.getName() + "  表：" + table, table);
//                    tabletoResources.put(table, resource);
//                }
//            } catch (Exception e) {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("提示");
//                alert.setHeaderText("");
//                alert.setContentText("数据库加载失败");
//                alert.showAndWait();
//            }
//        }
//    }

    /**
     * @Description: 读取EXCEL文件
     * @Param: [event]
     * @return: void
     * @Author: Emiya
     * @Date: 2019/11/21
     */
    public void chooseFile(final Event event) throws Exception {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件");
        Stage selectFile = new Stage();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        //添加Excel文件选项
        List<String> ExcelsType = new ArrayList<>();
        ExcelsType.add("*.xls");
        ExcelsType.add("*.xlsx");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ALL Excel", ExcelsType));
        //添加Word文件选项
        List<String> DocxsType = new ArrayList<>();
        DocxsType.add("*.docx");
        DocxsType.add("*.doc");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ALL Docx", DocxsType));
        File file = fileChooser.showOpenDialog(selectFile);
        if (file != null) {
            ChooseFileText.setText(file.getAbsolutePath());
            /**
             * @Description: 获取文件对象
             */
            FileInputStream input = new FileInputStream(file);
            //Excel文件解析
            if (file.getAbsolutePath().toLowerCase().endsWith("xls") || file.getAbsolutePath().toLowerCase().endsWith("xlsx")) {
                fileType = "Excel";
                //文件类型转换
                multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));

            }
            //Docx文件解析
            if (file.getAbsolutePath().toLowerCase().endsWith("docx") || file.getAbsolutePath().toLowerCase().endsWith("doc")) {
                fileType = "Docx";
                //文件类型转换
                multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
            }
        }
    }

    /**
     * @Description: 解析Excel文件内容
     * @Param: [event]
     * @return: void
     * @Author: Emiya
     * @Date: 2019/11/21
     */
    public void upload(final Event event) throws Exception {
        SheetComboBox.getItems().clear();
        cleanColsMappingTable();
        try {
            if ("Excel".equals(fileType)) {
                firstRowInDifferentSheet = excelUtilsutils.getKeys(multipartFile);
            }
            if ("Docx".equals(fileType)) {
                firstRowInDifferentSheet = docxUtiles.getKeys(multipartFile);
            }
            for (String key : firstRowInDifferentSheet.keySet()) {
                select.put(key, new ArrayList());
                SheetComboBox.getItems().add(key);
            }
           excelTabPane(firstRowInDifferentSheet);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText("");
            alert.setContentText("文件解析失败");
            alert.showAndWait();
        }
    }

    /**
     * @Description: 画EXCEL表格
     * @Param: [map]
     * @return: void
     * @Author: Emiya
     * @Date: 2019/11/21
     */
    private void excelTabPane(Map<String, String[]> map) {
        myTabPane.getTabs().clear();
        Map<String, List> result = null;
        int flag = 0;
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            //每个entry都是一个sheet表格
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();  //获取表格的名字
            String[] value = (String[]) entry.getValue();  //获取表格的所有的列的名字
            if (fileType.equals("Excel")) {
                try {
                    //flag 表示sheet 的序号,就是第几个sheet     value获取表格的名字
                    //该方法返回的是一个sheet的所有数据
                    result = excelUtilsutils.excel2json(multipartFile, value, flag);
                } catch (Exception e) {
                    e.printStackTrace();
                    alert.setTitle("提示");
                    alert.setHeaderText("");
                    alert.setContentText("文件打开失败");
                    alert.showAndWait();
                }
            }
            if (fileType.equals("Docx")) {
                try {
                    result = docxUtiles.docx2json(multipartFile, value, flag);
                } catch (Exception e) {
                    e.printStackTrace();
                    alert.setTitle("提示");
                    alert.setHeaderText("");
                    alert.setContentText("文件打开失败");
                    alert.showAndWait();
                }
            }
            /*数据整理*/
            ObservableList<LinkedHashMap> allData = FXCollections.observableArrayList();
            if (result != null) {
                for (int k = 0; k < result.get(key).size(); k++) {
                    allData.add((LinkedHashMap) result.get(key).get(k));
                }
            }
            /*画表格*/
            Tab tab = new Tab();
            tab.setText(key);
            tab.closableProperty().set(false);
            TableView table = new TableView<>(allData);
            TableColumn isSelect = new TableColumn();
            isSelect.setCellFactory((col) -> {
                TableCell<Map, Boolean> cell = new TableCell<Map, Boolean>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            CheckBox checkBox = new CheckBox();
                            checkBox.setSelected(true);
                            this.setGraphic(checkBox);
                            checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
                                if (newVal) {
                                    select.get(key).add(this.getIndex());
                                } else {
                                    for (int j = 0; j < select.get(key).size(); j++) {
                                        if (Integer.parseInt(select.get(key).get(j).toString()) == this.getIndex()) {
                                            select.get(key).remove(j);
                                        }
                                    }
                                }
                            });
                        }
                    }
                };
                return cell;
            });
            for (int i = 0; i < allData.size(); i++) {
                select.get(key).add(i);
            }
            isSelect.setMinWidth(27);
            table.getColumns().add(isSelect);
            int MAXSIZE = (value.length < 50) ? value.length : 50;
            for (int k = 0; k < MAXSIZE; k++) {
                TableColumn<Map, String> Col = new TableColumn(value[k]);
                Col.setMinWidth(130);
                Col.setCellValueFactory(new MapValueFactory<>(value[k]));
                table.getColumns().add(Col);
            }
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tab.setContent(table);
            myTabPane.getTabs().addAll(tab);
            flag++;
        }
    }

    /**
     * @Description: 上传EXCEL数据库内数据
     * @Param: [event]
     * @return: void
     * @Author: Emiya
     * @Date: 2019/11/21
     */
    public void importdata(final Event event) {
        try {
            Map<String, String[]> map = null;
            Map<String, List> get = null;
            if (fileType.equals("Excel")) {
                //map  获取文件中每个表，与表中的列list 的map
                map = excelUtilsutils.getKeys(multipartFile);
            }
            if (fileType.equals("Docx")) {
                map = docxUtiles.getKeys(multipartFile);
            }
            int flag = 0;
            for (String key : map.keySet()) {
                //判断该表是不是选择的那个表sheet
                if (key.equals(SheetComboBox.getValue())) {
                    break;
                } else {
                    flag++;
                }
            }
            try {
                if (fileType.equals("Excel")) {
                    //返回的是 文件的所有表的数据sheet 和每个表中所有的行的数据list  组成的map
                    get = excelUtilsutils.excel2json(multipartFile, map.get(SheetComboBox.getValue()), flag);
                }
                if (fileType.equals("Docx")) {
                    get = docxUtiles.docx2json(multipartFile, map.get(SheetComboBox.getValue()), flag);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText("");
                alert.setContentText("文件打开失败");
                alert.showAndWait();
            }
            //datas 为选中表的所有行的数据
            List<Map<String, String>> datas = get.get(SheetComboBox.getValue());
            List<Map<String, String>> data = new ArrayList<>();
            //select.get(SheetComboBox.getValue()).size()  选中表选中行的大小
            for (int j = 0; j < select.get(SheetComboBox.getValue()).size(); j++) {
                //select  所选择用于保存的数组(根据表名作key ）
                data.add(datas.get(Integer.parseInt(select.get(SheetComboBox.getValue()).get(j).toString())));
            }
            Future<Map<String, String>> message = null;
            // 名称
            HashMap<String, String> labelsMap = new HashMap<>();

            // 描述
            HashMap<String, String> descsMap = new HashMap<>();

            HashMap<String, List<String>> aliasesMap = new HashMap<>();
            String AliasesItems = aliasText.getText();
            if (!AliasesItems.equals("") && AliasesItems.split("[；;]").length > 0) {
                for (int i = 0; i < AliasesItems.split("[；;]").length; i++) {
                    if (!aliasesMap.keySet().contains(AliasesItems.split("[；;]")[i].split("@")[0])) {
                        List<String> aliase = new ArrayList<>();
                        aliase.add(AliasesItems.split("[；;]")[i].split("@")[1]);
                        aliasesMap.put(AliasesItems.split("[；;]")[i].split("@")[0], aliase);
                    } else {
                        aliasesMap.get(AliasesItems.split("[；;]")[i].split("@")[0])
                                .add(AliasesItems.split("[；;]")[i].split("@")[1]);
                    }
                }
            }



            HashMap<String, String> typeMap = new HashMap<>();
            typeMap.put(inputTypeComboBox.getValue(), typeText.getText());
            List<LinkedHashMap> ClaimsMap = remanentProp;


                                                                                                      //"tb_control_point"
                message = transformer.getFromObject(data, labelsMap, descsMap, aliasesMap, typeMap, ClaimsMap, "tb_control_point");
                while (true) {  ///这里使用了循环判断，等待获取结果信息
                    if (message.isDone()) {  //判断是否执行完毕
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("提示");
                        alert.setHeaderText("");
                        alert.setContentText(message.get().get("message"));
                        alert.showAndWait();
                        break;
                    }
                    System.out.println("Continue doing something else. ");
                    Thread.sleep(1000);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 添加新数据源
     * @Param: [event]
     * @return: void
     * @Author: Emiya
     * @Date: 2019/11/21
     */
    public void addDb(final Event event) throws Exception {
      //  Dialog<Map<String, String>> dialog = new Dialog<>();
        Resource resource = new Resource();

        resource.setName("kgms");
        resource.setDb("MongoDB");
        resource.setHost("172.16.19.61");
        resource.setPort("27017");
        resource.setDbName("kgms");
        if (connect(resource)) {
                resourceService.addResource(resource);
            }
        //更新页面
        cleanColsMappingTable();
     //   updateDBinfo();
    }


    /**
     * @Description: 测试数据源连接
     * @Param: [resource]
     * @return: java.lang.Boolean
     * @Author: Emiya
     * @Date: 2019/11/21
     */
    private Boolean connect(Resource resource) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Map<String, String> map = null;
        String dbType = resource.getDb();
        try {
            if ("MongoDB".equals(dbType)) {
                map = mongoDBImportService.testConnection(resource);
            }
            alert.setTitle("提示");
            alert.setHeaderText("");
            assert map != null;
            if (map.get("message").equals("success")) {
                alert.setContentText("成功");
                alert.showAndWait();
                return true;
            }
        } catch (Exception e) {
            alert.setTitle("提示");
            alert.setHeaderText("");
            alert.setContentText("链接数据库失败");
            alert.showAndWait();
            return false;
        }
        return false;
    }

    /**
     * @Description: 获取本地数据库中table、collection的信息
     * @Param: [resource]
     * @return: java.util.Map<java.lang.String, java.util.List < java.lang.String>>
     * @Author: Emiya
     * @Date: 2019/11/21
     */
    private Map<String, List<String>> getTables(Resource resource) throws Exception {
        Map<String, List<String>> map = new HashMap<>();//用于获取sever信息
        if ("MongoDB".equals(resource.getDb())) {
            map = mongoDBImportService.getCollections(resource); //获取所有 集合（表）
        }
        return map;
    }



    /**
     * @Description: 重写表内编辑框类
     * @Author: Emiya
     * @Date: 2019/11/22
     */
    private static class MyEditTableCell<S, T> extends TextFieldTableCell<S, T> {

        MyEditTableCell(StringConverter<T> converter) {
            this.getStyleClass().add("text-field-table-cell");
            setConverter(converter);
        }

        public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
            return forTableColumn(new DefaultStringConverter());
        }

        public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
                final StringConverter<T> converter) {
            return list -> new MyEditTableCell<S, T>(converter);
        }

        @Override
        public void updateItem(T item, boolean empty) {
            if (item == null) {
                super.updateItem(item, empty);
            } else {
                if (valuesStore.contains(item)) {
                    super.updateItem((T) OldValue, empty);
                } else {
                    super.updateItem(item, empty);
                }
                OldValue = null;
                valuesStore.clear();
            }
        }


    }


}
