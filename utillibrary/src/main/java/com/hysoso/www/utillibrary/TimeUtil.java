package com.hysoso.www.utillibrary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LuHe on 2016/11/10.
 */

public class TimeUtil {
    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        final int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        return curMonth;
    }

    public interface OnClickCallBack {
        void onClick(Boolean isTruth, String year, String month, String day, String hour);
    }

    private static String defaultDateFormater = "yyyy-MM-dd HH:mm:ss";

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String formater) {
        if (!isDateFormater(formater)) {
            formater = defaultDateFormater;
        }
        SimpleDateFormat df = new SimpleDateFormat(formater);
        return df.format(new Date());
    }

    public static long getCurrentTime() {
        return new Date().getTime() / 1000;
    }

    /**
     * 返回前一天系统时间
     */
    public static String getPreDataTime(String formater) {
        if (!isDateFormater(formater)) {
            formater = defaultDateFormater;
        }
        SimpleDateFormat df = new SimpleDateFormat(formater);
        Date nowDate = new Date();
        Date preDate = new Date();

        Calendar calendar = Calendar.getInstance();//得到日历
        calendar.setTime(nowDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        preDate = calendar.getTime();

        return df.format(preDate);
    }

    /**
     * 毫秒值转换为mm:ss
     *
     * @param ms
     */
    public static String timeFormat(int ms) {
        StringBuilder time = new StringBuilder();
        time.delete(0, time.length());
        ms /= 1000;
        int s = ms % 60;
        int min = ms / 60;
        if (min < 10) {
            time.append(0);
        }
        time.append(min).append(":");
        if (s < 10) {
            time.append(0);
        }
        time.append(s);
        return time.toString();
    }

    /**
     * 将字符串转位日期类型
     *
     * @return Date
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串时间是否为昨日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isYesterday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date yesterday = toDate(getPreDataTime(defaultDateFormater));
        if (time != null) {
            String yesterdayDate = dateFormater2.get().format(yesterday);
            String timeDate = dateFormater2.get().format(time);
            if (yesterdayDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    public static String getDayDate(String sdate) {
        String timeDate = null;
        Date time = toDate(sdate);
        if (time != null) timeDate = dateFormater2.get().format(time);
        return timeDate;
    }

    /**
     * 将时间戳转换成时间
     *
     * @param timeStamp
     * @param formater
     * @return
     */
    public static String getStrTime(String timeStamp, String formater) {
        if (!isDateFormater(formater)) {
            formater = defaultDateFormater;
        }
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        long l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    /**
     * 将时间转换成时间戳
     *
     * @param time
     * @param formater
     * @return
     */
    public static String getTimeTag(String time, String formater) {
        if (!isDateFormater(formater)) {
            formater = defaultDateFormater;
        }
        SimpleDateFormat sdr = new SimpleDateFormat(formater,
                Locale.CHINA);
        Date date = null;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            times = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    private static Boolean isDateFormater(String formater) {
        if (formater != null && (formater.contains("yyyy")
                || formater.contains("MM")
                || formater.contains("dd")
                || formater.contains("HH")
                || formater.contains("mm")
                || formater.contains("ss"))) {
            return true;
        } else
            return false;
    }

    /**
     * @param year
     * @param month
     * @return
     */
    private static int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    public static Integer getCurrentYear() {
        Calendar c = Calendar.getInstance();
        Integer curYear = c.get(Calendar.YEAR);
        return curYear;
    }

    public static Integer getCurrentDay() {
        Calendar c = Calendar.getInstance();
        int curDate = c.get(Calendar.DATE);
        return curDate;
    }

    /*
    从两个日期中获取最近的
    * */
    public static String getLastDate(String... dates) {
        String last = dates.length == 0 ? null : dates[0];
        for (int i = 0; i < dates.length; i++) {
            Date dateLast = StringUtil.isEmpty(last) ? toDate("2000-01-01 0:0:0") : toDate(last);
            String str = dates[i];
            if (!StringUtil.isEmpty(str)) {
                if (toDate(str).getTime() > dateLast.getTime()) last = str;
            }
        }
        return last;
    }
}

