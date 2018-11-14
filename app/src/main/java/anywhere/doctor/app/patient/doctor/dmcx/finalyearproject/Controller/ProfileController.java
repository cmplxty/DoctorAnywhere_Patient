package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LDBModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Patient;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class ProfileController {

    public static void LoadProfile(final IAction action) {
        // Loading
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.getCurrentUserInfo(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                // Loding stop
                LoadingDialog.stop();

                if (object instanceof String) {
                    String errorCode = (String) object;
                    action.onCompleteAction(errorCode);
                } else if (object instanceof DataSnapshot) {
                    if (isSuccessful) {
                        DataSnapshot snapshot = ((DataSnapshot) object);
                        Patient patient = snapshot.getValue(Patient.class);
                        if (patient != null) {
                            patient.setId(snapshot.getKey());
                            Gson gson = new Gson();
                            String patientJson = gson.toJson(patient);

                            Vars.localDB.saveString(LDBModel.SAVE_PATIENT_PROFILE, patientJson);
                            action.onCompleteAction(patient);
                        }
                    }
                }
            }
        });
    }

    private static void LoadLocalProfile(IAction action) {
        String patientJson = Vars.localDB.retriveString(LDBModel.SAVE_PATIENT_PROFILE);
        Gson gson = new Gson();
        Patient patient = gson.fromJson(patientJson, Patient.class);

        action.onCompleteAction(patient);
    }

    public static Patient GetLocalProfile() {
        String patientJson = Vars.localDB.retriveString(LDBModel.SAVE_PATIENT_PROFILE);
        Gson gson = new Gson();
        return gson.fromJson(patientJson, Patient.class);
    }

    public static void CheckForProfileData(IAction action) {
        if (!Vars.localDB.retriveString(LDBModel.SAVE_PATIENT_PROFILE).equals("")) {
            LoadLocalProfile(action);
            return;
        }

        LoadProfile(action);
    }

    public static void UpdateProfileDetail(String name, String email, String age, String phone, String address, String dob, String country, String gender, String link, Uri profileImageUri) {
        final Map<String, Object> map = new HashMap<>();
        map.put(AFModel.name, name.equals("") ? AFModel.deflt : name);
        map.put(AFModel.email, email.equals("") ? AFModel.deflt : email);
        map.put(AFModel.link, link);
        map.put(AFModel.phone, phone.equals("") ? AFModel.deflt : phone);
        map.put(AFModel.address, address.equals("") ? AFModel.deflt : address);
        map.put(AFModel.country, country.equals("") ? AFModel.deflt : country);
        map.put(AFModel.gender, gender.equals("") ? AFModel.deflt : gender);
        map.put(AFModel.dob, dob.equals("") ? AFModel.deflt : dob);
        map.put(AFModel.age, age.equals("") ? AFModel.deflt : age);
        map.put(AFModel.type, AFModel._new);

        if (profileImageUri != null) {
            LoadingDialog.start(LoadingText.UploadingImage);

            Vars.appFirebase.uploadProfileImage(profileImageUri, new ICallback() {
                @Override
                public void onCallback(boolean isSuccessful, Object object) {
                    LoadingDialog.stop();

                    if (isSuccessful) {
                        String url = (String) object;
                        map.put(AFModel.link, url);
                    }

                    UpdateUserDetail(map);
                }
            });
        } else {
            UpdateUserDetail(map);
        }
    }

    private static void UpdateUserDetail(Map<String, Object> map) {
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.updatePatientProfile(map, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();

                if (isSuccessful) {
                    LoadProfile(new IAction() {
                        @Override
                        public void onCompleteAction(Object object) {
                            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.UpdateSuccessful, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
