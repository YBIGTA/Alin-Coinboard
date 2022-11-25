package kimchipremium;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class KimchiPremiumUtil {
    public static String changeTimestampToDateString(Long timestamp){

        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss", Locale.KOREA);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return sdf.format(date);
    }
}

