package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.lang.ref.WeakReference;
import java.util.Objects;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AuthController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.HomeController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AppFirebase;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.ProfileController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.AppFragmentManager;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.FragmentNames;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home.AppointmentFragment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home.DashboardFragment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home.DoctorsFragment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home.ProfileFragment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home.ServiceFragment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Seed.ISeeder;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LDBModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LocalDB;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Seed.SeedController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.TitleText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;


public class HomeActivity extends AppCompatActivity implements ISeeder {

    // Variables
    public static WeakReference<HomeActivity> instance;

    private Toolbar toolbar;
    private MaterialSearchView materialSearchView;
    private BottomNavigationViewEx mainBottomNavViewEx;
    // Variables

    // Methods
    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @SuppressLint("HardwareIds")
    private void init() {
        materialSearchView = findViewById(R.id.materialSearchView);

        mainBottomNavViewEx = findViewById(R.id.mainBottomNavViewEx);
        mainBottomNavViewEx.enableAnimation(false);
        mainBottomNavViewEx.enableShiftingMode(false);
        mainBottomNavViewEx.enableItemShiftingMode(false);
        mainBottomNavViewEx.setTextSize(Vars.BNavBarProps.bottomNavBarTextSize);

        Vars.appFirebase = AppFirebase.Instance();
        Vars.localDB = new LocalDB();
        Vars.localDB.saveString(LDBModel.SAVE_DEVICE_ID, Secure.getString(RefActivity.refACActivity.get().getContentResolver(), Secure.ANDROID_ID));
        HomeController.UpdateTokenId();
    }

    private void events() {
        mainBottomNavViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (materialSearchView.isSearchOpen())
                    materialSearchView.closeSearch();

                switch (item.getItemId()) {
                    case R.id.doctorsBMI: {
                        DoctorsFragment doctorsFragment = new DoctorsFragment();
                        searchSetup(Vars.Search.PAGE_HOME_DOCTOR, doctorsFragment.getiSearch());

                        loadFragment(TitleText.Doctor, doctorsFragment, FragmentNames.Doctor);
                        break;
                    }
                    case R.id.serviceBMI: {
                        loadFragment(TitleText.Service, new ServiceFragment(), FragmentNames.Service);
                        break;
                    }
                    case R.id.apptBMI: {
                        AppointmentFragment appointmentFragment = new AppointmentFragment();
                        searchSetup(Vars.Search.PAGE_HOME_APPOINTMENT, appointmentFragment.getiSearch());

                        loadFragment(TitleText.Appointment, appointmentFragment, FragmentNames.Appintment);
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

    private void loadFragment (String title, Fragment fragment, String tag) {
        AppFragmentManager.replace(HomeActivity.this, AppFragmentManager.CONTAINER_HOME, fragment, tag);
        toolbar.setTitle(title);
    }

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

    private void setupSearchView() {
        materialSearchView.setOnSearchViewListener(SearchController.getInstance());
        materialSearchView.setOnQueryTextListener(SearchController.getInstance());
    }

    private boolean isSearchEnable() {
        return Vars.currentFragment != null && Vars.currentFragment.getTag() != null &&
                (
                    Objects.equals(Vars.currentFragment.getTag(), FragmentNames.Doctor) ||
                    Objects.equals(Vars.currentFragment.getTag(), FragmentNames.Appintment)
                );

    }

    private void searchSetup(String page, ISearch iSearch) {
        SearchController.setPage(page);
        SearchController.setiSearch(iSearch);
    }
    // Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RefActivity.updateACActivity(this);
        instance = new WeakReference<>(this);

        setupToolbar();
        init();
        events();
        loadProfileData();
        setupSearchView();

        // On Start - Dashboard
        loadFragment(TitleText.Dashboard, new DashboardFragment(), FragmentNames.Dashboard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Vars.currentFragment != null && Vars.currentFragment.getTag() != null) {
            if (Objects.equals(Vars.currentFragment.getTag(), FragmentNames.Profile)) {
                getMenuInflater().inflate(R.menu.profile_menu, menu);
                return true;
            } else if (isSearchEnable()) {
                getMenuInflater().inflate(R.menu.search_menu, menu);
                MenuItem search = menu.findItem(R.id.searchSMI);
                materialSearchView.setMenuItem(search);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Vars.currentFragment != null && Vars.currentFragment.getTag() != null) {
            if (Objects.equals(Vars.currentFragment.getTag(), FragmentNames.Profile)) {
                switch (item.getItemId()) {
                    case R.id.editProfilePMI: {
                        ProfileController.OpenProfileEditFragment();
                        break;
                    }
                    case R.id.signOutPMI: {
                        AuthController.SignOut();
                        Vars.localDB.clearLocalDB();
                        ActivityTrigger.AuthActivity();
                        break;
                    }
                }
            }
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Vars.Flag.IsDashboardLoaded = true;
        Vars.localDB.saveString(LDBModel.SAVE_ACTIVITY_STATE, LDBModel.TOKEN_ACTIVITY_STATE_OPENED);

        // Seeder
        instance.get().SeedLoader(isLoadSeed);

        // Show animation at the starting
        // overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Vars.Flag.IsDashboardLoaded = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Vars.localDB.saveString(LDBModel.SAVE_ACTIVITY_STATE, LDBModel.TOKEN_ACTIVITY_STATE_CLOSED);

        // Null -> Firebase instance - LocalDb instance
    }

    @Override
    public void onBackPressed() {
        // Search View
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();
            return;
        }

        if (Vars.currentFragment == null) {
            super.onBackPressed();
            return;
        }

        if (Vars.currentFragment.getTag() != null && Objects.equals(Vars.currentFragment.getTag(), FragmentNames.Dashboard)) {
            super.onBackPressed();
        } else if(Vars.currentFragment.getTag() != null && Objects.equals(Vars.currentFragment.getTag(), FragmentNames.ProfileEdit)) {
            loadFragment(TitleText.Profile, new ProfileFragment(), FragmentNames.Profile);
        } else {
            loadFragment(TitleText.Dashboard, new DashboardFragment(), FragmentNames.Dashboard);
        }
    }

    @Override
    public void SeedLoader(boolean isLoadSeed) {
        if (isLoadSeed) {

        } else {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.SeedingFalse, Toast.LENGTH_SHORT).show();
        }
    }
}
