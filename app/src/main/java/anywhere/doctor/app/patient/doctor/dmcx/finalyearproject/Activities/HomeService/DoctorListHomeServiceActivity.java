package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeService;

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

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.DoctorsListHomeServiceRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.HomeServiceController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HomeServiceDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorListHomeServiceActivity extends AppCompatActivity implements ISearch {

    // Variables
    private Toolbar toolbar;
    private TextView noDataFoundHSTV;
    private RecyclerView doctorsListHSRV;
    private RotateLoading mLoadingRL;
    private MaterialSearchView materialSearchView;

    private DoctorsListHomeServiceRecyclerViewAdapter doctorsListHomeServiceRecyclerViewAdapter;
    private List<HomeServiceDoctor> homeServiceDoctors;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        noDataFoundHSTV = findViewById(R.id.noDataFoundHSTV);
        mLoadingRL = findViewById(R.id.mLoadingRL);
        materialSearchView = findViewById(R.id.materialSearchView);
        doctorsListHSRV = findViewById(R.id.doctorsListHSRV);

        doctorsListHomeServiceRecyclerViewAdapter = new DoctorsListHomeServiceRecyclerViewAdapter();
        doctorsListHSRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        doctorsListHSRV.setHasFixedSize(true);
        doctorsListHSRV.setAdapter(doctorsListHomeServiceRecyclerViewAdapter);

        homeServiceDoctors = new ArrayList<>();
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

        SearchController.setPage(Vars.Search.PAGE_DOCTOR_LIST_HOME_SERVICE);
        SearchController.setiSearch(this);
    }

    private void loadData() {
        mLoadingRL.start();

        HomeServiceController.LoadAllHomeServiceDoctor(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();
                homeServiceDoctors = new ArrayList<>();

                for (Object obj : (List<?>) object) {
                    homeServiceDoctors.add((HomeServiceDoctor) obj);
                }

                updateAdapter(homeServiceDoctors);
                updateLayout(homeServiceDoctors);
            }
        });
    }

    private void updateAdapter(List<HomeServiceDoctor> homeServiceDoctors) {
        doctorsListHomeServiceRecyclerViewAdapter.setHsdoctors(homeServiceDoctors);
        doctorsListHomeServiceRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateLayout(List<HomeServiceDoctor> homeServiceDoctors) {
        if (homeServiceDoctors.size() > 0) {
            noDataFoundHSTV.setVisibility(View.GONE);
            doctorsListHSRV.setVisibility(View.VISIBLE);
        } else {
            doctorsListHSRV.setVisibility(View.GONE);
            noDataFoundHSTV.setVisibility(View.VISIBLE);
        }
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list_home_service);
        RefActivity.updateACActivity(this);

        init();
        setupToolbar();
        setupSearchView();
        loadData();
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
            List<HomeServiceDoctor> searches = new ArrayList<>();
            for (Object object  : objects) {
                if (object  instanceof HomeServiceDoctor)
                    searches.add((HomeServiceDoctor) object);
            }

            updateLayout(searches);
            updateAdapter(searches);
        }
    }

    @Override
    public List<HomeServiceDoctor> getList() {
        return homeServiceDoctors;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
