package ae.gov.sdg.paperless.platform.util;

import static ae.gov.sdg.paperless.platform.common.PlatformConstants.UAE_LOCALE;
import static java.lang.Math.abs;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Calendar.DATE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.time.DateUtils.isSameDay;
import static org.apache.commons.lang3.time.DateUtils.toCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.lang.NonNull;

public final class DateUtil {

    public static final String TIME_FORMAT_24_HRS = "HH:mm";
    public static final String TIME_FORMAT_12_HRS_WITH_AMPM = "hh:mm a";

    /**
     * private constructor because of static methods
     */
    private DateUtil() {
    }

    /**
     * Format time with AM and PM
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static String formatTimeWithAmPm(final String time) throws ParseException {
        if (isEmpty(time)) {
            return EMPTY;
        } else {
            final DateFormat sdf = new SimpleDateFormat(TIME_FORMAT_24_HRS);
            return new SimpleDateFormat(TIME_FORMAT_12_HRS_WITH_AMPM).format(sdf.parse(time));
        }
    }

    /**
     * Format Date String to the given format
     *
     * @param dateString
     * @param fromFormat
     * @param toFormat
     * @return
     * @throws ParseException
     */
    public static String transformToFormat(final String dateString, final String fromFormat, final String toFormat)
            throws ParseException {
        if (isBlank(dateString)) {
            return EMPTY;
        }
        final DateFormat format1 = new SimpleDateFormat(fromFormat);
        final DateFormat format2 = new SimpleDateFormat(toFormat);
        final Date date = format1.parse(dateString);
        return format2.format(date);
    }

    /**
     * Convert Date in String of inputPattern to Date in String of outputPattern
     *
     * @param dateString date in string of fromFormat pattern
     * @param fromFormat pattern of input date string
     * @param toFormat   pattern of string date in output
     * @return date in string of toFormat pattern
     */
    public static String formatDateWithFormats(final String dateString, final String fromFormat, final String toFormat)
            throws ParseException {
        if (dateString != null && !dateString.isEmpty()) {
            SimpleDateFormat format1 = new SimpleDateFormat(fromFormat);
            SimpleDateFormat format2 = new SimpleDateFormat(toFormat);
            Date date = format1.parse(dateString);
            return format2.format(date);
        }
        return EMPTY;
    }

    /**
     * @param dateString    date in string to be parsed
     * @param inputPatterns input patterns with which date string parsed
     * @return parsed Date
     * @throws ParseException           when none of the given input patterns able
     *                                  to parse the given date string
     * @throws IllegalArgumentException if the date string or pattern array is null
     */
    public static Date parse(final @NonNull String dateString, final @NonNull String... inputPatterns)
            throws ParseException {
        return parse(dateString, null, inputPatterns);
    }

    /**
     * @param dateString    date in string to be parsed
     * @param locale        Locale in which date to be parsed
     * @param inputPatterns input patterns with which date string parsed
     * @return parsed Date
     * @throws ParseException           when none of the given input patterns able
     *                                  to parse the given date string
     * @throws IllegalArgumentException if the date string or pattern array is null
     */
    public static Date parse(final @NonNull String dateString, final Locale locale,
            final @NonNull String... inputPatterns) throws ParseException {
        return DateUtils.parseDate(dateString, locale, inputPatterns);
    }

    public static String format(final @NonNull Date date, final @NonNull String pattern) {
        return format(date, pattern, null);
    }

    public static String formatToArabic(final @NonNull Date date, final @NonNull String pattern) {
        return format(date, pattern, UAE_LOCALE);
    }

    public static String format(final @NonNull Date date, final @NonNull String pattern, final Locale locale) {
        return DateFormatUtils.format(date, pattern, locale);
    }

    public static Date addMonths(final Date dateToAdd, final int noOfMonths) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateToAdd);
        c.add(Calendar.MONTH, noOfMonths);
        return c.getTime();
    }

    public static Date addDays(final Date dateToAdd, final int noOfDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateToAdd);
        c.add(DATE, noOfDays);
        return c.getTime();
    }

    /**
     * Get number of days as list except friday.
     *
     * @param noOfDays
     * @return
     */
    public static List<String> getNextNoOfDaysListExceptFriday(final int noOfDays) {
        return getNextNoOfDaysListExceptFriday(noOfDays, "MM/dd/yyyy");
    }

    public static List<String> getNextNoOfDaysListExceptFriday(final int noOfDays, final String outputDatePattern) {
        final List<String> datesInRange = new ArrayList<>();
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(Calendar.getInstance().getTime());

        final Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(addDays(Calendar.getInstance().getTime(), noOfDays - 1));
        while (calendar.before(endCalendar)) {
            final Date result = calendar.getTime();
            datesInRange.add(new SimpleDateFormat(outputDatePattern).format(result));
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }

    public static long getTimeDifferenceInMinutes(final Date firstDate, final Date secondDate) {
        return abs(MILLISECONDS.toMinutes(firstDate.getTime() - secondDate.getTime()));
    }

    /**
     * Date with 1st day of the Month of given date For ex: date is 2020-04-24,
     * method will return 2020-04-01
     *
     * @param date input date
     * @return date with 1 as day
     */
    public static Date getFirstDate(final Date date) {
        Calendar calendar = toCalendar(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * This method extracts time from given date
     *
     * @param date date in string and pattern should be yyyy-MM-dd'T'HH:mm:ssXXX"
     *             e.g. 2020-05-12T17:10:00+04:00
     * @return time in given date
     * @throws ParseException while parsing date
     */
    public static String extractTime(final String date) throws ParseException {
        return extractTime(date, "yyyy-MM-dd'T'HH:mm:ssXXX");
    }

    /**
     * This method extracts time from given date
     *
     * @param date             date in string
     * @param inputDatePattern pattern of the given date
     * @return time in given date
     * @throws ParseException while parsing date
     */
    public static String extractTime(final String date, final String inputDatePattern) throws ParseException {
        return formatDateWithFormats(date, inputDatePattern, TIME_FORMAT_24_HRS);
    }

    public static String extractTime(final Date date) {
        return DateFormatUtils.format(date, TIME_FORMAT_24_HRS);
    }

    public static String extractTimeToArabic(final Date date) {
        return extractTime(date, UAE_LOCALE);
    }

    public static String extractTime(final Date date, final Locale locale) {
        return DateFormatUtils.format(date, TIME_FORMAT_24_HRS, locale);
    }

    /**
     * @param date input date to check whether the given date matches today's date
     * @return true if given date is today
     */
    public static boolean isToday(final Date date) {
        return isSameDay(new Date(), date);
    }

    public static boolean isToday(final String date, final String inputPattern) throws ParseException {
        return isToday(parse(date, inputPattern));
    }

    public static Date fromLocalDateTime(final LocalDateTime localDateTime) {
        return fromLocalDateTime(localDateTime, ZoneId.systemDefault());
    }

    public static Date fromLocalDateTime(final LocalDateTime localDateTime, final ZoneId zoneId) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    public static LocalDateTime toLocalDateTime(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(final String date, final String pattern) {
        return LocalDateTime.parse(date, ofPattern(pattern));
    }

    public static LocalDate toLocalDate(final Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    public static Date fromLocalDate(final LocalDate date) {
        return java.sql.Date.valueOf(date);
    }

    public static Date fromUTCtoLocal(final Date utcDate) {
        return DateUtils.addHours(utcDate, 4);
    }

    public static Date fromLocalToUTC(final Date localDate) {
        return DateUtils.addHours(localDate, -4);
    }

    public static String toArabic(final Date date) {
        return toArabic(date, "dd MMMM yyyy");
    }

    public static String toArabic(final LocalDate date) {
        return toArabic(fromLocalDate(date), "dd MMMM yyyy");
    }

    public static String toArabic(final LocalDate date, final String pattern) {
        return toArabic(fromLocalDate(date), pattern);
    }

    public static String toArabic(final Date date, final String pattern) {
        return DateFormatUtils.format(date, pattern, UAE_LOCALE);
    }

    /**
     * Calculate Days between Today and Given date
     * 
     * @param givenDate
     * @return
     */
    public static int daysBetweenTodayAndGivenDate(Date givenDate) {
        final Date todaysDate = new Date();
        if (givenDate == null) {
            givenDate = new Date();
        }
        long difference = givenDate.getTime() - todaysDate.getTime();
        return (int) (difference / (1000 * 60 * 60 * 24));
    }

}