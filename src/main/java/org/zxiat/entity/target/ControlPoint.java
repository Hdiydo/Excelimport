package org.zxiat.entity.target;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * @description: 控制点
 * @author: ZhouWei
 * @create: 2020-08
 **/
@Data
@ToString
@Document(collection = "5f470f2fb5f3795247b6be37-c")
public class ControlPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id    //一个树 装一个sheet  放在一个文档里面
    private String id;   //uuid
    private String type;  //控制点
    private List<String> tags;  //sheet名字
    private HashMap<String,String> labels;  //sheet名字
    private HashMap<String,String> descs;   //
    private HashMap<String,List<String>> aliases;
    private ControlPointTree[] tree;  // 树
    private Date mod;   //生成日期
    private Integer version;  //1
    private List<Gene> genes;
    private boolean pushed;  //true
}
