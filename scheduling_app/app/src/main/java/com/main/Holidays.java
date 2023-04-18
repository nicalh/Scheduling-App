// ******* file not used in current build *******

//package com.main;
//
//import com.main.Caller.ShiftInfo;
//
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.Formatter;
//import java.util.GregorianCalendar;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Set;
//import java.util.StringTokenizer;
//
//public class Holidays {
//    public Hashtable<Date, String> holidays;
//
//    public Holidays() {
//        holidays = new Hashtable<Date, String>();
//        holidays.put(new Date(2021-1899, 12, 24), "Christmas Eve");
//        holidays.put(new Date(2021-1899, 12, 25), "Christmas Day");
//        holidays.put(new Date(2021-1899, 12, 26), "Boxing Day");
//        holidays.put(new Date(2021-1899, 12, 31), "New Year's Eve");
//        holidays.put(new Date(2022-1900, 1, 1), "New Year's Day");
//        holidays.put(new Date(2022-1900, 4, 15), "Good Friday");
//        holidays.put(new Date(2022-1900, 5, 23), "Victoria Day");
//        holidays.put(new Date(2022-1900, 7, 1), "Canada Day");
//        holidays.put(new Date(2022-1900, 8, 1), "Heritage Day");
//        holidays.put(new Date(2022-1900, 9, 5), "Labour Day");
//        holidays.put(new Date(2022-1900, 9, 30), "Truth and Reconciliation");
//        holidays.put(new Date(2022-1900, 10, 10), "Thanksgiving");
//        holidays.put(new Date(2022-1900, 11, 11), "Remembrance Day");
//        holidays.put(new Date(2022-1900, 12, 24), "Christmas Eve");
//        holidays.put(new Date(2022-1900, 12, 25), "Christmas Day");
//        holidays.put(new Date(2022-1900, 12, 31), "New Year's Eve");
//    }
//
//    public Hashtable<Date, String> getHolidays() {
//        return holidays;
//    }
//
//    public void addHoliday(Date date, String holiday){
//        holidays.put(date,holiday);
//    }
//
//    public boolean isHoliday(Long currentTimeMillis){
//        Date date = new Date(currentTimeMillis);
//        return holidays.containsKey(date);
//    }
//
//    public ArrayList<String> upcomingHolidays(){
//        ArrayList<String> upcoming_holidays = new ArrayList<String>();
//        Date current_date = new Date(System.currentTimeMillis());
//
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Set<Date> dates = holidays.keySet();
//        List<Date> date_list = new ArrayList<Date>();
//        for (Date date : dates){
//            date_list.add(date);
//        }
//
//        Collections.sort(date_list, new SortByDate());
//        for(Date date : date_list){
//            if (current_date.before(date)){
//                String holiday = holidays.get(date);
//                String fin_date = formatter.format(date);
//                String fin_string = holiday + ", " + fin_date;
//                upcoming_holidays.add(fin_string);
//            }
//        }
//        return upcoming_holidays;
//    }
//
//    static class SortByDate implements Comparator<Date> {
//        @Override
//        public int compare(Date a, Date b) {
//            return a.compareTo(b);
//        }
//    }
//
//
//}
//
//
//
