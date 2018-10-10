package com.you07.util;



import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
    private static DateUtil dateUtil;

    private DateUtil(){};
    /**
     *
     * 获得一个单一实例
     * @return MD5
     */
    public synchronized static DateUtil getDefaultInstance(){
        if(dateUtil==null){
            dateUtil = new DateUtil();
        }
        return dateUtil;
    }

    /**
     * 获取昨天的开始时间与结束时间
     * yyyy-MM-dd格式
     * @return
     */
    public String[] getYesterdayArray(){
        String[] timeArray = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        timeArray[1] = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        timeArray[0] = sdf.format(calendar.getTime());
        return timeArray;
    }

    /**
     * 获取上周的开始时间与结束时间
     * @return
     */
    public String[] getLastWeekArray(){
        String[] timeArray = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(day_of_week == 0)
            day_of_week = 7;
        calendar.add(Calendar.DATE, -day_of_week + 1);
        timeArray[1] = sdf.format(calendar.getTime()) + " 00:00:00";
        calendar.add(Calendar.DATE, -7);
        timeArray[0] = sdf.format(calendar.getTime()) + " 00:00:00";
        return timeArray;
    }

    /**
     * 获取上月的开始时间与结束时间
     * @return
     */
    public String[] getLastMonthArray(){
        String[] timeArray = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        calendar.add(Calendar.DATE, -day_of_month);
        timeArray[1] = sdf.format(calendar.getTime()) + " 00:00:00";
        calendar.add(Calendar.MONTH, -1);
        timeArray[0] = sdf.format(calendar.getTime()) + " 00:00:00";
        return timeArray;
    }

    /**
     * 获取当前学年
     * @return
     */
    public Integer getSchoolYear(){
        Calendar calendar = Calendar.getInstance();
        Integer schoolYear = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if(month < 7){
            schoolYear -= 1;
        }

        return schoolYear;
    }

    /**
     * 获取当前学期
     * @return
     */
    public Integer getSemaster(){
        Calendar calendar = Calendar.getInstance();
        Integer semaster = 1;
        int month = calendar.get(Calendar.MONTH);
        if(month < 7 && month > 0){
            semaster = 2;
        }

        return semaster;
    }

}
