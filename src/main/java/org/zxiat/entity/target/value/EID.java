package org.zxiat.entity.target.value;

import java.io.Serializable;

public class EID implements Serializable {
    private String label;
    private String eid;

    public EID() {
    }

    public EID(String label, String eid) {
        this.label = label;
        this.eid = eid;
    }

    @Override
    public String toString() {
        return label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }
}
