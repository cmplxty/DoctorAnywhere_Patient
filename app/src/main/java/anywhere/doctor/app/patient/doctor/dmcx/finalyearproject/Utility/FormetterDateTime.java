package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormetterDateTime {

    public static String getDate(String timestamp) {
        DateFormat simpleDateFormat = SimpleDateFormat.getDateInstance();
        return simpleDateFormat.format(new Date(Long.valueOf(timestamp)));
    }

}
