package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.victor.loading.rotate.RotateLoading;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.AudioCall.AudioCallHistoryActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.DoctorsListMessageRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.DoctorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Blog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorsListMessageActivity extends AppCompatActivity implements ISearch {

    // Variables
    public static WeakReference<DoctorsListMessageActivity> instance;

    private Toolbar toolbar;
    private RotateLoading mLoadingRL;
    private TextView noDataFoundMTV;
    private RecyclerView doctorsListMRV;
    private MaterialSearchView materialSearchView;

    private DoctorsListMessageRecyclerViewAdapter doctorsRecyclerViewAdapter;
    private List<Doctor> doctors;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        mLoadingRL = findViewById(R.id.mLoadingRL);
        noDataFoundMTV = findViewById(R.id.noDataFoundMTV);
        doctorsListMRV = findViewById(R.id.doctorsListMRV);
        materialSearchView = findViewById(R.id.materialSearchView);

        doctorsListMRV.setLayoutManager(new LinearLayoutManager(HomeActivity.instance.get()));
        doctorsListMRV.setHasFixedSize(true);
        doctorsRecyclerViewAdapter = new DoctorsListMessageRecyclerViewAdapter();
        doctorsListMRV.setAdapter(doctorsRecyclerViewAdapter);

        doctors = new ArrayList<>();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupSearchView() {
        materialSearchView.setOnSearchViewListener(SearchController.getInstance());
        materialSearchView.setOnQueryTextListener(SearchController.getInstance());

        SearchController.setPage(Vars.Search.PAGE_DOCTOR_LIST_MESSAGE);
        SearchController.setiSearch(this);
    }

    private void loadList() {
        mLoadingRL.start();

        DoctorController.LoadDoctors(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                doctors = new ArrayList<>();

                if (object != null) {
                    for (Object doctor : (List<?>) object) {
                        doctors.add((Doctor) doctor);
                    }
                }

                updateAdapter(doctors);
                updateLayout(doctors);
            }
        });
    }

    private void updateAdapter(List<Doctor> doctors) {
        doctorsRecyclerViewAdapter.setDoctors(doctors);
        doctorsRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateLayout(List<Doctor> doctors) {
        if (doctors.size() > 0) {
            noDataFoundMTV.setVisibility(View.GONE);
            doctorsListMRV.setVisibility(View.VISIBLE);
        } else {
            doctorsListMRV.setVisibility(View.GONE);
            noDataFoundMTV.setVisibility(View.VISIBLE);
        }
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list_message);
        RefActivity.updateACActivity(this);
        instance = new WeakReference<>(this);
        init();
        setupToolbar();
        setupSearchView();
        loadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.searchSMI);
        materialSearchView.setMenuItem(search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();
            return;
        }

        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }

    @Override
    public void onSearch(List<?> objects) {
        if (objects != null) {
            List<Doctor> searches = new ArrayList<>();
            for (Object object  : objects) {
                if (object  instanceof Doctor)
                    searches.add((Doctor) object);
            }

            updateLayout(searches);
            updateAdapter(searches);
        }
    }

    @Override
    public List<Doctor> getList() {
        return doctors;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
