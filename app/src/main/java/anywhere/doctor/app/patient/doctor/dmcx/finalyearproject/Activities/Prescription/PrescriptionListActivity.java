package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Prescription;

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

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.PrescriptionListRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.PrescriptionController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Prescription;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class PrescriptionListActivity extends AppCompatActivity implements ISearch {

    // Variables
    private Toolbar toolbar;
    private TextView noDataFoundTV;
    private RecyclerView prescriptionListPLRV;
    private MaterialSearchView materialSearchView;

    private PrescriptionListRecyclerViewAdapter prescriptionListRecyclerViewAdapter;
    private List<Prescription> prescriptions;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        noDataFoundTV = findViewById(R.id.noDataFoundTV);
        prescriptionListPLRV = findViewById(R.id.prescriptionListPLRV);
        materialSearchView = findViewById(R.id.materialSearchView);

        prescriptionListRecyclerViewAdapter = new PrescriptionListRecyclerViewAdapter(RefActivity.refACActivity.get());
        prescriptionListPLRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        prescriptionListPLRV.setHasFixedSize(true);
        prescriptionListPLRV.setAdapter(prescriptionListRecyclerViewAdapter);

        prescriptions = new ArrayList<>();
    }

    private void setupSearchView() {
        materialSearchView.setOnSearchViewListener(SearchController.getInstance());
        materialSearchView.setOnQueryTextListener(SearchController.getInstance());

        SearchController.setPage(Vars.Search.PAGE_PRESCRIPTION_LIST);
        SearchController.setiSearch(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadData() {
        PrescriptionController.LoadAllPrescriptions(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                prescriptions = new ArrayList<>();

                if (object instanceof List) {
                    for (Object prescription : (List<?>) object) {
                        prescriptions.add((Prescription) prescription);
                    }
                }

                updateAdapter(prescriptions);
                updateLayout(prescriptions);
            }
        });
    }

    private void updateLayout(List<Prescription> prescriptions) {
        if (prescriptions.size() > 0) {
            prescriptionListPLRV.setVisibility(View.VISIBLE);
            noDataFoundTV.setVisibility(View.GONE);
        } else {
            prescriptionListPLRV.setVisibility(View.VISIBLE);
            noDataFoundTV.setVisibility(View.GONE);
        }
    }

    private void updateAdapter(List<Prescription> prescriptions) {
        prescriptionListRecyclerViewAdapter.setPrescription(prescriptions);
        prescriptionListRecyclerViewAdapter.notifyDataSetChanged();
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);
        RefActivity.updateACActivity(this);

        init();
        setupToolbar();
        loadData();
        setupSearchView();
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
            List<Prescription> searches = new ArrayList<>();
            for (Object object : objects) {
                if (object instanceof Prescription) {
                    searches.add((Prescription) object);
                }
            }

            updateAdapter(searches);
            updateLayout(searches);
        }
    }

    @Override
    public List<Prescription> getList() {
        return prescriptions;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
