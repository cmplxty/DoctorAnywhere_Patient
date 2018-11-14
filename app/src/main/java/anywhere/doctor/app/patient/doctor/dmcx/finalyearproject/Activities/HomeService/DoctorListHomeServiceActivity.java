package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.DoctorsListHomeServiceRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.HomeServiceController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HSDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class DoctorListHomeServiceActivity extends AppCompatActivity {

    // Variables
    private RecyclerView doctorsListHSRV;
    private DoctorsListHomeServiceRecyclerViewAdapter doctorsListHomeServiceRecyclerViewAdapter;
    // Variables

    // Methods
    private void init() {
        doctorsListHomeServiceRecyclerViewAdapter = new DoctorsListHomeServiceRecyclerViewAdapter();
        doctorsListHSRV = findViewById(R.id.doctorsListHSRV);
        doctorsListHSRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        doctorsListHSRV.setHasFixedSize(true);
        doctorsListHSRV.setAdapter(doctorsListHomeServiceRecyclerViewAdapter);
    }

    private void loadData() {
        HomeServiceController.LoadAllHomeServiceDoctor(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                List<HSDoctor> hsDoctors = new ArrayList<>();

                for (Object obj : (List<?>) object) {
                    hsDoctors.add((HSDoctor) obj);
                }

                doctorsListHomeServiceRecyclerViewAdapter.setHsdoctors(hsDoctors);
                doctorsListHomeServiceRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list_home_service);
        RefActivity.updateACActivity(this);

        init();
        loadData();
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }
}
