package com.matianyi.accountingapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    //unix time -> 11:11
    public static String getFormattedTime(long timeStamp){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(new Date(timeStamp));
    }

    //2019-06-01
    public static String getFormattedDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    private static Date strToDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String getWeekDay(String date){
        String[] weekdays = {"周日","周一","周二","周三","周四","周五","周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(date));
        int index = calendar.get(Calendar.DAY_OF_WEEK)-1;
        return  weekdays[index];
    }

    public static String getDateTitle(String date){
        String[] months ={"1月", "2月", "3月", "4月", "5月", "6月","7月", "8月","9月","10月", "11月", "12月"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(date));
        int monthIndex = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return  months[monthIndex] + " "+String.valueOf(day);
    }

    public static String convertSqlMonthToCharacter(String sqlMonth){
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月","7月", "8月","9月","10月", "11月", "12月"};
        String convertedMonth = "未知";
        switch (sqlMonth) {
            case "01": return months[0];
            case "02": return months[1];
            case "03": return months[2];
            case "04": return months[3];
            case "05": return months[4];
            case "06": return months[5];
            case "07": return months[6];
            case "08": return months[7];
            case "09": return months[8];
            case "10": return months[9];
            case "11": return months[10];
            case "12": return months[11];
        }

        return convertedMonth;
    }

    public static boolean isSelectedDateAfterToday(String selectedDate, String today){
        return strToDate(today).before(strToDate(selectedDate));
    }
}
