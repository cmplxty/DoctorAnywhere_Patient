package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import android.widget.Toast;

import com.google.firebase.database.ThrowOnExtraProperties;
import com.victor.loading.rotate.RotateLoading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Prescription;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class PrescriptionController {

    public static void LoadSinglePrescription(String timestamp, final IAction action) {
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.loadSpecificPrescription(timestamp, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();

                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else {
                    if (object instanceof String)
                        Toast.makeText(RefActivity.refACActivity.get(), String.valueOf(object), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void LoadAllPrescriptions(final IAction action) {
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.loadPrescriptions(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();

                if (object instanceof String) {
                    Toast.makeText(RefActivity.refACActivity.get(), String.valueOf(object), Toast.LENGTH_SHORT).show();
                } else {
                    if (object != null) {
                        action.onCompleteAction(object);
                    } else {
                        Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ErrorNoDatabaseFound, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
