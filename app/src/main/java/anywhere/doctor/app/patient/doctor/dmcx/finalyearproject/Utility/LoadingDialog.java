package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility;

import android.app.AlertDialog;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import dmax.dialog.SpotsDialog;

public class LoadingDialog {

    private static AlertDialog spotDialog;

    public static void start(String message) {
        spotDialog = new SpotsDialog(RefActivity.refACActivity.get(), message, R.style.SpotDialogTheme);
        spotDialog.show();
    }

    public static void stop() {
        if (spotDialog != null)
            spotDialog.dismiss();
    }

}
