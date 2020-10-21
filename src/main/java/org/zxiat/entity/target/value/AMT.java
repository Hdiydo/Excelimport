package org.zxiat.entity.target.value;

import java.io.Serializable;

public class AMT implements Serializable {

    private static final long serialVersionUID = 1L;
    private String amt;
    private String unt;
    private String up;
    private String low;

    public AMT() {
        super();
    }

    public AMT(String amount) {
        super();
        this.amt = amount;
    }

    public AMT(String amount, String unit) {
        super();
        this.amt = amount;
        this.unt = unit;
    }

    public AMT(String amt, String unt, String up, String low) {
        this.amt = amt;
        this.unt = unt;
        this.up = up;
        this.low = low;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amount) {
        this.amt = amount;
    }

    public String getUnt() {
        return unt;
    }

    public void setUnt(String unit) {
        this.unt = unit;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String upperBound) {
        this.up = upperBound;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String lowerBound) {
        this.low = lowerBound;
    }

    @Override
    public String toString() {
        return amt + unt;
    }
}
