package org.zxiat.entity.target.value.helper;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Emiya
 */
public class EntityValueHelper {
    private final static DateFormat F1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    private final static DateFormat F11 = new SimpleDateFormat("yyyyMMdd");
    private final static DateFormat F2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private final static DateFormat F21 = new SimpleDateFormat("yyyy/MM/dd");
    private final static DateFormat F3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static DateFormat F31 = new SimpleDateFormat("yyyy-MM-dd");
    private final static DateFormat F4 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private final static DateFormat F41 = new SimpleDateFormat("yyyy.MM.dd");

    public static String format(String time) {
        Date date = stringToDate(time);
        if (date == null) {
            return null;
        }
        return F2.format(date);
    }

    public static Date stringToDate(String time) {
        DateFormat formatter;
        time = time.trim();

        if (time.indexOf("/") > -1) {
            if (time.indexOf(" ") > -1) {
                formatter = F2;
            } else {
                formatter = F21;
            }
        } else if (time.indexOf("-") > -1) {
            if (time.indexOf(" ") > -1) {
                formatter = F3;
            } else {
                formatter = F31;
            }
        } else if (time.indexOf(".") > -1) {
            if (time.indexOf(" ") > -1) {
                formatter = F4;
            } else {
                formatter = F41;
            }
        } else {
            if (time.indexOf(" ") > -1) {
                formatter = F1;
            } else {
                formatter = F11;
            }
        }
        ParsePosition pos = new ParsePosition(0);

        return formatter.parse(time, pos);
    }

}
