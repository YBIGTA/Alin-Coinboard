package kimchipremium;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KimchiPremiumUtil {
    public static String changeTimestampToDateString(String timestamp){
        Date date = new Date(Long.valueOf(timestamp));
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
        return sdf.format(date);
    }

    public static String getValue(String keyValue, boolean isQuoted){
        String value = keyValue.split(":")[1];
        if (isQuoted){
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

}
