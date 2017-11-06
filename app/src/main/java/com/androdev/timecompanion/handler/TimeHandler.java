package com.androdev.timecompanion.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ayushsingh on 02/01/17.
 */

public class TimeHandler {

    public String timeUpdate() {

        String day;
        int temp, j;
        String t;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HHmm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String localTime = date.format(currentLocalTime);
        DateFormat df = new SimpleDateFormat("EEE");
        day = df.format(Calendar.getInstance().getTime());
        j = Integer.parseInt(localTime.replaceAll("[\\D]", ""));

        if (day.equals("Sat") || day.equals("Sun"))
            return ("Enjoy The Weekend");
        else if (j > 0 && j <= 500) {
            return ("Have a Good Night");
        } else if (j > 500 && j <= 700) {
            return ("Good Morning");
        } else if (j > 700 && j < 800) {
            temp = 800 - j - 40;
            t = Integer.toString(temp);
            return ("College will start in " + t + " Mins.");
        } else if (j > 800 && j <= 850) {
            temp = 850 - j;
            t = Integer.toString(temp);
            return ("Hour 1 will finish in " + t + " Mins.");
        } else if (j > 850 && j <= 940) {
            if (j < 900) {
                temp = 940 - j - 40;
            } else {
                temp = 940 - j;
            }
            t = Integer.toString(temp);
            return ("Hour 2 will finish in " + t + " Mins.");
        } else if (j > 940 && j <= 945) {
            temp = 945 - j;
            t = Integer.toString(temp);
            return ("Hour 3 will start in " + t + " Mins.");
        } else if (j > 945 && j <= 1035) {
            if (j < 1000) {
                temp = 1035 - j - 40;
            } else {
                temp = 1035 - j;
            }
            t = Integer.toString(temp);
            return ("Hour 3 will finish in " + t + " Mins.");
        } else if (j > 1035 && j <= 1040) {
            temp = 1040 - j;
            t = Integer.toString(temp);
            return ("Hour 4 will start in " + t + " Mins.");
        } else if (j > 1040 && j <= 1130) {
            if (j < 1100) {
                temp = 1130 - j - 40;
            } else {
                temp = 1130 - j;
            }
            t = Integer.toString(temp);
            return ("Hour 4 will finish in " + t + " Mins.");
        } else if (j > 1130 && j <= 1135) {
            temp = 1040 - j;
            t = Integer.toString(temp);
            return ("Hour 5 will start in " + t + " Mins.");
        } else if (j > 1135 && j <= 1225) {
            if (j < 1200) {
                temp = 1230 - j - 40;
            } else {
                temp = 1230 - j;
            }
            t = Integer.toString(temp);
            return ("Hour 5 will finish in " + t + " Mins.");
        } else if (j > 1225 && j <= 1230) {
            temp = 1230 - j;
            t = Integer.toString(temp);
            return ("Hour 6 will start in " + t + " Mins.");
        } else if (j > 1230 && j <= 1320) {
            if (j < 1300) {
                temp = 1320 - j - 40;
            } else {
                temp = 1320 - j;
            }
            t = Integer.toString(temp);
            return ("Hour 6 will finish in " + t + " Mins.");
        } else if (j > 1320 && j <= 1325) {
            temp = 1325 - j;
            t = Integer.toString(temp);
            return ("Hour 7 will start in " + t + " Mins.");
        } else if (j > 1325 && j <= 1415) {
            if (j < 1400) {
                temp = 1415 - j - 40;
            } else {
                temp = 1415 - j;
            }
            t = Integer.toString(temp);
            return ("Hour 7 will finish in " + t + " Mins.");
        } else if (j > 1415 && j <= 1420) {
            temp = 1420 - j;
            t = Integer.toString(temp);
            return ("Hour 8 will start in " + t + " Mins.");
        } else if (j > 1420 && j <= 1510) {
            if (j < 1500) {
                temp = 1510 - j - 40;
            } else {
                temp = 1510 - j;
            }
            t = Integer.toString(temp);
            return ("Hour 8 will finish in " + t + " Mins.");
        } else if (j > 1510 && j <= 1515) {
            temp = 1515 - j;
            t = Integer.toString(temp);
            return ("Hour 9 will start in " + t + " Mins.");
        } else if (j > 1515 && j <= 1605) {
            if (j < 1600) {
                temp = 1605 - j - 40;
            } else {
                temp = 1605 - j;
            }
            t = Integer.toString(temp);
            return ("Hour 9 will finish in " + t + " Mins.");
        } else if (j > 1605 && j <= 1655) {
            temp = 1655 - j;
            t = Integer.toString(temp);
            return ("Hour 10 will finish in " + t + " Mins.");
        } else if (j > 1700 && j < 2200) {
            return ("How was your day?");
        } else {
            return ("Hi There!");
        }
    }
}