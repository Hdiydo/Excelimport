package org.zxiat.entity.target;


import java.io.Serializable;

public class Snak implements Serializable {
    /**
     * MTXT:多语言文本, COD:地理坐标, AMT：数量-WIKI, TIME：时间-WIKI, EID：实体表示 ,STR：字符串,
     * Date：Date
     */
    public static enum Type {
        STR, NUM, MTXT, AMT, TIME, EID, COD, TEXT
    }

    private Type tp;
    private Object val;
    private String prop;
    private Gene src;

    public Snak() {}
    public Snak(Object value) {this.val = value;}
    public Snak(Type type, Object value) {
        this.tp = type;
        this.val = value;
    }

    public Type getTp() {
        return tp;
    }

    public void setTp(Type tp) {
        this.tp = tp;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public Gene getSrc() {
        return src;
    }

    public void setSrc(Gene src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return "Snak{" +
                "tp=" + tp +
                ", val=" + val +
                ", prop='" + prop + '\'' +
                ", src='" + src + '\'' +
                '}';
    }
}
