package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Patient;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AudioCallController {

    public static void LoadAllAudioCallDoctors(final IAction action) {
        DoctorController.LoadDoctors(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object != null) {
                    List<Doctor> doctors = new ArrayList<>();
                    for (Object obj : (List<?>) object) {
                        doctors.add((Doctor) obj);
                    }

                    Vars.appFirebase.loadAllAudioCallDoctors(doctors, new ICallback() {
                        @Override
                        public void onCallback(boolean isSuccessful, Object object) {
                            action.onCompleteAction(object);
                        }
                    });
                }
            }
        });
    }

    public static void SendAudioCallNotification(String patientId, String doctorId, String patientName, final IAction action) {
        Vars.appFirebase.sendAudioCallNotification(patientId, doctorId, patientName, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(isSuccessful);
            }
        });
    }

    public static void SetCurrentDeviceId(final IAction action) {
        Vars.appFirebase.setAudioCallDeviceId(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(isSuccessful);
            }
        });
    }

}
