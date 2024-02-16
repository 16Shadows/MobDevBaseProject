package org.hse.android.baseproject;

import android.icu.text.DateFormatSymbols;
import android.icu.text.SimpleDateFormat;

import java.util.Arrays;

//Test

public class SimpleDateFormatEx {
    public static void capitalizeWeekdays(SimpleDateFormat formatter) {
        DateFormatSymbols symbols = formatter.getDateFormatSymbols();
        String[] weekdays = symbols.getWeekdays();
        for (int i = 0; i < weekdays.length; i++)
            weekdays[i] = weekdays[i].length() > 2 ?
                            Character.toUpperCase(weekdays[i].charAt(0)) + weekdays[i].substring(1) :
                            weekdays[i].toUpperCase();
        symbols.setWeekdays(weekdays);
        formatter.setDateFormatSymbols(symbols);
    }
}