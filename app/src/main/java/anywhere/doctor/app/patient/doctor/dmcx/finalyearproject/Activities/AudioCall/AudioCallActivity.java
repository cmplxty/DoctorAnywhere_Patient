package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.AudioCall;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import java.util.EventListener;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;
import de.hdodenhof.circleimageview.CircleImageView;

public class AudioCallActivity extends AppCompatActivity {

    // Variables
    private CircleImageView userProfilePicACCIV;
    private TextView userNameACTV;
    private TextView callStateACTV;
    private FloatingActionButton declineCallFab;

    private Call call;
    private SinchClient sinchClient;
    private Doctor doctor;
    private String patientId;
    private CallHandler callHandler;
    // Variables

    // Class
    private static class CallHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    }

    private class CallRunnable implements Runnable {
        @Override
        public void run() {
            callDoctor();
        }
    }
    // Class

    // Methods
    private void init() {
        userProfilePicACCIV = findViewById(R.id.userProfilePicACCIV);
        userNameACTV = findViewById(R.id.userNameACTV);
        callStateACTV = findViewById(R.id.callStateACTV);
        declineCallFab = findViewById(R.id.declineCallFab);

        doctor = getIntent().getParcelableExtra(Vars.Connector.AUDIO_CALL_ACTIVITY_DATA);
        patientId = Vars.appFirebase.getCurrentUser().getUid();
        callHandler = new CallHandler();
        if (doctor != null) {
            Picasso.with(RefActivity.refACActivity.get()).load(doctor.getImage_link()).placeholder(R.drawable.noperson).into(userProfilePicACCIV);
            userNameACTV.setText(doctor.getName());
            callStateACTV.setText("Connecting...");
        } else {
            finish();
        }
    }

    private void setupSinchClient() {
        sinchClient = Sinch.getSinchClientBuilder()
                .context(RefActivity.refACActivity.get())
                .userId(patientId)
                .applicationKey(Vars.Sinch.APP_KEY)
                .applicationSecret(Vars.Sinch.APP_SECRET)
                .environmentHost(Vars.Sinch.ENVIRONMENT)
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        callHandler.postDelayed(new CallRunnable(), 500);
    }

    private void callDoctor() {
        if (sinchClient.isStarted()) {
            if (call == null) {
                call = sinchClient.getCallClient().callUser(doctor.getId());
                call.addCallListener(new SinchCallListener());
            }
        }
    }

    private void event() {
        declineCallFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectCall();
            }
        });
    }

    private void rejectCall() {
        if (call != null) {
            call.hangup();
        }
        RefActivity.updateACActivity(HomeActivity.instance.get());
        finish();
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RefActivity.updateACActivity(this);
        setContentView(R.layout.activity_audio_call);
        init();
        setupSinchClient();
        event();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(RefActivity.refACActivity.get(), "Can't go back at this stage.", Toast.LENGTH_SHORT).show();
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallProgressing(Call call) {
            callStateACTV.setText("Ringing...");
        }

        @Override
        public void onCallEstablished(Call call) {
            callStateACTV.setText("Connected...");
        }

        @Override
        public void onCallEnded(Call call) {
            rejectCall();
            setVolumeControlStream(android.media.AudioManager.USE_DEFAULT_STREAM_TYPE);
            finish();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call call) {

        }
    }

}
