package org.zxiat.entity.target.value;

import org.zxiat.entity.target.value.helper.EntityValueHelper;

import java.io.Serializable;
import java.util.Date;

public class TIME implements Serializable {

    private static final long serialVersionUID = 1L;
    private String time;
    private int zone;
    private int prs;
    private String cal;

    public TIME() {
    }

    public TIME(String time) {
        this.time = EntityValueHelper.format(time);
    }

    public TIME(String time, int zone, int prs, String cal) {
        this.time = time;
        this.zone = zone;
        this.prs = prs;
        this.cal = cal;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int timezone) {
        this.zone = timezone;
    }

    public int getPrs() {
        return prs;
    }

    public void setPrs(int precision) {
        this.prs = precision;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String calendarmodel) {
        this.cal = calendarmodel;
    }

    public Date toDate() {
        return EntityValueHelper.stringToDate(time);
    }

    @Override
    public String toString() {
        return time;
    }
}
