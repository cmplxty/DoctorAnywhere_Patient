package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeService;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.HomeServiceRequestRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.HomeServiceController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICall;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HomeService;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class HomeServiceListActivity extends AppCompatActivity implements ISearch {

    // Variables
    private Toolbar toolbar;
    private RecyclerView homeServiceHSLRV;
    private RotateLoading mLoadingRL;
    private TextView noItemInListHSTV;
    private MaterialSearchView materialSearchView;

    private HomeServiceRequestRecyclerViewAdapter homeServiceRequestRecyclerViewAdapter;
    private ICall iCall;
    private List<HomeService> homeServices;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        homeServiceHSLRV = findViewById(R.id.homeServiceHSLRV);
        mLoadingRL = findViewById(R.id.mLoadingRL);
        noItemInListHSTV = findViewById(R.id.noItemInListHSTV);
        materialSearchView = findViewById(R.id.materialSearchView);

        homeServiceRequestRecyclerViewAdapter = new HomeServiceRequestRecyclerViewAdapter();
        homeServiceHSLRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        homeServiceHSLRV.setHasFixedSize(true);
        homeServiceHSLRV.setAdapter(homeServiceRequestRecyclerViewAdapter);

        iCall = homeServiceRequestRecyclerViewAdapter.getiCall();
        homeServices = new ArrayList<>();
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

        SearchController.setPage(Vars.Search.PAGE_HOME_SERVICE_LIST);
        SearchController.setiSearch(this);
    }

    private void loadHSRequests() {
        mLoadingRL.start();

        HomeServiceController.LoadHomeServiceRequests(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                homeServices = new ArrayList<>();
                if (object != null) {
                    for (Object obj : (List<?>) object) {
                        homeServices.add((HomeService) obj);
                    }
                }

                updateLayout(homeServices);
                updateAdapter(homeServices);
            }
        });
    }

    private void updateAdapter(List<HomeService> homeServices) {
        homeServiceRequestRecyclerViewAdapter.setHomeServices(homeServices);
        homeServiceRequestRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateLayout(List<HomeService> homeServices) {
        if (homeServices.size() > 0) {
            noItemInListHSTV.setVisibility(View.GONE);
            homeServiceHSLRV.setVisibility(View.VISIBLE);
        } else {
            homeServiceHSLRV.setVisibility(View.GONE);
            noItemInListHSTV.setVisibility(View.VISIBLE);
        }
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_service_list);
        RefActivity.updateACActivity(this);

        init();
        setupToolbar();
        setupSearchView();
        loadHSRequests();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_CALL_CODE_HS) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iCall.call();
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
        }

        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }

    @Override
    public void onSearch(List<?> objects) {
        if (objects != null) {
            List<HomeService> searches = new ArrayList<>();
            for (Object object  : objects) {
                if (object  instanceof HomeService)
                    searches.add((HomeService) object);
            }

            updateLayout(searches);
            updateAdapter(searches);
        }
    }

    @Override
    public List<HomeService> getList() {
        return homeServices;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
