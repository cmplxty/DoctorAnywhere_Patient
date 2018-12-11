package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import android.widget.Toast;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Patient;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class NurseController {

    public static void LoadNurses(final IAction action) {
        Vars.appFirebase.loadNurses(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void SendRequestNurse(final String nurseId) {
        ProfileController.CheckForProfileData(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object != null) {

                    Patient patient = (Patient) object;
                    Vars.appFirebase.sendRequestNurse(nurseId, patient, new ICallback() {
                        @Override
                        public void onCallback(boolean isSuccessful, Object object) {
                            if (isSuccessful)
                                Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RequestSent, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ErrorRequestSentFailed, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
    }

    public static void CancelRequestNurse(final String nurseId) {
        Vars.appFirebase.removeRequestNurse(nurseId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful)
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RequestCanceled, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ErrorRequestSentFailed, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static void CheckRequesNurse(String nurseId, final IAction action) {
        Vars.appFirebase.checkRequestNurse(nurseId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(isSuccessful);
            }
        });
    }

}
