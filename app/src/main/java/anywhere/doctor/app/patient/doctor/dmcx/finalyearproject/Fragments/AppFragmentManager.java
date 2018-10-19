package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AppFragmentManager {

    public static int CONTAINER_HOME = R.id.homeFragmentFrameLayout;

    public static void replace (AppCompatActivity appCompatActivity, int container, Fragment fragment, String tag) {
        appCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(container, fragment, tag)
                .commit();

        Vars.currentFragment = fragment;
    }
}
