package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blood;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.victor.loading.rotate.RotateLoading;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.BloodDonorRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.BloodPostRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.BloodDonorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICall;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodDonor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodPost;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class BloodDonorActivity extends AppCompatActivity implements ISearch, View.OnClickListener {

    public static WeakReference<BloodDonorActivity> instance;

    private final String TAG_NEW_BLOOD_DONOR = "NewBLOODDonor";
    private final String TAG_BLOOD_DONOR = "BLOODDonor";

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialSearchView materialSearchView;
    private ImageView bloodDonorBIV;
    private ImageView swichBIV;
    private ImageView bloodDonorPostsBIV;
    private NestedScrollView bloodGroupListBNSV;

    private ConstraintLayout bloodDonorBCL;
    private TextView noDonorFoundBTV;
    private RecyclerView bloodDonorListBRV;
    private RotateLoading mLoadingORL;
    private SwipeRefreshLayout refreshBloodDonorsBSRL;

    private ConstraintLayout bloodPostBCL;
    private TextView noPostFoundBTV;
    private RecyclerView bloodPostListBRV;
    private RotateLoading mLoadingTRL;

    private TextView aPositiveNITV;
    private TextView bPositiveNITV;
    private TextView oPositiveNITV;
    private TextView abPositiveNITV;
    private TextView aNegativeNITV;
    private TextView bNegativeNITV;
    private TextView oNegativeNITV;
    private TextView abNegativeNITV;

    private ICall iCallDonor;
    private ICall iCallPost;

    private BloodDonorRecyclerViewAdapter bloodDonorRecyclerViewAdapter;
    private BloodPostRecyclerViewAdapter bloodPostRecyclerViewAdapter;
    private List<BloodDonor> bloodDonors;
    private List<BloodPost> bloodPosts;
    private boolean isBloodDonorPage;

    private BloodActivityHandler bloodActivityHandler = new BloodActivityHandler();

    private static class BloodActivityHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    }

    private class OpenNewBloodDonorActivityRunnable implements Runnable {
        @Override
        public void run() {
            ActivityTrigger.AddNewBloodDonorActivity();
        }
    }

    private class OpenMyBloodDonorActivityRunnable implements Runnable {
        @Override
        public void run() {
            BloodDonorController.MyBloodDonorData(new IAction() {
                @Override
                public void onCompleteAction(Object object) {
                    if (object != null) {
                        ActivityTrigger.MyBloodDonorActivity((BloodDonor) object);
                    }
                }
            });
        }
    }

    private class OpenMyBloodPostActivityRunnable implements Runnable {
        @Override
        public void run() {
            BloodDonorController.CheckMyBloodPosts(new IAction() {
                @Override
                public void onCompleteAction(Object object) {


                    if ((Boolean) object) {
                        ActivityTrigger.MyBloodPostActivity();
                    }
                }
            });
        }
    }

    private class NavigationBloodGroupLoaderRunnable implements Runnable {
        private View view;

        NavigationBloodGroupLoaderRunnable(View view) {
            this.view = view;
        }

        @Override
        public void run() {
            navigationBloodGroupLoader(view);
        }
    }

    private class SwitchDonorPostRunnable implements Runnable {
        @Override
        public void run() {
            swichDonorAndPost();
        }
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        materialSearchView = findViewById(R.id.materialSearchView);
        navigationView = findViewById(R.id.navigationView);
        bloodDonorBIV = findViewById(R.id.bloodDonorBIV);
        bloodDonorPostsBIV = findViewById(R.id.bloodDonorPostsBIV);
        swichBIV = findViewById(R.id.swichBIV);
        bloodGroupListBNSV = findViewById(R.id.bloodGroupListBNSV);

        refreshBloodDonorsBSRL = findViewById(R.id.refreshBloodDonorsBSRL);
        bloodDonorBCL = findViewById(R.id.bloodDonorBCL);
        noDonorFoundBTV = findViewById(R.id.noDonorFoundBTV);
        bloodDonorListBRV = findViewById(R.id.bloodDonorListBRV);
        mLoadingORL = findViewById(R.id.mLoadingORL);

        bloodPostBCL = findViewById(R.id.bloodPostBCL);
        noPostFoundBTV = findViewById(R.id.noPostFoundBTV);
        bloodPostListBRV = findViewById(R.id.bloodPostListBRV);
        mLoadingTRL = findViewById(R.id.mLoadingTRL);

        aPositiveNITV = findViewById(R.id.aPositiveNITV);
        bPositiveNITV = findViewById(R.id.bPositiveNITV);
        oPositiveNITV = findViewById(R.id.oPositiveNITV);
        abPositiveNITV = findViewById(R.id.abPositiveNITV);
        aNegativeNITV = findViewById(R.id.aNegativeNITV);
        bNegativeNITV = findViewById(R.id.bNegativeNITV);
        oNegativeNITV = findViewById(R.id.oNegativeNITV);
        abNegativeNITV = findViewById(R.id.abNegativeNITV);

        bloodDonorListBRV.setHasFixedSize(true);
        bloodDonorListBRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));

        bloodPostListBRV.setHasFixedSize(true);
        bloodPostListBRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));

        bloodDonors = new ArrayList<>();
        bloodPosts = new ArrayList<>();

        bloodDonorRecyclerViewAdapter = new BloodDonorRecyclerViewAdapter();
        bloodDonorListBRV.setAdapter(bloodDonorRecyclerViewAdapter);

        bloodPostRecyclerViewAdapter = new BloodPostRecyclerViewAdapter();
        bloodPostListBRV.setAdapter(bloodPostRecyclerViewAdapter);

        bloodDonorBIV.setVisibility(View.INVISIBLE);

        iCallDonor = bloodDonorRecyclerViewAdapter.getiCall();
        iCallPost = bloodPostRecyclerViewAdapter.getiCall();
        isBloodDonorPage = true;
    }

    private void setupSearchView() {
        materialSearchView.setOnSearchViewListener(SearchController.getInstance());
        materialSearchView.setOnQueryTextListener(SearchController.getInstance());

        SearchController.setPage(Vars.Search.PAGE_BLOOD_DONOR_LIST);
        SearchController.setiSearch(this);
    }

    private void switchSearch() {
        if (isBloodDonorPage) {
            toolbar.setTitle("Blood");
            SearchController.setPage(Vars.Search.PAGE_BLOOD_DONOR_LIST);
        } else {
            toolbar.setTitle("Post");
            SearchController.setPage(Vars.Search.PAGE_BLOOD_POST_LIST);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(RefActivity.refACActivity.get(), drawerLayout, toolbar, R.string.open_navbar, R.string.close_navbar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void event() {
        bloodDonorBIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bloodDonorBIV.getTag().equals(TAG_NEW_BLOOD_DONOR)) {
                    if (drawerLayout.isDrawerOpen(Gravity.START)) {
                        drawerLayout.closeDrawer(Gravity.START);
                    }

                    bloodActivityHandler.postDelayed(new OpenNewBloodDonorActivityRunnable(), 300);
                } else if (bloodDonorBIV.getTag().equals(TAG_BLOOD_DONOR)) {
                    if (drawerLayout.isDrawerOpen(Gravity.START)) {
                        drawerLayout.closeDrawer(Gravity.START);
                    }

                    bloodActivityHandler.postDelayed(new OpenMyBloodDonorActivityRunnable(), 300);
                }
            }
        });

        swichBIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START);
                }

                bloodActivityHandler.postDelayed(new SwitchDonorPostRunnable(), 300);
            }
        });

        bloodDonorPostsBIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START);
                }

                bloodActivityHandler.postDelayed(new OpenMyBloodPostActivityRunnable(), 300);
            }
        });

        refreshBloodDonorsBSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBloodDonorsBSRL.setRefreshing(true);
                loadBloodDonors();
            }
        });
    }

    private void bloodGroupNavEvents() {
        aPositiveNITV.setOnClickListener(this);
        bPositiveNITV.setOnClickListener(this);
        oPositiveNITV.setOnClickListener(this);
        abPositiveNITV.setOnClickListener(this);
        aNegativeNITV.setOnClickListener(this);
        bNegativeNITV.setOnClickListener(this);
        oNegativeNITV.setOnClickListener(this);
        abNegativeNITV.setOnClickListener(this);
    }

    private void checkUserBloodDonor() {
        BloodDonorController.CheckMeAsBloodDonor(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if ((Boolean) object) {
                    bloodDonorBIV.setImageResource(R.drawable.person_white);
                    bloodDonorBIV.setTag(TAG_BLOOD_DONOR);
                } else {
                    bloodDonorBIV.setImageResource(R.drawable.add_white_0);
                    bloodDonorBIV.setTag(TAG_NEW_BLOOD_DONOR);
                }

                bloodDonorBIV.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadBloodDonors() {
        // Enabling
        if (!refreshBloodDonorsBSRL.isRefreshing())
            mLoadingORL.start();

        BloodDonorController.LoadAllBloodDonor(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                bloodDonors = new ArrayList<>();
                if (object != null) {
                    for (Object donor : (List<?>) object) {
                        if (donor != null) {
                            bloodDonors.add((BloodDonor) donor);
                        }
                    }
                }

                updateBloodDonorAdapter( bloodDonors);
                updateDonorLayout(bloodDonors);

                // Disables
                mLoadingORL.stop();
                if (refreshBloodDonorsBSRL.isRefreshing()) {
                    refreshBloodDonorsBSRL.setRefreshing(false);
                }
            }
        });
    }

    private void loadBloodDonorsByGroup(String bloodGroup) {
        // Enabling
        mLoadingORL.start();

        BloodDonorController.FindUserByBloodGroup(bloodGroup, new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                bloodDonors = new ArrayList<>();
                if (object != null) {
                    for (Object donor : (List<?>) object) {
                        if (donor != null) {
                            bloodDonors.add((BloodDonor) donor);
                        }
                    }
                }

                updateBloodDonorAdapter( bloodDonors);
                updateDonorLayout(bloodDonors);

                // Disables
                mLoadingORL.stop();
                if (refreshBloodDonorsBSRL.isRefreshing()) {
                    refreshBloodDonorsBSRL.setRefreshing(false);
                }
            }
        });
    }

    private void loadBloodPosts() {
        mLoadingTRL.start();

        BloodDonorController.LoadAllBloodPosts(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingTRL.stop();

                bloodPosts = new ArrayList<>();
                if (object != null) {
                    for (Object post : (List<?>) object) {
                        if (post != null) {
                            bloodPosts.add((BloodPost) post);
                        }
                    }
                }

                updateBloodPostAdapter(bloodPosts);
                updatePostLayout(bloodPosts);
            }
        });
    }

    private void updateBloodDonorAdapter(List<BloodDonor> donors) {
        bloodDonorRecyclerViewAdapter.setBloodDonors(donors);
        bloodDonorRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateBloodPostAdapter(List<BloodPost> posts) {
        bloodPostRecyclerViewAdapter.setBloodPosts(posts);
        bloodPostRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateDonorLayout(List<BloodDonor> donors) {
        if (donors.size() > 0) {
            bloodDonorListBRV.setVisibility(View.VISIBLE);
            noDonorFoundBTV.setVisibility(View.GONE);
        } else {
            noDonorFoundBTV.setVisibility(View.VISIBLE);
            bloodDonorListBRV.setVisibility(View.GONE);
        }
    }

    private void updatePostLayout(List<BloodPost> posts) {
        if (posts.size() > 0) {
            bloodPostListBRV.setVisibility(View.VISIBLE);
            noPostFoundBTV.setVisibility(View.GONE);
        } else {
            noPostFoundBTV.setVisibility(View.VISIBLE);
            bloodPostListBRV.setVisibility(View.GONE);
        }
    }

    private void swichDonorAndPost() {
        if (bloodDonorBCL.getVisibility() == View.VISIBLE) {
            bloodPostBCL.setVisibility(View.VISIBLE);
            bloodDonorBCL.setVisibility(View.GONE);

            bloodGroupListBNSV.setVisibility(View.INVISIBLE);

            isBloodDonorPage = false;
            switchSearch();
        } else if (bloodPostBCL.getVisibility() == View.VISIBLE) {
            bloodDonorBCL.setVisibility(View.VISIBLE);
            bloodPostBCL.setVisibility(View.GONE);

            bloodGroupListBNSV.setVisibility(View.VISIBLE);

            isBloodDonorPage = true;
            switchSearch();
        }
    }

    private void navigationBloodGroupLoader(View view) {
        switch (view.getId()) {
            case R.id.aPositiveNITV: {
                String group = getResources().getStringArray(R.array.blood_group_array)[0];
                Toast.makeText(RefActivity.refACActivity.get(), group, Toast.LENGTH_SHORT).show();
                loadBloodDonorsByGroup(group);
                break;
            }
            case R.id.bPositiveNITV: {
                String group = getResources().getStringArray(R.array.blood_group_array)[2];
                Toast.makeText(RefActivity.refACActivity.get(), group, Toast.LENGTH_SHORT).show();
                loadBloodDonorsByGroup(group);
                break;
            }
            case R.id.oPositiveNITV: {
                String group = getResources().getStringArray(R.array.blood_group_array)[4];
                Toast.makeText(RefActivity.refACActivity.get(), group, Toast.LENGTH_SHORT).show();
                loadBloodDonorsByGroup(group);
                break;
            }
            case R.id.abPositiveNITV: {
                String group = getResources().getStringArray(R.array.blood_group_array)[6];
                Toast.makeText(RefActivity.refACActivity.get(), group, Toast.LENGTH_SHORT).show();
                loadBloodDonorsByGroup(group);
                break;
            }
            case R.id.aNegativeNITV: {
                String group = getResources().getStringArray(R.array.blood_group_array)[1];
                Toast.makeText(RefActivity.refACActivity.get(), group, Toast.LENGTH_SHORT).show();
                loadBloodDonorsByGroup(group);
                break;
            }
            case R.id.bNegativeNITV: {
                String group = getResources().getStringArray(R.array.blood_group_array)[3];
                Toast.makeText(RefActivity.refACActivity.get(), group, Toast.LENGTH_SHORT).show();
                loadBloodDonorsByGroup(group);
                break;
            }
            case R.id.oNegativeNITV: {
                String group = getResources().getStringArray(R.array.blood_group_array)[5];
                Toast.makeText(RefActivity.refACActivity.get(), group, Toast.LENGTH_SHORT).show();
                loadBloodDonorsByGroup(group);
                break;
            }
            case R.id.abNegativeNITV: {
                String group = getResources().getStringArray(R.array.blood_group_array)[7];
                Toast.makeText(RefActivity.refACActivity.get(), group, Toast.LENGTH_SHORT).show();
                loadBloodDonorsByGroup(group);
                break;
            }
            default: {
                loadBloodDonors();
                break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donor);
        RefActivity.updateACActivity(this);
        instance = new WeakReference<>(this);
        init();
        setupToolbar();
        setupSearchView();
        event();
        bloodGroupNavEvents();
        checkUserBloodDonor();
        loadBloodDonors();
        loadBloodPosts();
    }

    @Override
    public void onClick(View view) {
        if (drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawer(Gravity.START);

        bloodActivityHandler.postDelayed(new NavigationBloodGroupLoaderRunnable(view), 300);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blood_donor_menu, menu);
        MenuItem search = menu.findItem(R.id.searchBDMI);
        materialSearchView.setMenuItem(search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.postBDMI: {
                ActivityTrigger.NewBloodPostActivity();
                break;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_CALL_CODE_BD) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iCallDonor.call();
            } else {
                Toast.makeText(RefActivity.refACActivity.get(), ErrorText.PermissionNeedToCallDoctor, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Vars.RequestCode.REQUEST_CALL_CODE_BP) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iCallPost.call();
            } else {
                Toast.makeText(RefActivity.refACActivity.get(), ErrorText.PermissionNeedToCallDoctor, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();
            return;
        } else if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
            return;
        }

        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }

    @Override
    public void onSearch(List<?> objects) {
        if (isBloodDonorPage) {
            List<BloodDonor> donors = new ArrayList<>();
            for (Object object : objects) {
                if (object != null && object instanceof BloodDonor) {
                    donors.add((BloodDonor) object);
                }
            }

            updateBloodDonorAdapter(donors);
            updateDonorLayout(donors);
        } else {
            List<BloodPost> posts = new ArrayList<>();
            for (Object object : objects) {
                if (object != null && object instanceof BloodPost) {
                    posts.add((BloodPost) object);
                }
            }

            updateBloodPostAdapter(posts);
            updatePostLayout(posts);
        }
    }

    @Override
    public List<?> getList() {
        if (isBloodDonorPage)
            return bloodDonors;
        else
            return bloodPosts;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
