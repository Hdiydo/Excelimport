package org.zxiat.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "TARGET",uniqueConstraints = {@UniqueConstraint(columnNames="NAME")})
public class Resource  implements Serializable {

  @Id
  @GeneratedValue
  private Long id;

  private String name; //数据资源名称

  private String type; //数据类型(人物数据、动态数据等)

  private String db; // 数据库（MySQL、MongoDB）

  private String url; // 连接,数据库ip地址

  private String host; // 数据库主机IP

  private String port;//数据库主机端口号

  private String dbName; //数据库名称

  private String username;//数据库用户名

  private String password;//数据库密码

  private String collection; // 集合

  private String org; // 数据提供方（组织）

  private String description; //数据源描述


  public Resource() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDb() {
    return db;
  }

  public void setDb(String db) {
    this.db = db;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCollection() {
    return collection;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public String getOrg() {
    return org;
  }

  public void setOrg(String org) {
    this.org = org;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }
}
