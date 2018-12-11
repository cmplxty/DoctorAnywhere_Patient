package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DashboardController {

    public static void CountNewMessages(final IAction action) {
        Vars.appFirebase.countNewMessage(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else
                    action.onCompleteAction(null);
            }
        });
    }

    public static void CountHomeServices(final IAction action) {
        Vars.appFirebase.countHomeServices(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else
                    action.onCompleteAction(null);
            }
        });
    }

    public static void CountAcceptedAppointments(final IAction action) {
        Vars.appFirebase.countAcceptedAppointments(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else
                    action.onCompleteAction(null);
            }
        });
    }

}
