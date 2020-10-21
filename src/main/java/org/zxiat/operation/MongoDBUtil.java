package org.zxiat.operation;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.zxiat.entity.Resource;
import org.zxiat.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MongoDBUtil {

    private static Logger logger = LoggerFactory.getLogger(MongoDBUtil.class);

    private final ResourceService resourceService;


    public MongoDBUtil(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    //不通过认证获取连接数据库对象
    public static MongoDatabase getConnect(Resource resource) {
        String host = resource.getHost();
        String port = resource.getPort();
        String dbName = resource.getDbName();
        //连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient(host, Integer.parseInt(port));

        //连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        logger.info("Connect to database successfully!");
        //返回连接数据库对象
        return mongoDatabase;
    }

    //需要密码认证方式连接
    public static MongoDatabase getConnect2(Resource resource) {
        String host = resource.getHost();
        String port = resource.getPort();
        String dbName = resource.getDbName();
        String username = resource.getUsername();
        String password = resource.getPassword();

        List<ServerAddress> adds = new ArrayList<>();
        //ServerAddress()两个参数分别为 服务器地址 和 端口
        ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port));
        adds.add(serverAddress);

        List<MongoCredential> credentials = new ArrayList<>();
        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(username, dbName, password.toCharArray());
        credentials.add(mongoCredential);

        //通过连接认证获取MongoDB连接
        MongoClient mongoClient = new MongoClient(adds, credentials);

        //连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        logger.info("Connect to database successfully!");

        //返回连接数据库对象
        return mongoDatabase;
    }

}

