package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.AudioCall;

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

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.DoctorListAudioCallRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AudioCallController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AudioCallDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorListAudioCallActivity extends AppCompatActivity implements ISearch {

    // Variables
    public static WeakReference<DoctorListAudioCallActivity> instance = null;

    private Toolbar toolbar;
    private MaterialSearchView materialSearchView;
    private TextView noDataFoundDLACTV;
    private RecyclerView audioCallDoctorListDLACRV;
    private RotateLoading mLoadingRL;

    private DoctorListAudioCallRecyclerViewAdapter doctorListAudioCallRecyclerViewAdapter;
    private List<AudioCallDoctor> audioCallDoctors;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        materialSearchView = findViewById(R.id.materialSearchView);
        noDataFoundDLACTV = findViewById(R.id.noDataFoundDLACTV);
        audioCallDoctorListDLACRV = findViewById(R.id.audioCallDoctorListDLACRV);
        mLoadingRL = findViewById(R.id.mLoadingRL);

        audioCallDoctorListDLACRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        audioCallDoctorListDLACRV.setHasFixedSize(true);
        doctorListAudioCallRecyclerViewAdapter = new DoctorListAudioCallRecyclerViewAdapter();
        audioCallDoctorListDLACRV.setAdapter(doctorListAudioCallRecyclerViewAdapter);

        audioCallDoctors = new ArrayList<>();
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

        SearchController.setPage(Vars.Search.PAGE_DOCTOR_LIST_AUDIO_CALL);
        SearchController.setiSearch(this);
    }

    private void loadAudioCallUsers() {
        mLoadingRL.start();

        AudioCallController.LoadAllAudioCallDoctors(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                audioCallDoctors = new ArrayList<>();
                if (object != null) {
                    for (Object data : (List<?>) object) {
                        audioCallDoctors.add((AudioCallDoctor) data);
                    }
                }

                updateAdapter(audioCallDoctors);
                updateLayout(audioCallDoctors);
            }
        });
    }

    private void updateAdapter(List<AudioCallDoctor> audioCallDoctors) {
        doctorListAudioCallRecyclerViewAdapter.setAcDoctors(audioCallDoctors);
        doctorListAudioCallRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateLayout(List<AudioCallDoctor> audioCallDoctors) {
        if (audioCallDoctors.size() > 0) {
            noDataFoundDLACTV.setVisibility(View.GONE);
            audioCallDoctorListDLACRV.setVisibility(View.VISIBLE);
        } else {
            audioCallDoctorListDLACRV.setVisibility(View.GONE);
            noDataFoundDLACTV.setVisibility(View.VISIBLE);
        }
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list_audio_call);
        RefActivity.updateACActivity(this);
        instance = new WeakReference<>(this);
        init();
        setupToolbar();
        setupSearchView();
        loadAudioCallUsers();
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
            List<AudioCallDoctor> searches = new ArrayList<>();
            for (Object object  : objects) {
                if (object  instanceof AudioCallDoctor)
                    searches.add((AudioCallDoctor) object);
            }

            updateLayout(searches);
            updateAdapter(searches);
        }
    }

    @Override
    public List<AudioCallDoctor> getList() {
        return audioCallDoctors;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
