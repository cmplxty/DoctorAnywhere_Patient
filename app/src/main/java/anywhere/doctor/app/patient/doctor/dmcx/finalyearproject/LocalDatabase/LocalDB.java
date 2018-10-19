package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;


public class LocalDB {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public LocalDB() {
        sharedPreferences = RefActivity.refACActivity.get().getSharedPreferences(LDBModel.DB_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String retriveString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void clearLocalDB() {
        editor.clear();
        editor.apply();
    }

}
