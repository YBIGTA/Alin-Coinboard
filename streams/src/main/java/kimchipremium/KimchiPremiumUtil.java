package kimchipremium;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class KimchiPremiumUtil {
    public static String changeTimestampToDateString(Long timestamp){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));

        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
        return sdf.format(date);
    }
}

