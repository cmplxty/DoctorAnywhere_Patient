package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorController {

    public static void LoadDoctors(final IAction action) {
        Vars.appFirebase.getDoctors(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void LoadDoctor(String id, final IAction action) {
        Vars.appFirebase.getDoctor(id, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }
}
