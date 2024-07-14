package Customer.utils;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Utils {

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(CUSTOM_FORMATTER);

    }


}
