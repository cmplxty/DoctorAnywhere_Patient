package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Seed;

import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;

public class Seeder {
    private static Seeder instance;

    private char[] seed = {'1', '2', '3', '4', '5'};

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    public static Seeder Instance() {
        if (instance == null)
            instance = new Seeder();

        return instance;
    }

    private FirebaseUser getCurrentUser() {
        if (mAuth == null)
            mAuth = FirebaseAuth.getInstance();

        return mAuth.getCurrentUser();
    }

    public Seeder create() {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        return instance;
    }

    public Seeder showToast() {
        Toast.makeText(RefActivity.refACActivity.get(), "Seeding...", Toast.LENGTH_SHORT).show();
        return instance;
    }

    public void seedAppointment() {
        if (mReference != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(AFModel.ap_variables.patient_name, "Mr. Kamal Raj");
            map.put(AFModel.ap_variables.patient_phone, "0192039122");
            map.put(AFModel.ap_variables.date, "24 NOV 2018");
            map.put(AFModel.ap_variables.time, "3 PM - 6 PM");
            map.put(AFModel.ap_variables.status, AFModel.accept);
            map.put(AFModel.ap_variables.doctor_name, "Doctor One");
            map.put(AFModel.ap_variables.doctor_clinic, "Some Chember");
            map.put(AFModel.ap_variables.timestamp, String.valueOf(System.currentTimeMillis()));
            map.put(AFModel.notification_status, AFModel.not_viewed);

            for (char value : seed) {
                mReference.child(AFModel.database)
                        .child(AFModel.appointment)
                        .child(getCurrentUser().getUid())
                        .child(String.valueOf(value))
                        .updateChildren(map);
            }
        }
    }

    public void seedMessageUser() {
        if (mReference != null) {
            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put(AFModel.content, "Some Message");
            userMessage.put(AFModel.timestamp, AFModel.text);
            userMessage.put(AFModel.doctor, "1");
            userMessage.put(AFModel.patient, "1");
            userMessage.put(AFModel.type, AFModel.prescription);

            final Map<String, Object> userMessageMap = new HashMap<>();
            userMessage.put(AFModel.notification_status, AFModel.viewed);

            for (char value : seed) {
                userMessageMap.put(getCurrentUser().getUid() + "/" + String.valueOf(value), userMessage);
                mReference.child(AFModel.database)
                        .child(AFModel.message_user)
                        .updateChildren(userMessageMap);
            }
        }
    }

    public void seedMessageUserRemove() {
        if (mReference != null) {
            for (char value : seed) {
                mReference.child(AFModel.database)
                        .child(AFModel.message_user)
                        .child(getCurrentUser().getUid())
                        .child(String.valueOf(value))
                        .removeValue();
            }
        }
    }
}
