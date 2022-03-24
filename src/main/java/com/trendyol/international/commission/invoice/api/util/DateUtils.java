package com.trendyol.international.commission.invoice.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

public final class DateUtils {

    private static final String DATE_WITHOUT_TIME_FORMAT_DOTS = "dd.MM.yyyy";
    private static final String DATE_AND_TIME_FORMAT_SLASH = "dd/MM/yyyy HH:mm";
    private static final String DATE_AND_TIME_FORMAT_WITH_SECOND_SLASH = "dd/MM/yyyy HH:mm:ss";
    private static final String DATE_AND_TIME_FORMAT_WITH_MILISECOND_SLASH = "dd/MM/yyyy HH:mm:ss.SSS";
    private static final String DATE_AND_TIME_FORMAT_WITH_SECOND_HYPHEN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_WITHOUT_TIME_FORMAT_BASIC = "dd MMMM yyyy";
    private static final String DATE_AND_TIME_FORMAT_DOTS = "dd.MM.yyyy HH:mm";
    private static final String DATE_WITHOUT_TIME_WITHOUT_YEAR = "dd MMMM";
    private static final String DATE_WITHOUT_TIME_ADJOINED = "ddMMYYYY";
    private static final String DATE_AND_TIME_FORMAT_WITHOUT_SECONDS = "yyyy-MM-dd'T'HH:mm";
    public static final Locale LOCALE_EN = new Locale("en");
    public static final String TURKISH_DATE_FORMAT = "dd/MM/yy HH:mm";
    public static final String INTERNATIONAL_DATE_FORMAT = "dd/MM/yy HH:mm";

    public static final String EMPTY_DATE_MESSAGE = "dateAsString cannot be null";

    private DateUtils() {

    }

    public static String toAdjoinedDate(Date date) {
        return formatDate(date, DATE_WITHOUT_TIME_ADJOINED);
    }

    public static String toString(Date date) {
        return formatDate(date, DATE_AND_TIME_FORMAT_SLASH);
    }

    public static String toStringWithDots(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return formatDate(date, DATE_AND_TIME_FORMAT_DOTS);
    }

    public static String toStringWithDotsWithoutTime(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return formatDate(date, DATE_WITHOUT_TIME_FORMAT_DOTS);
    }

    public static String toStringTurkish(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return getDateFormat(DateUtils.DATE_WITHOUT_TIME_FORMAT_BASIC).format(date);
    }



    public static String formatDate(Date date, String format) {
        if (Objects.isNull(date)) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    public static String dateAsStringWithoutYear(Date date) {
        return getDateFormat(DateUtils.DATE_WITHOUT_TIME_WITHOUT_YEAR).format(date);
    }

    private static DateFormat getDateFormat(String dateFormat) {
        return new SimpleDateFormat(dateFormat, LOCALE_EN);
    }

    public static boolean isValidFormat(String date, String dateFormat) {
        try {
            new SimpleDateFormat(dateFormat).parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int getLastDayOfMonth(int month, int year) {
        return YearMonth.of(year, month).lengthOfMonth();
    }


    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String convertToTurkishFormat(String dateInString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", LOCALE_EN);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy", LOCALE_EN);

        Date date = sdf2.parse(dateInString);
        String formattedDateString = sdf.format(date);
        return formattedDateString;
    }

    public static Date addHours (Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static String toTurkishFormatDate (Date date) {
        return new SimpleDateFormat(DATE_AND_TIME_FORMAT_DOTS).format(date);
    }

    public static Integer getYear(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }
}
