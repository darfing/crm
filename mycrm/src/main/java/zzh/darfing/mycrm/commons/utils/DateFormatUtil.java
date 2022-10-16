package zzh.darfing.mycrm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    public static String formatDateAndTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}
