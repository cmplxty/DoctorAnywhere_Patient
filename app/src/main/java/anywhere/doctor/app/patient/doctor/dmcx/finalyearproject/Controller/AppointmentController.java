package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AppointmentDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AppointmentController {

    public static void LoadAppointmentRequests(final IAction action) {
        Vars.appFirebase.loadPatientAppointments(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void SendAppointmentRequest(String name, String phone, String date, String time, String doctorId, AppointmentDoctor appointmentDoctor) {

        Map<String, Object> map = new HashMap<>();
        map.put(AFModel.ap_variables.patient_name, name);
        map.put(AFModel.ap_variables.patient_phone, phone);
        map.put(AFModel.ap_variables.date, date);
        map.put(AFModel.ap_variables.time, time);
        map.put(AFModel.ap_variables.status, AFModel.request);
        map.put(AFModel.ap_variables.doctor_name, appointmentDoctor.getName());
        map.put(AFModel.ap_variables.doctor_clinic, appointmentDoctor.getClinic());
        map.put(AFModel.ap_variables.timestamp, String.valueOf(System.currentTimeMillis()));
        map.put(AFModel.notification_status, AFModel.not_viewed);

        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put(doctorId + "/" + Vars.appFirebase.getCurrentUser().getUid(), map);
        mainMap.put(Vars.appFirebase.getCurrentUser().getUid() + "/" + doctorId, map);

        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.saveAppointmentRequest(mainMap, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();

                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AppointmentSent, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.AppontmentNotSent, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public static void LoadAllAppointmentDoctors(final IAction action) {
        Vars.appFirebase.loadAppointmentDoctors(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void CheckAppointmentIsSent(String doctorId, final IAction action) {
        Vars.appFirebase.checkAppointmentSent(doctorId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(isSuccessful);
            }
        });
    }

    public static void DeleteAppointmentRequest(String doctorId) {
        Vars.appFirebase.deletePatientAppointment(doctorId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AppointmentDeleted, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
