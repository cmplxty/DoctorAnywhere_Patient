package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
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

    public static void SetCurrentDeviceId(final IAction action) {
        Vars.appFirebase.setAudioCallDeviceId(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(isSuccessful);
            }
        });
    }

    public static void LoadAllAudioCallHistory(final IAction action) {
        Vars.appFirebase.loadAllDoctors(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    List<Doctor> doctors = new ArrayList<>();
                    for (Object doctor : (List<?>) object) {
                        if (doctor != null) {
                            doctors.add((Doctor) doctor);
                        }
                    }

                    if (doctors.size() > 0) {
                        Vars.appFirebase.audioCallHistories(doctors, new ICallback() {
                            @Override
                            public void onCallback(boolean isSuccessful, Object object) {
                                action.onCompleteAction(object);
                            }
                        });
                    } else {
                        action.onCompleteAction(null);
                    }
                } else {
                    action.onCompleteAction(null);
                }
            }
        });
    }

    public static void DeleteCurrentHistory(String historyId) {
        Vars.appFirebase.deleteCurrentHistory(historyId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful)
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.HistoryDeleted, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.FailedToDelete, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
