package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.ICallback;
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

}
