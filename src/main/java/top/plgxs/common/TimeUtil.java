package top.plgxs.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

/**
 * 功能：日期处理相关的操作
 * @author lipian
 */
public class TimeUtil {

    public static final ThreadLocal<DateFormat> datetimeFormat = new ThreadLocal<DateFormat>() {
                                                                   @Override
                                                                   protected DateFormat initialValue() {
                                                                       return new SimpleDateFormat(
                                                                           "yyyy-MM-dd HH:mm:ss");
                                                                   }
                                                               };

    public static final ThreadLocal<DateFormat> dateFormat     = new ThreadLocal<DateFormat>() {
                                                                   @Override
                                                                   protected DateFormat initialValue() {
                                                                       return new SimpleDateFormat(
                                                                           "yyyy-MM-dd");
                                                                   }
                                                               };

    /**
     * 取得当天日期,格式 2009-02-11
     * @return
     */
    public static String getToday() {
        Calendar cl = new GregorianCalendar();
        return dateFormat.get().format(cl.getTime());
    }

    /**
     * 取得当天日期,格式 20090211
     * @return
     */
    public static String getTodayNumber() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
        Calendar cl = new GregorianCalendar();
        return sdf.format(cl.getTime());
    }

    /**
     * 给出日期转换成格式 2009-02-11,
     * 如果date为空那么返回null
     * @param date
     * @return
     */
    public static String getDayStr(Date date) {
        if (date == null) {
            return null;
        }
        return dateFormat.get().format(date);
    }

    /**
     * 取得当天日期时间,格式 2009-02-11 23:9:21
     * @return
     */
    public static String getTodaytime() {
        Calendar cl = new GregorianCalendar();
        return getToday() + " " + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":"
               + cl.get(Calendar.SECOND) + " ";
    }

    /**
     * 取得当前时间,格式 23:12:20
     * @return
     */
    public static String getTime() {
        Calendar cl = new GregorianCalendar();
        return cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":"
               + cl.get(Calendar.SECOND) + " ";
    }

    /**
     * 取得当前年份
     * @return
     */
    public static String getYear() {
        return TimeUtil.getNoFormatToday().substring(0, 4);
    }

    /**
     * 取得当前月份
     * @return
     */
    public static String getMonth() {
        return TimeUtil.getNoFormatToday().substring(4, 6);
    }

    /**
     * 获取当前属于上半年还是下半年
     * @return
     */
    public static String getBanNian() {
        String ban = "上半年";
        String month = TimeUtil.getMonth();
        if (StringUtils.isNotBlank(month)) {
            int m = 0;
            try {
                m = Integer.parseInt(month);
                if (m > 6) {
                    ban = "下半年";
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        return ban;
    }

    /**
     * 获取当前属于那一季
     * @return
     */
    public static String getJiDu() {
        String jidu = "1";
        String month = TimeUtil.getMonth();
        if (StringUtils.isNotBlank(month)) {
            int m = 0;
            try {
                m = Integer.parseInt(month);
                if (m <= 3) {
                    jidu = "1";
                } else if (m <= 6) {
                    jidu = "2";
                } else if (m <= 6) {
                    jidu = "3";
                } else {
                    jidu = "4";
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        return jidu;
    }

    /**
     * 取得当前日
     * @return
     */
    public static String getDay() {
        return TimeUtil.getNoFormatToday().substring(6, 8);
    }

    /**
     * 取得当前小时
     * @return
     */
    public static int getHour() {
        Calendar cl = new GregorianCalendar();
        return cl.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 取得当前分钟
     * @return
     */
    public static int getMinute() {
        Calendar cl = new GregorianCalendar();
        return cl.get(Calendar.MINUTE);
    }

    /**
     * 取得当前秒
     * @return
     */
    public static int getSecond() {
        Calendar cl = new GregorianCalendar();
        return cl.get(Calendar.SECOND);
    }

    /**
     * 取得当前日期 格式为20090211
     * @return
     */
    public static String getNoFormatToday() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
        Calendar cl = new GregorianCalendar();
        return sdf.format(cl.getTime());
    }

    /**
     * 取得当前时间 格式为231611
     * @return
     */
    public static String getNoFormatTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HHmmss");
        Calendar cl = new GregorianCalendar();
        return sdf.format(cl.getTime());
    }

    /**
     * 返回N天前（后的）日期，正数是后的日期，负数是前的日期。例如：2009-02-11 12:12:12
     * @param number
     * @return
     */
    public static String getYesterday(int number) {
        String strYesterday = "";
        Calendar cale = null;
        cale = new GregorianCalendar();
        cale.add(Calendar.DATE, number);
        strYesterday = TimeUtil.getStrByCalendar(cale);
        return strYesterday;
    }

    public static boolean dateFlag(int number, String nowdate) {
        boolean flag = false;
        try {
            String d = getYesterday(-number);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Date yesterday = sdf.parse(d);
            Date nd = sdf.parse(nowdate);
            flag = yesterday.before(nd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String getStrByCalendar(Calendar cale) {
        return dateFormat.get().format(cale.getTime());
    }

    /**
     * 日期字符串的格式转换,例如"2009-02-11"转换为2009年2月11日
     * @param sDate
     * @return
     */
    public static String getChnDateString(String sDate) {
        if (sDate == null) {
            return null;
        }
        sDate = sDate.trim();
        if (sDate.length() == 7) {
            sDate += "-01";
        }
        StringTokenizer st = new StringTokenizer(sDate, "-");
        int year = 2100;
        int month = 0;
        int day = 1;
        try {
            year = Integer.parseInt(st.nextToken());
            month = Integer.parseInt(st.nextToken()) - 1;
            day = Integer.parseInt(st.nextToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cl = new GregorianCalendar(year, month, day);
        return cl.get(Calendar.YEAR) + "年" + (cl.get(Calendar.MONTH) + 1) + "月"
               + cl.get(Calendar.DATE) + "日";
    }

    /**
     * 取得某年某月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static String getMaxDayOfMonth(int year, int month) {
        Calendar cal = new GregorianCalendar(year, month - 1, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 取得某年某月的第一天
     * @param year
     * @param month
     * @return
     */
    public static String getMinDayOfMonth(int year, int month) {
        Calendar cal = new GregorianCalendar(year, month - 1, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dateFormat.get().format(cal.getTime());
    }

    /**
     * 取得当天的中文日期，像2006年11月28日 星期二
     * @return
     */
    public static String getChineseToDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 E", Locale.CHINESE);
        Calendar cl = new GregorianCalendar();
        return sdf.format(cl.getTime());
    }

    /**
     * 取得当天的中文日期，像2006年11月28日 星期二 下午05:06
     * @return
     */
    public static String getChineseToDayTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 E a", Locale.CHINESE);
        Calendar cl = new GregorianCalendar();
        return sdf.format(cl.getTime());
    }

    /**
     * 根据字符串，取得日期类
     * @param sDate
     * @return
     */
    public static Calendar getDate(String sDate) {
        if (sDate == null) {
            return null;
        }
        sDate = sDate.trim();
        if (sDate.length() == 7) {
            sDate += "-01";
        }
        StringTokenizer st = new StringTokenizer(sDate, "-");
        int year = 2100;
        int month = 0;
        int day = 1;
        try {
            year = Integer.parseInt(st.nextToken());
            month = Integer.parseInt(st.nextToken()) - 1;
            day = Integer.parseInt(st.nextToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GregorianCalendar(year, month, day);
    }

    /**
     * 根据日期类取得日期的字符串形式
     * @param sDate
     * @return
     */
    public static String getDateString(Calendar sDate) {
        if (sDate == null) {
            return "";
        }
        return dateFormat.get().format(sDate.getTime());
    }

    /**
     * 根据日期类取得日期的字符串形式
     * <p>yyyy-MM-dd</p>
     * @param sDate
     * @return
     */
    public static String getDateString(Date sDate) {
        if (sDate == null) {
            return "";
        }
        return dateFormat.get().format(sDate.getTime());
    }

    /**
     * 字符串转换成日期
     * <p>yyyy-MM-dd HH:mm:ss</p>
     * @param str
     * @return date
     */
    public static Date strToDate(String str) {
        Date date = null;
        try {
            date = dateFormat.get().parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据日期类取得日期的字符串形式
     * <p>yyyy-MM-dd HH:mm:ss</p>
     * @param sDate
     * @return
     */
    public static String getDateTimeString(Date sDate) {
        if (sDate == null) {
            return "";
        }
        return datetimeFormat.get().format(sDate.getTime());
    }

    /**
     * 字符串转换成日期
     * <p>yyyy-MM-dd HH:mm:ss</p>
     * @param str
     * @return date
     */
    public static Date strToDateTime(String str) {
        Date date = null;
        try {
            date = datetimeFormat.get().parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**根据日期类取年月的字符串形式
     * @param sDate
     * @return
     */
    public static String getYearMonth(Calendar sDate) {
        if (sDate == null) {
            return "";
        }
        return (new java.text.SimpleDateFormat("yyyy-MM")).format(sDate.getTime());
    }

    /**比较两个日期类型的字符串，格式为（yyyy-mm-dd）
     * 如果cale1大于cale2，返回1
     * 如果cale1小于cale2，返回-1
     * 如果相等，返回0
     * 如果格式错误，返回-2
     * @param cale1
     * @param cale2
     * @return
     */
    public static int compareCalendar(String cale1, String cale2) {
        Calendar calendar1 = getDate(cale1);
        Calendar calendar2 = getDate(cale2);
        if (calendar1 == null || calendar2 == null) {
            return -2;
        }
        return calendar1.compareTo(calendar2);
    }

    /**获取当前日期   格式 yyyy-MM-01 00:00:01
     * @return
     */
    public static String getYearMonth() {

        SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM");
        return sf.format(new Date()) + "-01 00:00:00";
    }

    /**
     * 获取当前时间字符串 格式:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDateTimeString() {
        return datetimeFormat.get().format(new Date());
    }

}
