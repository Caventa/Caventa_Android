package caventa.ansheer.ndk.caventa.commons;

import java.text.SimpleDateFormat;

//TODO: Add to common class

public class Date_Utils {
    public static SimpleDateFormat mysql_date_time_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat normal_date_time_format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public static SimpleDateFormat normal_date_time_short_year_format = new SimpleDateFormat("dd-MM-yy HH:mm");
    public static SimpleDateFormat normal_date_format = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat normal_date_short_year_format = new SimpleDateFormat("dd-MM-yy");
}
