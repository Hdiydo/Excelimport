package org.zxiat.entity.target;

import lombok.Data;

import java.io.Serializable;

@Data
public class Gene implements Serializable {
    private String src;  //表名
    private String name;  //控制点
    private String color;
    private String url;
    private Object timestamp; // 日期

    public Gene() {
    }

    public Gene(String src, String name, Object timestamp) {
        this.src = src;
        this.name = name;
        this.timestamp = timestamp;
    }

    public Gene(String src, String name, String color, Object timestamp) {
        this.name = name;
        this.color = color;
        this.src = src;
        this.timestamp = timestamp;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
