package com.example.demo.model;

public class StartDay {
    private static boolean startDay= false;;

    public static boolean isStartDay() {
        return startDay;
    }

    public static void setStartDay(boolean startDay) {
        StartDay.startDay = startDay;
    }
}