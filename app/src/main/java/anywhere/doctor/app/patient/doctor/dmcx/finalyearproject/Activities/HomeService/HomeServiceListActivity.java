package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeService;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.HomeServiceListRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.HomeServiceController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HomeService;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class HomeServiceListActivity extends AppCompatActivity {

    // Variables
    private RecyclerView homeServiceHSLRV;
    private RotateLoading mLoadingRL;
    private TextView noItemInListHSTV;

    private HomeServiceListRecyclerViewAdapter homeServiceListRecyclerViewAdapter;
    private ICallDoctor iCallDoctor;
    // Variables

    // Methods
    private void init() {
        homeServiceHSLRV = findViewById(R.id.homeServiceHSLRV);
        mLoadingRL = findViewById(R.id.mLoadingRL);
        noItemInListHSTV = findViewById(R.id.noItemInListHSTV);

        homeServiceListRecyclerViewAdapter = new HomeServiceListRecyclerViewAdapter();
        homeServiceHSLRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        homeServiceHSLRV.setHasFixedSize(true);
        homeServiceHSLRV.setAdapter(homeServiceListRecyclerViewAdapter);

        iCallDoctor = homeServiceListRecyclerViewAdapter.getiCallDoctor();
    }

    private void loadHSRequests() {
        mLoadingRL.start();

        HomeServiceController.LoadHomeServiceRequests(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                List<HomeService> homeServices = new ArrayList<>();

                if (object != null) {
                    for (Object obj : (List<?>) object) {
                        homeServices.add((HomeService) obj);
                    }
                }

                updateUI(homeServices);
                homeServiceListRecyclerViewAdapter.setHomeServices(homeServices);
                homeServiceListRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateUI(List<HomeService> homeServices) {
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
        loadHSRequests();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_CALL_CODE_HS) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iCallDoctor.call();
            } else {
                Toast.makeText(RefActivity.refACActivity.get(), ErrorText.PermissionNeedToCallDoctor, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance);
        super.onBackPressed();
    }
}
