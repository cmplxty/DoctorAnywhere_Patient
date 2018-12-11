package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Appointment;

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
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.DoctorListAppointmentRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AppointmentController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AppointmentDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorListAppointmentActivity extends AppCompatActivity implements ISearch {

    // Variables
    private Toolbar toolbar;
    private MaterialSearchView materialSearchView;
    private TextView noDataFoundDLATV;
    private RecyclerView appointmentDoctorListDLARV;
    private RotateLoading mLoadingRL;

    private DoctorListAppointmentRecyclerViewAdapter doctorListAppointmentRecyclerViewAdapter;
    private List<AppointmentDoctor> appointmentDoctors;

    // Methods
    private void init() {
        noDataFoundDLATV = findViewById(R.id.noDataFoundDLATV);
        appointmentDoctorListDLARV = findViewById(R.id.appointmentDoctorListDLARV);
        toolbar = findViewById(R.id.toolbar);
        mLoadingRL = findViewById(R.id.mLoadingRL);
        materialSearchView = findViewById(R.id.materialSearchView);

        appointmentDoctorListDLARV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        appointmentDoctorListDLARV.setHasFixedSize(true);

        doctorListAppointmentRecyclerViewAdapter = new DoctorListAppointmentRecyclerViewAdapter();
        appointmentDoctorListDLARV.setAdapter(doctorListAppointmentRecyclerViewAdapter);

        appointmentDoctors = new ArrayList<>();
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

        SearchController.setPage(Vars.Search.PAGE_DOCTOR_LIST_APPOINTMENT);
        SearchController.setiSearch(this);
    }

    private void loadData() {
        mLoadingRL.start();

        AppointmentController.LoadAllAppointmentDoctors(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                appointmentDoctors = new ArrayList<>();
                if (object != null) {
                    for (Object obj : (List<?>) object) {
                        appointmentDoctors.add((AppointmentDoctor) obj);
                    }
                }

                updateAdapter(appointmentDoctors);
                updateLayout(appointmentDoctors);
            }
        });
    }

    private void updateAdapter(List<AppointmentDoctor> appointmentDoctors) {
        doctorListAppointmentRecyclerViewAdapter.setApDoctors(appointmentDoctors);
        doctorListAppointmentRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateLayout(List<AppointmentDoctor> appointmentDoctors) {
        if (appointmentDoctors.size() > 0) {
            noDataFoundDLATV.setVisibility(View.GONE);
            appointmentDoctorListDLARV.setVisibility(View.VISIBLE);
        } else {
            appointmentDoctorListDLARV.setVisibility(View.GONE);
            noDataFoundDLATV.setVisibility(View.VISIBLE);
        }
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list_appointment);
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
            List<AppointmentDoctor> searches = new ArrayList<>();
            for (Object object  : objects) {
                if (object  instanceof AppointmentDoctor)
                    searches.add((AppointmentDoctor) object);
            }

            updateLayout(searches);
            updateAdapter(searches);
        }
    }

    @Override
    public List<AppointmentDoctor> getList() {
        return appointmentDoctors;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
