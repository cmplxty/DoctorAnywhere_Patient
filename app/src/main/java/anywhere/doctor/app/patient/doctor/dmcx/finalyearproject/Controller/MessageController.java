package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import android.net.Uri;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class MessageController {

    private static void showToast(String content) {
        Toast.makeText(RefActivity.refACActivity.get(), content, Toast.LENGTH_SHORT).show();
    }

    public static void sendMessageText(String message, String dToUserId) {

        String pFromUserId = Vars.appFirebase.getCurrentUser().getUid();

        Map<String, String> map = new HashMap<>();
        map.put(AFModel.content, message);
        map.put(AFModel.type, AFModel.text);
        map.put(AFModel.from, pFromUserId);
        map.put(AFModel.to, dToUserId);
        map.put(AFModel.timestamp, String.valueOf(System.currentTimeMillis()));

        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put(pFromUserId + "/" + Vars.appFirebase.getPushId(), map);
        mainMap.put(dToUserId + "/" + Vars.appFirebase.getPushId(), map);

        // Hold the last message - Update for one user
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put(AFModel.content, message);
        userMessage.put(AFModel.type, AFModel.text);
        userMessage.put(AFModel.timestamp, AFModel.text);
        userMessage.put(AFModel.doctor, dToUserId);
        userMessage.put(AFModel.patient, pFromUserId);

        Map<String, Object> userMessageMap = new HashMap<>();
        userMessage.put(AFModel.notification_status, AFModel.viewed);
        userMessageMap.put(pFromUserId + "/" + dToUserId, userMessage);
        userMessage.put(AFModel.notification_status, AFModel.not_viewed);
        userMessageMap.put(dToUserId + "/" + pFromUserId, userMessage);

        Vars.appFirebase.saveMessage(mainMap, userMessageMap, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (object instanceof String) {
                    showToast(String.valueOf(object));
                } else {
                    showToast(ErrorText.ErrorUnknownReturnType);
                }
            }
        });
    }

    public static void sendMessageImage(Uri imageUri, String dToUserId) {

        Toast.makeText(RefActivity.refACActivity.get(), "Image Uploading... Will take some time to start.", Toast.LENGTH_SHORT).show();

        String pFromUserId = Vars.appFirebase.getCurrentUser().getUid();

        Map<String, Object> map = new HashMap<>();
        map.put(AFModel.content, AFModel.deflt);
        map.put(AFModel.type, AFModel.image);
        map.put(AFModel.from, pFromUserId);
        map.put(AFModel.to, dToUserId);
        map.put(AFModel.timestamp, String.valueOf(System.currentTimeMillis()));

        // Hold the last message - Update for one user
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put(AFModel.content, AFModel.deflt);
        userMessage.put(AFModel.type, AFModel.image);
        userMessage.put(AFModel.timestamp, AFModel.text);
        userMessage.put(AFModel.doctor, dToUserId);
        userMessage.put(AFModel.patient, pFromUserId);
        userMessage.put(AFModel.notification_status, AFModel.not_viewed);

        Vars.appFirebase.saveImageMessage(imageUri, map, userMessage, pFromUserId, dToUserId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (object instanceof String) {
                    String message = (String) object;
                    Toast.makeText(RefActivity.refACActivity.get(), message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ErrorUnknownReturnType, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void showMessages(String toUserId, final IAction action) {
        Vars.appFirebase.showMessages(toUserId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else {
                    action.onCompleteAction(ValidationText.NoMessagesFound);
                }
            }
        });
    }

    public static void LoadMessgeUsersList(final IAction action) {
        Vars.appFirebase.loadMessageUsers(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    if (object != null) {
                        action.onCompleteAction(object);
                    } else {
                        action.onCompleteAction(ErrorText.NoUserFound);
                    }
                } else {
                    action.onCompleteAction(ErrorText.NoUserFound);
                }
            }
        });
    }

    public static void LoadMessageUserContent(String fromId, final IAction action) {
        Vars.appFirebase.loadMessageUserDetail(fromId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Doctor doctor = (Doctor) object;
                    action.onCompleteAction(doctor);
                } else {
                    action.onCompleteAction(ErrorText.ErrorUserNotExists);
                }
            }
        });
    }

    public static void UpdateMessageNotViewedToViewedMessage() {
        Vars.appFirebase.updateMessageNotViewedToViewed();
    }
}
