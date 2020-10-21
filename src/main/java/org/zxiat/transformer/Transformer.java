//Transformer


package org.zxiat.transformer;

import com.alibaba.fastjson.JSON;
import com.mongodb.DBObject;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.zxiat.entity.LocalData;
import org.zxiat.entity.Resource;
import org.zxiat.entity.target.*;
import org.zxiat.service.LocalDataService;

import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by fu on 2019/3/6.
 * 数据融合使用
 */
@Component
public class Transformer {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommonTransformer.class);
    @Autowired
    private LocalDataService localDataService;

    private MongoTemplate template;

    public static String getKey(Map map, Object value) {
        List<Object> keyList = new ArrayList<>();
        for (Object key : map.keySet()) {
            if (map.get(key) instanceof String) {
                if (map.get(key).equals(value)) {
                    return (String) key;
                }
            }
            if (map.get(key) instanceof List) {
                if (((List) map.get(key)).contains(value)) {
                    return (String) key;
                }
            }
        }
        return null;
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

    @Async
    public Future<Map<String, String>> getFromObject(List<Map<String, String>> objects, HashMap<String, String> labelsMap, HashMap<String, String> descsMap,
                                                     HashMap<String, List<String>> aliasesMap, HashMap<String, String> typeMap, List<LinkedHashMap> ClaimsMap, String source) {
        LOGGER.info("开始转换，共计转换" + objects.size() + "条数据");

        Map<String, String> listMap = new HashMap<>();
        int success = 0;
        int error = 0;
        List<String> labelcols = new ArrayList<>();
        for (String str : labelsMap.values()) {
            labelcols.add(str);
        }
        List<String> desccols = new ArrayList<>();
        for (String str : descsMap.values()) {
            desccols.add(str);
        }
        List<String> aliascols = new ArrayList<>();
        for (List list : aliasesMap.values()) {
            aliascols.addAll(list);
        }
        String typecol = null;
        if (typeMap.get("原始列名") != null) {
            typecol = typeMap.get("原始列名");
        }
        List<String> claimscols = new ArrayList<>();
        for (LinkedHashMap linkedHashMap : ClaimsMap) {
            claimscols.add((String) linkedHashMap.get("原始列名"));
        }
        for (Object object : objects) {
            DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(JSON.toJSONString(object));

            Entity entity = new Entity();
            ControlPoint controlPoint = new ControlPoint();


            String uuid = getOrderIdByUUId();
            controlPoint.setId(uuid);

            HashMap<String, String> labels = new HashMap<>();
            String type = null;
            HashMap<String, String> descs = new HashMap<>();
            HashMap<String, List<String>> aliases = new HashMap<>();
            HashMap<String, List<Claim>> clas = new HashMap<>();
            if (typecol == null) {
                type = typeMap.get("手动输入");
            }
            for (String colName : dbObject.keySet()) {
                if (dbObject.get(colName) != null && !"".equals(dbObject.get(colName))) {
                    if (labelcols.contains(colName)) {
                        labels.put(getKey(labelsMap, colName), (String) dbObject.get(colName));
                    } else if (desccols.contains(colName)) {
                        descs.put(getKey(descsMap, colName), (String) dbObject.get(colName));
                    } else if (colName.equals(typecol)) {
                        type = (String) dbObject.get(colName);
                    } else if (aliascols.contains(colName)) {
                        String key = getKey(aliasesMap, colName);
                        if (aliases.containsKey(key)) {
                            aliases.get(key).add((String) dbObject.get(colName));
                        } else {
                            List<String> aliase = new ArrayList<>();
                            aliase.add((String) dbObject.get(colName));
                            aliases.put(key, aliase);
                        }
                    } else if (claimscols.contains(colName)) {
                        Claim c = new Claim();
                        String propName = null;
                        String qlfsStr = null;
                        for (LinkedHashMap linkedHashMap : ClaimsMap) {
                            if (linkedHashMap.get("原始列名").equals(colName)) {
                                propName = (String) linkedHashMap.get("属性名");
                                qlfsStr = (String) linkedHashMap.get("限定配置");
                            }
                        }
                        c.setProp(propName);
                        c.setTp(Snak.Type.STR);
                        c.setVal(dbObject.get(colName));
                        if ((qlfsStr != null) && !"".equals(qlfsStr)) {
                            HashMap<String, List<Snak>> qlfs = new HashMap<>(0);
                            for (String qlfStr : qlfsStr.split("[;；]")) {
                                String qlfName = qlfStr.split("@")[0];
                                String qlfColName = qlfStr.split("@")[1];
                                List<Snak> snaks = new ArrayList<>(0);
                                Snak qlf = new Snak();
                                qlf.setVal(dbObject.get(qlfColName));
                                qlf.setProp(qlfName);
                                qlf.setTp(Snak.Type.STR);
                                snaks.add(qlf);
                                qlfs.put(qlfName, snaks);
                            }
                            c.setQlfs(qlfs);
                        }
                        //source   所选的表的表名   这里是 Gene的src属性
                        Gene gene = new Gene(source, "控制点",  new Date());
                        c.setSrc(gene);
                        List<Claim> list = new ArrayList<>();
                        list.add(c);
                        clas.put(propName, list);
                    }
                }
            }

            controlPoint.setType("控制点");

            entity.setClaims(clas);
            entity.setLabels(labels);
            entity.setDescs(descs);
            controlPoint.setAliases(aliases); //setTags   //

            controlPoint.setMod(new Date());
            controlPoint.setGenes(Arrays.asList(new Gene(source, type, new Date())));

            entity.setVersion(1);
            try {
                if ("本地".equals(source)) {
                    saveLocalEntity(entity);
                } else {
                    saveEntity(controlPoint, source);
                }
                success++;
            } catch (Exception e) {
                e.printStackTrace();
                error++;
            }
        }
        listMap.put("message", "成功导入" + success + "条，" + "导入错误" + error + "条");
        return new AsyncResult<Map<String, String>>(listMap);
    }

    private void saveLocalEntity(Entity entity) {
        JSONObject json = JSONObject.fromObject(entity);
        String strJson = json.toString();
        LocalData data = new LocalData();
        data.setId(entity.getId());
        data.setData(strJson);
        data.setMlabel(entity.getLabels().get("zh"));
        localDataService.addLocalData(data);
    }


    private void saveEntity(ControlPoint controlPoint, String collectionName) {
        String uuid = getOrderIdByUUId();
        controlPoint.setId(uuid);

        template.save(controlPoint, collectionName);

    }

    public void getTemplate(MongoTemplate template) {
        this.template = template;
    }

}
