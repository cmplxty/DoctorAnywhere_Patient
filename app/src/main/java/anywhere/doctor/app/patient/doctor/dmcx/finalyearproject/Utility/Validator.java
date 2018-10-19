package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility;

import android.util.Patterns;

public class Validator {

    public static boolean empty(String value) {
        return value.equals("");
    }

    public static boolean validEmail(String value) {
        return !Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    public static boolean validPassword(String value) {
        return value.length() < 6;
    }

}
