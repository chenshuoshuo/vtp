package com.you07.vtp.model.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName
 * @Description 时间工具类
 * @Author ts
 * @Date 2019/6/21 10:23
 * @Version 1.0
 **/
public class DateUtils {

    private static DateUtils dateUtils;

    private DateUtils(){};
    /**
     *
     * 获得一个单一实例
     * @return MD5
     */
    public synchronized static DateUtils getDefaultInstance(){
        if(dateUtils==null){
            dateUtils = new DateUtils();
        }
        return dateUtils;
    }

    /**
     * 获取日期
     * @param date
     * @param formatStr
     * @return
     */
    public String formatDate(Date date,String formatStr){
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    /**
     * 获取日期
     * yyyy-MM-dd格式
     * @return
     */
    public String getDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String day = sdf.format(calendar.getTime());
        return  day;
    }
    /**
     * 获取时间
     * hh:ss格式
     * @return
     */
    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        String time = sdf.format(calendar.getTime());
        return  time;
    }
    /**
     * 获取日期
     * MM-dd格式
     * @return
     */
    public String getToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        String day = sdf.format(calendar.getTime());
        return  day;
    }
    /**
     * 获取今天的开始时间与结束时间
     * @return
     */
    public String[] getTodayArray(){
        String[] timeArray = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        timeArray[0] = sdf.format(calendar.getTime()) + " 00:00:00";
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        timeArray[1] = sdf.format(calendar.getTime()) + " 00:00:00";
        return timeArray;
    }
    /**
     * 获取当周的开始时间与结束时间
     * @return
     */
    public String[] getThisWeekArray(){
        String[] timeArray = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(day_of_week == 0){
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE,7 -day_of_week+1);
        timeArray[1] = sdf.format(calendar.getTime()) + " 00:00:00";
        calendar.add(Calendar.DATE, -7);
        timeArray[0] = sdf.format(calendar.getTime()) + " 00:00:00";
        return timeArray;
    }

    /**
     * 获取当月的开始时间与结束时间
     * @return
     */
    public String[] getThisMonthArray(){
        String[] timeArray = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH) -1;
        calendar.add(Calendar.DATE, -day_of_month);
        timeArray[0] = sdf.format(calendar.getTime()) + " 00:00:00";
        calendar.add(Calendar.MONTH,1);
        timeArray[1] = sdf.format(calendar.getTime()) + " 00:00:00";
        return timeArray;
    }
    /**
     * 获取当月之前3个月的开始时间与结束时间
     * @return
     */
    public String[] getThreeMonthArray(){
        String[] timeArray = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH) -1;
        calendar.add(Calendar.DATE, -day_of_month);
        timeArray[1] = sdf.format(calendar.getTime()) + " 00:00:00";
        calendar.add(Calendar.MONTH,-3);
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

    /**
     * 根据指定开始日期、结束日期
     * 获取开始时间与结束时间
     * @return
     */
    public String[] getArrayWithDateRange(String startDate, String endDate) throws ParseException {
        String[] timeArray = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(sdf.parse(startDate));
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        timeArray[0] = sdf.format(calendar.getTime()) + " 00:00:00";


        calendar.setTime(sdf.parse(endDate));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        timeArray[1] = sdf.format(calendar.getTime()) + " 00:00:00";

        return timeArray;
    }

    /**
     * 根据指定日期、时间跨度
     * 获取开始时间与结束时间
     * @return
     */
    public String[] getArrayWithDateAndRange(String dateString, Integer range) throws ParseException {
        String[] timeArray = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(dateString));

        if(range > 0){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            timeArray[0] = sdf.format(calendar.getTime()) + " 00:00:00";

            calendar.add(Calendar.DAY_OF_YEAR, range);
            timeArray[1] = sdf.format(calendar.getTime()) + " 00:00:00";
        } else{
            timeArray[1] = sdf.format(calendar.getTime()) + " 00:00:00";

            calendar.add(Calendar.DAY_OF_YEAR, range);
            timeArray[0] = sdf.format(calendar.getTime()) + " 00:00:00";
        }

        return timeArray;
    }
    /**
     * 根据开始时间、结束时间
     * 获取之间时间列表
     * @return
     */
    public List<String> findDates(String begin, String end) throws ParseException {
        List<String> lDate = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sta = sdf.format(sdf.parse(begin));
        lDate.add(sta);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(sdf.parse(begin));
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间  
        calEnd.setTime(sdf.parse(end));
        // 测试此日期是否在指定日期之后
        while (sdf.parse(end).after(calBegin.getTime()))
        {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sdf.format(calBegin.getTime()));
        }
        return lDate;
    }
    /**
     * 获取当前日期是星期几
     *
     * @param date
     * @return 当前日期是星期几
     */
    public String getWeekOfDate(String date) {
        String[] weekDays = { "星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        int weekly = 0;
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cal.setTime(sdf.parse(date));
            weekly = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (weekly < 0){
                weekly = 0;
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return weekDays[weekly];
    }

    /**
     * 根据开始日期和当前日期，获取当前是第几周
     * @param beginDateStr 开始日期
     * @param endDateStr 当前日期
     * @return
     */
     public long getWeek(String beginDateStr,String endDateStr)
     {
         long day=0;
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
         Date beginDate = null;
         Date endDate = null;
         try {
             //先判断学期开始日期是星期几
             Calendar calendar = Calendar.getInstance();// 获得一个日历的实例
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             calendar.setTime(sdf.parse(beginDateStr));
             String dayNames[] = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
             //对应的星期几和星期一倒推相差几天
             int dayNum[] = {6,0,1,2,3,4,5};
             //算出的星期几，和星期一差几天就把开始时间多倒推几天
             calendar.add(Calendar.DATE, -dayNum[calendar.get(Calendar.DAY_OF_WEEK)-1]);
             beginDate=calendar.getTime();
             //beginDate = format.parse(beginDateStr);
             //System.out.println(sdf.format(beginDate));
             endDate= format.parse(endDateStr);
             day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
             //System.out.println("相隔的天数="+day);
         } catch (ParseException e) {
             e.printStackTrace();
         }
         return day/7+1;
     }

}
