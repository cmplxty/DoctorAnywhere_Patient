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
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.victor.loading.rotate.RotateLoading;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.AudioCallHistoryRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AudioCallController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AudioCallHistory;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AudioCallHistoryActivity extends AppCompatActivity implements ISearch {

    public static WeakReference<AudioCallHistoryActivity> instance;

    private Toolbar toolbar;
    private TextView informationACTV;
    private RecyclerView audioCallHistoryACRV;
    private RotateLoading mLoadingRL;
    private MaterialSearchView materialSearchView;

    private List<AudioCallHistory> histories;
    private AudioCallHistoryRecyclerViewAdapter audioCallHistoryRecyclerViewAdapter;

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        informationACTV = findViewById(R.id.informationACTV);
        audioCallHistoryACRV = findViewById(R.id.audioCallHistoryACRV);
        mLoadingRL = findViewById(R.id.mLoadingRL);
        materialSearchView = findViewById(R.id.materialSearchView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(RefActivity.refACActivity.get());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        audioCallHistoryACRV.setHasFixedSize(true);
        audioCallHistoryACRV.setLayoutManager(layoutManager);

        audioCallHistoryRecyclerViewAdapter = new AudioCallHistoryRecyclerViewAdapter();
        audioCallHistoryACRV.setAdapter(audioCallHistoryRecyclerViewAdapter);

        histories = new ArrayList<>();
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

        SearchController.setPage(Vars.Search.PAGE_AUDIO_CALL_HISTORY_LIST);
        SearchController.setiSearch(this);
    }

    private void loadHistories() {
        mLoadingRL.start();

        AudioCallController.LoadAllAudioCallHistory(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();
                histories = new ArrayList<>();

                if (object != null) {
                    for (Object history : (List<?>) object) {
                        if (history != null) {
                            histories.add((AudioCallHistory) history);
                        }
                    }
                }

                updateAdapter(histories);
                updateLayout(histories);
            }
        });
    }

    private void updateAdapter(List<AudioCallHistory> histories) {
        audioCallHistoryRecyclerViewAdapter.setHistories(histories);
        audioCallHistoryRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateLayout(List<AudioCallHistory> histories) {
        if (histories.size() > 0) {
            audioCallHistoryACRV.setVisibility(View.VISIBLE);
            informationACTV.setVisibility(View.GONE);
        } else {
            informationACTV.setVisibility(View.VISIBLE);
            audioCallHistoryACRV.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_call_history);
        RefActivity.updateACActivity(this);
        instance = new WeakReference<>(this);
        init();
        setupToolbar();
        setupSearchView();
        loadHistories();
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
            List<AudioCallHistory> searches = new ArrayList<>();
            for (Object search : objects) {
                searches.add((AudioCallHistory) search);
            }

            updateAdapter(searches);
            updateLayout(searches);
        }
    }

    @Override
    public List<?> getList() {
        return histories;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
