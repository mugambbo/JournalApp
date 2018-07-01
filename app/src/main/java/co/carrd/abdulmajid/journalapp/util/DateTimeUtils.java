package co.carrd.abdulmajid.journalapp.util;

import android.text.format.DateUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static android.text.format.DateUtils.FORMAT_NUMERIC_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_YEAR;
import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

/**
 * Created by Abdulmajid on 30/06/2018.
 */
public class DateTimeUtils {

    public static final int ONE_DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
    public static String SERVER_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static String SERVER_DATE_PATTERN = "yyyy-MM-dd";

    public static String todaysDate () {
        return new SimpleDateFormat(SERVER_DATE_TIME_PATTERN, Locale.getDefault()).format(new Date());
    }

    /**
     * Get relative time for date
     *
     * @param date date to find its relative time
     * @return relative time
     */
    public static CharSequence getRelativeTime(final Date date) {
        long now = System.currentTimeMillis();
        if (Math.abs(now - date.getTime()) > 60000)
            return DateUtils.getRelativeTimeSpanString(date.getTime(), now,
                    MINUTE_IN_MILLIS, FORMAT_SHOW_DATE | FORMAT_SHOW_YEAR
                            | FORMAT_NUMERIC_DATE);
        else
            return "now";
    }
}