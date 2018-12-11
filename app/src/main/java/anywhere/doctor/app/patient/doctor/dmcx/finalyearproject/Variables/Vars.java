package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables;

import android.support.v4.app.Fragment;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AppFirebase;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LocalDB;

public class Vars {

    public static final String TAG = "APP_LOG_TAG";
    public static String deviceId;

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
        public static final String MESSAGE_ACTIVITY_DATA            = "M A D";
        public static final String VIEW_IMAGE_DATA                  = "V I D";
        public static final String PERSCRIPTION_ACTIVITY_DATA       = "P A D";
        public static final String DOCTOR_PROFILE_ACTIVITY_DATA     = "D P A D";
        public static final String PROFILE_EDIT_FRAGMENT_DATA       = "P E F D";
        public static final String AUDIO_CALL_ACTIVITY_DATA         = "A C A D";
        public static final String BLOG_VIEWER_ACTIVITY_DATA        = "B V A D";
        public static final String MY_BLOOD_DONOR_ACTIVITY_DATA     = "M B D A D";
    }

    public static class ParentActivity {
        public static final String TRIG_BLOG_VIEWER_ACTIVITY = "TRIG BLOG VIEWER ACTIVITY";
        public static final String TRIG_AUDIO_CALL_ACTIVITY = "TRIG AUDIO CALL ACTIVITY";

        public static final String HOME_ACTIVITY = "HOME ACTIVITY";
        public static final String BLOG_ACTIVITY = "BLOG ACTIVITY";
        public static final String AUDIO_CALL_HISTORY = "AUDIO CALL HISTORY ACTIVITY";
        public static final String DOCTOR_LIST_AUDIO_CALL = "DOCTOR LIST AUDIO CALL ACTIVITY";
    }

    public static class ActivityOverrider {
        public static final String OPEN_MESSAGE_ACTIVITY = "OPEN MESSAGE ACTIVITY -- 0x1";

        public static final String FROM_HOME_ACTIVITY = "FROM HOME ACTIVITY -- 0x1";
        public static final String FROM_MESSAGE_USER_LIST_ACTIVITY = "FROM MESSAGE USER LIST ACTIVITY -- 0x2";
        public static final String FROM_DOCTOR_LIST_ACTIVITY = "FROM DOCTOR LIST ACTIVITY -- 0x3";
        public static final String FROM_AUDIO_CALL_HISTORY = "FROM AUDIO CALL HISTORY ACTIVITY -- 0x4";
    }

    public static class RequestCode {
        public static final int REQUEST_CALL_CODE_HS = 1111;
        public static final int REQUEST_CALL_CODE_AP = 1112;
        public static final int REQUEST_ACCESS_IMAGE_CODE_PEF = 1113;
        public static final int REQUEST_CALL_CODE_BD = 1114;
        public static final int REQUEST_CALL_CODE_BP = 1124;

        public static final int REQUEST_FOR_ONLINE_AUDIO_CALL = 1114;
    }

    public static class Flag {
        public static boolean IsDashboardLoaded = false;
    }

    public static class Sinch {
        public static final String APP_KEY = "76a8749a-b68e-4196-b382-84de8eb133fc";
        public static final String APP_SECRET = "eWBstQ1CoUCYdGQbjHaYwA==";
        public static final String ENVIRONMENT = "clientapi.sinch.com";
    }

    public static class Search {
        public static final String PAGE_HOME_DOCTOR = "Home Activity Doctor Fragment";
        public static final String PAGE_HOME_APPOINTMENT = "Home Activity Appointment Fragment";
        public static final String PAGE_NURSE = "Nurse Activity";
        public static final String PAGE_MESSAGE_USER_LIST = "Message User List Activity";
        public static final String PAGE_PRESCRIPTION_LIST = "Prescription List Activity";
        public static final String PAGE_HOME_SERVICE_LIST = "Home Service List Activity";
        public static final String PAGE_BLOG = "Blog Activity";
        public static final String PAGE_DOCTOR_LIST_MESSAGE = "Doctor List Message Activity";
        public static final String PAGE_DOCTOR_LIST_HOME_SERVICE = "Doctor List Home Service Activity";
        public static final String PAGE_DOCTOR_LIST_AUDIO_CALL = "Doctor List Audio Call Activity";
        public static final String PAGE_DOCTOR_LIST_APPOINTMENT = "Doctor List Appointment Activity";
        public static final String PAGE_BLOOD_DONOR_LIST = "Blood Donor List Activity";
        public static final String PAGE_BLOOD_POST_LIST = "Blood Post List Activity";
        public static final String PAGE_AUDIO_CALL_HISTORY_LIST = "Audio Call History Activity";
    }
}
