package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RefActivity.updateACActivity(this);

        new splashHandler().postDelayed(new splashRunnable(), 1000);
    }

    private static class splashHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private static class splashRunnable implements Runnable {

        @Override
        public void run() {
            RefActivity.refACActivity.get().startActivity(new Intent(RefActivity.refACActivity.get(), AuthActivity.class));
            RefActivity.refACActivity.get().finish();
        }
    }

}
