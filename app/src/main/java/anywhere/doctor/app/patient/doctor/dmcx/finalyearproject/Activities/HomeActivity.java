package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.lang.ref.WeakReference;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.HomeController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.ProfileController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.AppFragmentManager;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.FragmentNames;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home.AppointmentFragment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home.DashboardFragment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home.DoctorsFragment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home.ProfileFragment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home.ServiceFragment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LDBModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LocalDB;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.TitleText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;


public class HomeActivity extends AppCompatActivity {

    // Variables
    public static WeakReference<HomeActivity> instance;

    private Toolbar toolbar;
    private FrameLayout mainFrameLayout;
    private BottomNavigationViewEx mainBottomNavViewEx;
    // Variables

    // Methods
    /*
    * Initialize toolbar
    * */
    private void init_toolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /*
    * Initializa all the app views
    * */
    @SuppressLint("HardwareIds")
    private void init() {
        mainFrameLayout = findViewById(R.id.homeFragmentFrameLayout);

        mainBottomNavViewEx = findViewById(R.id.mainBottomNavViewEx);
        mainBottomNavViewEx.enableAnimation(false);
        mainBottomNavViewEx.enableShiftingMode(false);
        mainBottomNavViewEx.enableItemShiftingMode(false);
        mainBottomNavViewEx.setTextSize(Vars.BNavBarProps.bottomNavBarTextSize);

        Vars.localDB = new LocalDB();
        Vars.localDB.saveString(LDBModel.SAVE_DEVICE_ID, Secure.getString(RefActivity.refACActivity.get().getContentResolver(), Secure.ANDROID_ID));
        HomeController.UpdateTokenId();
    }

    /*
    * Properties of all the views
    * */
    private void events() {
        mainBottomNavViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.doctorsBMI: {
                        loadFragment(TitleText.Doctor, new DoctorsFragment(), FragmentNames.Doctor);
                        break;
                    }
                    case R.id.serviceBMI: {
                        loadFragment(TitleText.Service, new ServiceFragment(), FragmentNames.Service);
                        break;
                    }
                    case R.id.apptBMI: {
                        loadFragment(TitleText.Appointment, new AppointmentFragment(), FragmentNames.Appintment);
                        break;
                    }
                    case R.id.profileBMI: {
                        loadFragment(TitleText.Profile, new ProfileFragment(), FragmentNames.Profile);
                        break;
                    }
                    case R.id.dashboardBMI: {
                        loadFragment(TitleText.Dashboard, new DashboardFragment(), FragmentNames.Dashboard);
                        break;
                    }
                }

                return false;
            }
        });
    }

    /*
    * Load fragment and toolbar title
    * params: Title Name, Fragment, Fragment Tag
    * */
    private void loadFragment (String title, Fragment fragment, String tag) {
        AppFragmentManager.replace(HomeActivity.this, AppFragmentManager.CONTAINER_HOME, fragment, tag);
        toolbar.setTitle(title);
    }

    /*
    * Load profile data
    * */
    private void loadProfileData() {
        ProfileController.CheckForProfileData(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object instanceof String) {
                    String errorCode = (String) object;
                    Toast.makeText(RefActivity.refACActivity.get(), errorCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RefActivity.updateACActivity(this);

        instance = new WeakReference<>(this);

        init_toolbar();
        init();
        events();
        loadProfileData();

        // On Start - Dashboard
        loadFragment(TitleText.Dashboard, new DashboardFragment(), FragmentNames.Dashboard);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Vars.Flag.IsDashboardLoaded = true;

        // Show animation at the starting
//        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Vars.Flag.IsDashboardLoaded = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Null -> Firebase instance - LocalDb instance
    }

    @Override
    public void onBackPressed() {
        if (Vars.currentFragment.getTag() != null && Vars.currentFragment.getTag().equals(FragmentNames.Dashboard)) {
            super.onBackPressed();
        } else if(Vars.currentFragment.getTag() != null && Vars.currentFragment.getTag().equals(FragmentNames.ProfileEdit)) {
            loadFragment(TitleText.Profile, new ProfileFragment(), FragmentNames.Profile);
        } else {
            loadFragment(TitleText.Dashboard, new DashboardFragment(), FragmentNames.Dashboard);
        }
    }
}
