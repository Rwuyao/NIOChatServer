package server.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    /** The date pattern that is used for conversion. Change as you wish. */
    private static final String DATE_PATTERN = "yy/MM/dd";

    /** The date formatter. */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static String format(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }


    public static LocalDateTime parse(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDateTime::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public  static long compareTimeWithNow(LocalDateTime date){
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(date,now);

        return duration.toSeconds();//相差的分钟数
    }

}
