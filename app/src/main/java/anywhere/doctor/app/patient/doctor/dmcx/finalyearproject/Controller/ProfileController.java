package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LDBModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Patient;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingText;
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
                        Patient patient = ((DataSnapshot) object).getValue(Patient.class);
                        Gson gson = new Gson();
                        String patientJson = gson.toJson(patient);

                        Vars.localDB.saveString(LDBModel.SAVE_PATIENT_PROFILE, patientJson);
                        action.onCompleteAction(patient);
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

}
