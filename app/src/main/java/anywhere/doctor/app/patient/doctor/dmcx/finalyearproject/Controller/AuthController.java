package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import android.widget.Toast;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.Validator;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AuthController {

    public static void SignUp(final String name, final String email, final String password, final IAction action) {
        if (Validator.empty(name)){
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.EmptyName, Toast.LENGTH_SHORT).show();
            return;
        } else if (Validator.empty(email)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.EmptyEmail, Toast.LENGTH_SHORT).show();
            return;
        } else if (Validator.validEmail(email)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.ValidEmail, Toast.LENGTH_SHORT).show();
            return;
        } else if (Validator.empty(password)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.EmptyPassword, Toast.LENGTH_SHORT).show();
            return;
        } else if (Validator.validPassword(password)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.ValidPassword, Toast.LENGTH_SHORT).show();
            return;
        }

        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.signUp(name, email, password, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();

                if (isSuccessful) {
                    action.onCompleteAction(true);
                } else {
                    if (object instanceof String) {
                        Toast.makeText(RefActivity.refACActivity.get(), String.valueOf(object), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AuthenticationFailed, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static void SignIn(final String email, final String password, final IAction action) {
        if (Validator.empty(email)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.EmptyEmail, Toast.LENGTH_SHORT).show();
            return;
        } else if (Validator.validEmail(email)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.ValidEmail, Toast.LENGTH_SHORT).show();
            return;
        } else if (Validator.empty(password)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.EmptyPassword, Toast.LENGTH_SHORT).show();
            return;
        }

        // Loading
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.getUserByEmail(email, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Vars.appFirebase.signIn(email, password, new ICallback() {
                        @Override
                        public void onCallback(boolean isSuccessful, Object object) {
                            // Loading Stop
                            LoadingDialog.stop();

                            if (!isSuccessful) {
                                String errorCode = (String) object;
                                action.onCompleteAction(errorCode);
                            } else {
                                action.onCompleteAction(true);
                            }
                        }
                    });
                } else {
                    // Loading Stop
                    LoadingDialog.stop();

                    action.onCompleteAction(ValidationText.UserNotExists);
                }
            }
        });
    }

    public static void SignOut() {
        Vars.appFirebase.SignOut();
    }
}
