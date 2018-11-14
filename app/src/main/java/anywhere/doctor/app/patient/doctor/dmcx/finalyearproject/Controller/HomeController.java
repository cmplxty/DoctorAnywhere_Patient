package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LDBModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class HomeController {

    public static void UpdateTokenId() {
        Vars.appFirebase.setTokenId();
    }

}
