package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Nurse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.NurseRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.NurseController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HomeService;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Nurse;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class NurseActivity extends AppCompatActivity implements ISearch {

    // Variables
    private Toolbar toolbar;
    private RecyclerView nurseRV;
    private RotateLoading mLoadingRL;
    private MaterialSearchView materialSearchView;

    private NurseRecyclerViewAdapter nurseRecyclerViewAdapter;
    private List<Nurse> nurses;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        nurseRV = findViewById(R.id.nurseRV);
        mLoadingRL = findViewById(R.id.mLoadingRL);
        materialSearchView = findViewById(R.id.materialSearchView);

        nurseRecyclerViewAdapter = new NurseRecyclerViewAdapter();
        nurseRV.setHasFixedSize(true);
        nurseRV.setLayoutManager(new LinearLayoutManager(this));
        nurseRV.setAdapter(nurseRecyclerViewAdapter);
        nurses = new ArrayList<>();
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

        SearchController.setPage(Vars.Search.PAGE_NURSE);
        SearchController.setiSearch(this);
    }

    private void loadList() {
        mLoadingRL.start();
        NurseController.LoadNurses(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();
                nurses = new ArrayList<>();

                if (object != null) {
                    for (Object nurse : (List<?>) object) {
                        nurses.add((Nurse) nurse);
                    }
                }

                nurseRecyclerViewAdapter.setNurses(nurses);
                nurseRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateAdapter(List<Nurse> nurses) {
        nurseRecyclerViewAdapter.setNurses(nurses);
        nurseRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateLayout(List<Nurse> nurses) {
        if (nurses.size() > 0) {
            nurseRV.setVisibility(View.GONE);
            nurseRV.setVisibility(View.VISIBLE);
        } else {
            nurseRV.setVisibility(View.GONE);
            nurseRV.setVisibility(View.VISIBLE);
        }
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse);
        RefActivity.updateACActivity(this);
        init();
        loadList();
        setupToolbar();
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
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Show animation at the starting
        // overridePendingTransition(R.anim.slide_right_to_position, R.anim.slide_position_to_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        nurseRV = null;
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
            List<Nurse> searches = new ArrayList<>();
            for (Object object  : objects) {
                if (object  instanceof Nurse)
                    searches.add((Nurse) object);
            }

            updateLayout(searches);
            updateAdapter(searches);
        }
    }

    @Override
    public List<Nurse> getList() {
        return nurses;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
