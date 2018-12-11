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
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.MessageUserRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.DoctorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.MessageController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.MessageUser;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class MessageUserListActivity extends AppCompatActivity implements ISearch {

    // Variables
    public static MessageUserListActivity instance;

    private Toolbar toolbar;
    private RecyclerView messagesHFMRV;
    private RotateLoading mLoadingRL;
    private MaterialSearchView materialSearchView;
    private TextView noDataFoundTV;

    private MessageUserRecyclerViewAdapter messageUserRecyclerViewAdapter;
    private List<MessageUser> messageUsers;
    private List<Doctor> doctors;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        mLoadingRL = findViewById(R.id.mLoadingRL);
        noDataFoundTV = findViewById(R.id.noDataFoundTV);
        materialSearchView = findViewById(R.id.materialSearchView);
        messagesHFMRV = findViewById(R.id.messagesHFMRV);

        messageUserRecyclerViewAdapter = new MessageUserRecyclerViewAdapter();
        messagesHFMRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        messagesHFMRV.setHasFixedSize(true);
        messagesHFMRV.setAdapter(messageUserRecyclerViewAdapter);

        messageUsers = new ArrayList<>();

        MessageController.UpdateMessageNotViewedToViewedMessage();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadAllDoctors() {
        mLoadingRL.start();

        DoctorController.LoadDoctors(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object != null) {
                    doctors = new ArrayList<>();
                    for (Object data : (List<?>) object) {
                        if (data instanceof Doctor)
                            doctors.add((Doctor) data);
                    }

                    loadData();
                }
            }
        });
    }

    private void loadData() {
        messageUserRecyclerViewAdapter.setDoctors(doctors);

        MessageController.LoadMessgeUsersList(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                if (object instanceof String) {
                    String errorCode = (String) object;
                    Toast.makeText(RefActivity.refACActivity.get(), errorCode, Toast.LENGTH_SHORT).show();
                    return;
                }

                messageUsers = new ArrayList<>();
                for (Object data : (List<?>) object) {
                    if (data instanceof MessageUser)
                        messageUsers.add((MessageUser) data);
                }

                updateAdapter(messageUsers);
                updateLayout(messageUsers);
            }
        });
    }

    private void setupSearchView() {
        materialSearchView.setOnSearchViewListener(SearchController.getInstance());
        materialSearchView.setOnQueryTextListener(SearchController.getInstance());

        SearchController.setPage(Vars.Search.PAGE_MESSAGE_USER_LIST);
        SearchController.setiSearch(this);
    }

    private void updateLayout(List<MessageUser> messageUsers) {
        if (messageUsers.size() > 0) {
            messagesHFMRV.setVisibility(View.VISIBLE);
            noDataFoundTV.setVisibility(View.GONE);
        } else {
            messagesHFMRV.setVisibility(View.VISIBLE);
            noDataFoundTV.setVisibility(View.GONE);
        }
    }

    private void updateAdapter(List<MessageUser> messageUsers) {
        messageUserRecyclerViewAdapter.setMessageUsers(messageUsers);
        messageUserRecyclerViewAdapter.notifyDataSetChanged();
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_user_list);
        RefActivity.updateACActivity(this);
        instance = (MessageUserListActivity) RefActivity.refACActivity.get();
        init();
        setupToolbar();
        loadAllDoctors();
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
            List<MessageUser> searches = new ArrayList<>();
            for (Object object : objects) {
                if (object instanceof MessageUser) {
                    searches.add((MessageUser) object);
                }
            }

            updateAdapter(searches);
            updateLayout(searches);
        }
    }

    @Override
    public List<?> getList() {
        List<List<?>> data = new ArrayList<>();
        data.add(doctors);
        data.add(messageUsers);

        return data;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
