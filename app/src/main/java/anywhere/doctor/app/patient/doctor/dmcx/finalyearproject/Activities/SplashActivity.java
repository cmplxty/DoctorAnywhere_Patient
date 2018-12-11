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

    private SplashHandler splashHandlar = new SplashHandler();

    private static class SplashHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    }

    private class SplashRunnable implements Runnable {

        @Override
        public void run() {
            RefActivity.refACActivity.get().startActivity(new Intent(RefActivity.refACActivity.get(), AuthActivity.class));
            RefActivity.refACActivity.get().finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RefActivity.updateACActivity(this);

        splashHandlar.postDelayed(new SplashRunnable(), 1000);
    }

}
