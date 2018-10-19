package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import java.nio.file.DirectoryIteratorException;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorController {

    public static void LoadDoctors(final IAction action) {
        Vars.appFirebase.getDoctors(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    List<Doctor> doctors = (List<Doctor>) object;
                    if (doctors != null && doctors.size() > 0) {
                        action.onCompleteAction(doctors);
                    }
                }
            }
        });
    }

}
