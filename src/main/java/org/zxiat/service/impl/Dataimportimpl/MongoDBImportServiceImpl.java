package org.zxiat.service.impl.Dataimportimpl;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.zxiat.entity.Resource;
import org.zxiat.operation.MongoDB;
import org.zxiat.service.dataimport.MongoDBImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MongoDBImportServiceImpl implements MongoDBImportService {

  private static Logger logger = LoggerFactory.getLogger(MongoDBImportServiceImpl.class);

  @Override
  public Map<String,String> testConnection(Resource resource) throws SQLException {
    Map<String,String> map=new HashMap<>();
    String info=getConnection(resource);
    map.put("message",info);
    return map;
  }

  @Override
  public Map<String, List<String>> getCollections(Resource resource) throws SQLException {
    String str=getConnection(resource);
    Map<String,List<String>> map=new HashMap<>();
    if("success".equals(str)){
      map.put("tables", MongoDB.getCollection(resource));
      return map;
    }else{
      List<String> list=new ArrayList<>();
      list.add(str);
      map.put("tables",list);
      logger.info("查询失败!");
      return map;
    }
  }

  //不通过认证获取连接数据库对象
  private static String getConnection(Resource resource) throws SQLException {
    String host = resource.getHost();
    String port = resource.getPort();
    String dbName = resource.getDbName();
    try {
      //连接到 mongodb 服务
      MongoClient mongoClient = new MongoClient(host, Integer.parseInt(port));
      //连接到数据库
      MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
      logger.info("Connect to database successfully(连接MongoDB数据库)!");
      return "success";
    }catch (Exception e){
      e.printStackTrace();
      return e.getMessage();
    }
  }

  //需要密码认证方式连接
  private static String getConnection2(Resource resource) throws SQLException {
    String host = resource.getHost();
    String port = resource.getPort();
    String dbName = resource.getDbName();
    String username = resource.getUsername();
    String password = resource.getPassword();
    try {
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
      return "success!";
    }catch (Exception e){
      e.printStackTrace();
      return e.getMessage();
    }
  }





}
