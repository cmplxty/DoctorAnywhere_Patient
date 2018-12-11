package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import android.widget.Toast;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodDonor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodPost;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class BloodDonorController {

    public static void LoadAllBloodDonor(final IAction action) {
        Vars.appFirebase.loadBloodDonors(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void LoadAllBloodPosts(final IAction action) {
        Vars.appFirebase.loadBloodPosts(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void LoadMyBloodPosts(final IAction action) {
        Vars.appFirebase.loadMyBloodPosts(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void CheckMyBloodPosts(final IAction action) {
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.checkMyBloodPosts(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();
                action.onCompleteAction(isSuccessful);

                if (!isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.YouHaveNoPosts, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void NewBloodPost(BloodPost post) {
        Vars.appFirebase.newBloodPost(post, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.Posted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.FailedToPost, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void SaveBloodDonor(BloodDonor bloodDonor) {
        Vars.appFirebase.saveBloodDonor(bloodDonor, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful)
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.DataSaved, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.FailedToCreateAccount, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void CheckMeAsBloodDonor(final IAction action) {
        Vars.appFirebase.checkMeAsBloodDonor(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(isSuccessful);
            }
        });
    }

    public static void MyBloodDonorData(final IAction action) {
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.myBloodDonorData(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();
                action.onCompleteAction(object);
            }
        });
    }

    public static void FindUserByBloodGroup(String bloodGroup, final IAction action) {
        Vars.appFirebase.findUserByBloodGroup(bloodGroup, new ICallback() {
           @Override
           public void onCallback(boolean isSuccessful, Object object) {
               action.onCompleteAction(object);
           }
        });
    }

    public static void DeleteCurrentDonorData() {
        Vars.appFirebase.deleteCurrentDonorData(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.Deleted, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void DeleteCurrentPost(String postId) {
        Vars.appFirebase.deleteCurrentPost(postId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.Deleted, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
