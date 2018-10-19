package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables;

import android.support.v4.app.Fragment;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AppFirebase;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LocalDB;

public class Vars {

    public static Fragment currentFragment;
    public static AppFirebase appFirebase;
    public static LocalDB localDB;

    public static class Animation {
        public static final int DEFAULT_ANIMATION_TIME = 500;
        public static final int ANIMATION_WAITING_TIME = 700;
    }

    public static class BNavBarProps {
        public static final int bottomNavBarTextSize = 10;
    }

    public static class Connector {
        public static final String MESSAGE_ACTIVITY_DATA = "M A D";
        public static final String VIEW_IMAGE_DATA = "V I D";
        public static final String PERSCRIPTION_ACTIVITY_DATA = "P A D";
    }

    public static class ActivityOverrider {
        public static final String OPEN_MESSAGE_ACTIVITY = "OPEN MESSAGE ACTIVITY -- 0x1";

        public static final String FROM_HOME_ACTIVITY = "FROM HOME ACTIVITY -- 0x1";
        public static final String FROM_MESSAGE_USER_LIST_ACTIVITY = "FROM MESSAGE USER LIST ACTIVITY -- 0x2";
        public static final String FROM_DOCTOR_LIST_ACTIVITY = "FROM DOCTOR LIST ACTIVITY -- 0x3";
    }
}
