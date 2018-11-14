package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class HomeServiceController {

    public static void LoadAllHomeServiceDoctor(final IAction action) {
        Vars.appFirebase.loadHomeServiceDoctors(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ErrorNoAvailabeDoctors, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void CheckIsPatientRequestedYet(String doctorId, final IAction action) {
        Vars.appFirebase.checkHomeServiceRequest(doctorId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(isSuccessful);
            }
        });
    }

    public static void SendHomeServiceRequest(String did, String pid, String dname, String dlocation, String dphone, String dspeacialist, String dtime, String pname, String paddress, String pphone) {
        Map<String, Object> value = new HashMap<>();
        value.put(AFModel.hs_variables.doctor_name, dname);
        value.put(AFModel.hs_variables.doctor_location, dlocation);
        value.put(AFModel.hs_variables.doctor_phone, dphone);
        value.put(AFModel.hs_variables.doctor_specialist, dspeacialist);
        value.put(AFModel.hs_variables.doctor_time, dtime);
        value.put(AFModel.hs_variables.patient_address, paddress);
        value.put(AFModel.hs_variables.patient_phone, pphone);
        value.put(AFModel.hs_variables.patient_name, pname);
        value.put(AFModel.hs_variables.timestamp, String.valueOf(System.currentTimeMillis()));
        value.put(AFModel.notification_status, AFModel.not_viewed);

        Map<String, Object> map = new HashMap<>();
        map.put( did + "/" + pid, value);
        map.put(pid + "/" + did, value);

        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.sendHomeServiceRequest(map, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();

                if (!isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ErrorRequestSentFailed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RequestSent, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void CancelHomeServiceRequest(String doctorId) {
        Vars.appFirebase.cancelHomeServiceRequest(doctorId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RequestCanceled, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.RequestCancelFailed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void LoadHomeServiceRequests(final IAction action) {
        Vars.appFirebase.loadHomeService(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }
}
