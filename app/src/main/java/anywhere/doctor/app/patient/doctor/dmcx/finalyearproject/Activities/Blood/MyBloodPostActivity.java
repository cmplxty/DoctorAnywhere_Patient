package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blood;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.MyBloodPostRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.BloodDonorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodPost;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class MyBloodPostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView noDataFoundMBPTV;
    private RecyclerView bloodPostsMBPRV;
    private RotateLoading mLoadingRL;

    private MyBloodPostRecyclerViewAdapter myBloodPostRecyclerViewAdapter;
    private List<BloodPost> bloodPosts;

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        noDataFoundMBPTV = findViewById(R.id.noDataFoundMBPTV);
        bloodPostsMBPRV = findViewById(R.id.bloodPostsMBPRV);
        mLoadingRL = findViewById(R.id.mLoadingRL);

        bloodPostsMBPRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        bloodPostsMBPRV.setHasFixedSize(true);

        myBloodPostRecyclerViewAdapter = new MyBloodPostRecyclerViewAdapter();
        bloodPostsMBPRV.setAdapter(myBloodPostRecyclerViewAdapter);

        bloodPosts = new ArrayList<>();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadMyPostData() {
        mLoadingRL.start();

        BloodDonorController.LoadMyBloodPosts(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                bloodPosts = new ArrayList<>();

                mLoadingRL.stop();
                if (object != null) {
                    for (Object post : (List<?>) object) {
                        if (post != null && post instanceof BloodPost) {
                            bloodPosts.add((BloodPost) post);
                        }
                    }
                }

                updateAdapter(bloodPosts);
                updateLayout(bloodPosts);
            }
        });
    }

    private void updateAdapter(List<BloodPost> bloodPosts) {
        myBloodPostRecyclerViewAdapter.setBloodPosts(bloodPosts);
        myBloodPostRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateLayout(List<BloodPost> bloodPosts) {
        if (bloodPosts.size() > 0) {
            bloodPostsMBPRV.setVisibility(View.VISIBLE);
            noDataFoundMBPTV.setVisibility(View.GONE);
        } else {
            noDataFoundMBPTV.setVisibility(View.VISIBLE);
            bloodPostsMBPRV.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_blood_post);
        RefActivity.updateACActivity(this);
        init();
        setToolbar();
        loadMyPostData();
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
        RefActivity.updateACActivity(BloodDonorActivity.instance.get());
        super.onBackPressed();
    }

}
