package org.zxiat.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LocalData",uniqueConstraints = {@UniqueConstraint(columnNames="MLABEL")})
@Data
public class LocalData implements Serializable {
    @Id
    private String id;
    @Column(name = "MLABEL")
    private String mlabel; //数据名称
    @Column(name = "DATA",columnDefinition = "text")
    private String data; // 数据JSON

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMlabel() {
        return mlabel;
    }
    public void setMlabel(String mlabel) {
        this.mlabel = mlabel;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return " Target{" +
                "id=" + id +
                ", mlabel='" + mlabel + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
