package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger;

import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.DoctorsListMessageRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.DoctorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class DoctorsListMessageActivity extends AppCompatActivity {

    // Variables
    private TextView chooseDoctCptTV;
    private RotateLoading mLoadingRL;
    private RecyclerView doctorsListRV;

    private DoctorsListMessageRecyclerViewAdapter doctorsRecyclerViewAdapter;
    private List<Doctor> doctors;
    // Variables

    // Methods
    private void init() {
        chooseDoctCptTV = findViewById(R.id.chooseDoctCptTV);
        mLoadingRL = findViewById(R.id.mLoadingRL);
        doctorsListRV = findViewById(R.id.doctorsListRV);

        doctorsListRV.setLayoutManager(new LinearLayoutManager(HomeActivity.instance));
        doctorsListRV.setHasFixedSize(true);

        doctors = new ArrayList<>();
    }

    private void event() {
        doctorsListRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (recyclerView.getLayoutManager().findViewByPosition(0) != null) {
                    if (recyclerView.getLayoutManager().findViewByPosition(0).getTop() >= 0) {
                        chooseDoctCptTV.setVisibility(View.VISIBLE);
                    } else {
                        chooseDoctCptTV.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });
    }

    private void loadList() {
        mLoadingRL.start();

        DoctorController.LoadDoctors(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                doctors = (List<Doctor>) object;
                doctorsRecyclerViewAdapter = new DoctorsListMessageRecyclerViewAdapter(doctors);
                doctorsListRV.setAdapter(doctorsRecyclerViewAdapter);
            }
        });
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list_message);
        RefActivity.updateACActivity(this);

        init();
        event();
        loadList();
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance);
        super.onBackPressed();
    }
}
