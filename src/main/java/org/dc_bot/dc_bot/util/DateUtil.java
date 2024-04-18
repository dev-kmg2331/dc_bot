package org.dc_bot.dc_bot.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String convertDefault(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static String convertToKR(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }
}
