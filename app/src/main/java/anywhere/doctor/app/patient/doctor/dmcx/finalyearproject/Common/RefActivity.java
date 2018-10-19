package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common;

import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class RefActivity {

    public static WeakReference<AppCompatActivity> refACActivity;

    public static void updateACActivity(AppCompatActivity activity) {
        refACActivity = new WeakReference<>(activity);
    }
    
}
