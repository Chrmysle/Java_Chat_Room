package com.bx.implatform.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeUtils extends DateUtils {

    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String PARTDATEFORMAT = "yyyyMMdd";


    public static String getFormatDate(Date date, String xFormat) {
        date = date == null ? new Date() : date;
        xFormat = StringUtils.isNotEmpty(xFormat) ? xFormat : FULL_DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(xFormat);
        return sdf.format(date);
    }


}