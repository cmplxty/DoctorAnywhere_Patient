package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Service;

import android.annotation.SuppressLint;
import android.provider.Settings;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingServiceFirebase extends FirebaseMessagingService {

    @Override
    @SuppressLint("HardwareIds")
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String icon = remoteMessage.getNotification().getIcon();
        String doctorId = remoteMessage.getData().get("doctorId");
        String patientId = remoteMessage.getData().get("patientId");
        String type = remoteMessage.getData().get("type");
        String timestamp = remoteMessage.getData().get("timestamp");
        String deviceId = remoteMessage.getData().get("deviceId");
        String currentDeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


    }
}
