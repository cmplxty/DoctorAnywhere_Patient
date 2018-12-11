package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger.MessageActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LDBModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LocalDB;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Message;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class MessagingServiceFirebase extends FirebaseMessagingService {

    @Override
    @SuppressLint("HardwareIds")
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        LocalDB localDB = new LocalDB();

        if (remoteMessage != null && remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String icon = remoteMessage.getNotification().getIcon();
            String click_action = remoteMessage.getNotification().getClickAction();
            String doctorId = remoteMessage.getData().get("doctorId");
            String patientId = remoteMessage.getData().get("patientId");
            String type = remoteMessage.getData().get("type");
            String deviceId = remoteMessage.getData().get("deviceId");
            String currentDeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String activityState = localDB.retriveString(LDBModel.SAVE_ACTIVITY_STATE);

            if (activityState.equals(LDBModel.TOKEN_ACTIVITY_STATE_CLOSED)) {
                if (deviceId.equals(currentDeviceId)) {
                    if (type.equals(AFModel.message)) {
                        if (mAuth.getCurrentUser() != null) {
                            if (patientId.equals(mAuth.getCurrentUser().getUid())) {
                                message(doctorId, title, body);
                            }
                        }
                    }
                }
            }
        } else {
            Log.d("ASDJLKASJDL", "message: " + "Noti Null");
        }
    }

    private void message(String doctorId, String title, String body) {
        int notificationId = (int) System.currentTimeMillis();

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra(Vars.ActivityOverrider.OPEN_MESSAGE_ACTIVITY, Vars.ParentActivity.HOME_ACTIVITY);
        intent.putExtra(Vars.Connector.MESSAGE_ACTIVITY_DATA, doctor);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder = new NotificationCompat
                .Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("Default", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            if (nManager != null)
                nManager.createNotificationChannel(notificationChannel);
        }

        if (nManager != null)
            nManager.notify(notificationId, nBuilder.build());
    }
}
