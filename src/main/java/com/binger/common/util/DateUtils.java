package com.binger.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

    public final static String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String SIMPLE_FORMAT = "yyyy-MM-dd";

    /**
     * @param date
     * @return
     */
    public static String format(Date date) {
        if (!ObjectUtils.isEmpty(date)) {
            SimpleDateFormat format = new SimpleDateFormat(FORMAT);
            return format.format(date);
        }
        return null;
    }

    /**
     * @param date
     * @return
     */
    public static String simpleFormat(Date date) {
        if (!ObjectUtils.isEmpty(date)) {
            SimpleDateFormat format = new SimpleDateFormat(SIMPLE_FORMAT);
            return format.format(date);
        }
        return null;
    }

    /**
     * @param type
     * @param num
     * @return
     */
    public static String getDate(Date date, int type, int num) {
        if (!ObjectUtils.isEmpty(date)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(type, num);
            return simpleFormat(calendar.getTime());
        }
        return null;
    }

    /**
     * 计算两个日期的天数差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int daysBetween(Date startDate, Date endDate) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endDate);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            //不同年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)  {
                    //闰年
                    timeDistance += 366;
                } else {
                    //不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {
            //同一年
            return day2 - day1;
        }
    }

    /**
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        if (ObjectUtils.isEmpty(date) || StringUtils.isEmpty(format)) {
            return null;
        }
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    /**
     * 时间戳转换成日期格式
     *
     * @param strDate
     * @param format
     * @return
     */
    public static String timestampToDate(String strDate, String format) {
        if (StringUtils.isEmpty(strDate) || StringUtils.isEmpty(format)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(new Long(strDate));
        return simpleDateFormat.format(date);

    }

    /**
     * 过去七天日期
     *
     * @return
     */
    public static Date getLastSevenDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -7);
        return c.getTime();
    }

    /**
     * 字符串转时间格式
     *
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        Date date = null;
        if (!StringUtils.isEmpty(str)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 字符串转日期
     *
     * @param date
     * @return
     */
    public static Date stringParseToDate(String date) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(date)) {
            date = date.replace("/", "-");
            Pattern a = Pattern.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
            Matcher matcher = a.matcher(date);
            SimpleDateFormat sdf;
            if (matcher.matches()) {
                sdf = new SimpleDateFormat(SIMPLE_FORMAT);
            } else {
                sdf = new SimpleDateFormat(FORMAT);
            }
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Integer compareDate(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return 1;
        } else if (date1.getTime() < date2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }
}
