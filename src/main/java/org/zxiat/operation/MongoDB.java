package org.zxiat.operation;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.zxiat.entity.Resource;
import org.zxiat.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MongoDB {

    private static Logger logger = LoggerFactory.getLogger(MongoDB.class);

    private ResourceService resourceService;

    private Resource resource;

    /**
     * MongoDB连接实例
     */
    public MongoClient mongoClient = null;

    /**
     * MongoDB数据库实例
     */
    public MongoDatabase mongoDatabase = null;

    //查询到数据库中所有的集合
    public static List<String> getCollection(Resource resource) {
        List<String> collections = new ArrayList<>();
        try {
            //获取数据库连接对象
            MongoDatabase mongoDatabase = MongoDBUtil.getConnect(resource);

            // 获取该db下所有集合名称:mongoDatabase.listCollectionNames()
            logger.info("数据库的名称为：" + mongoDatabase.getName());
            for (String collectionName : mongoDatabase.listCollectionNames()) {
                collections.add(collectionName);
            }
        } catch (Exception e) {
            logger.info("连接MongoDB数据库失败!");
        }
        return collections;
    }





    //查询到数据库中所有的集合


    //查询出数据库中集合的文档数和大小
}
