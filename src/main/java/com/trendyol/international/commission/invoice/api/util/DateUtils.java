package com.trendyol.international.commission.invoice.api.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class DateUtils {
    private static final String DATE_WITHOUT_TIME_WITHOUT_YEAR = "dd MMMM";
    private static final Locale LOCALE_EN = new Locale("en");
    private static final Integer LAST_HOUR = 23;
    private static final Integer LAST_MINUTE = 59;
    private static final Integer LAST_SECOND = 59;
    private static final Integer LAST_NANO_SECOND = 999000000;

    private static DateFormat getDateFormat(String dateFormat) {
        return new SimpleDateFormat(dateFormat, LOCALE_EN);
    }

    public static String getDateAsStringWithoutYear(Date date) {
        return getDateFormat(DATE_WITHOUT_TIME_WITHOUT_YEAR).format(date);
    }

    public static Integer getYearOfDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static LocalDateTime getLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date getLastDateOfMonth(LocalDateTime localDateTime, String zoneId) {
        return Date.from(LocalDateTime.of(
                localDateTime.getYear(),
                localDateTime.getMonth(),
                localDateTime.getDayOfMonth(),
                LAST_HOUR,
                LAST_MINUTE,
                LAST_SECOND,
                LAST_NANO_SECOND
        ).atZone(ZoneId.of(zoneId)).toInstant());
    }
}
