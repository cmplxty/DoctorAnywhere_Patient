package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Nurse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.NurseRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.NurseController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Nurse;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class NurseActivity extends AppCompatActivity {

    // Variables
    private RecyclerView nurseRV;
    private RotateLoading mLoadingRL;

    private NurseRecyclerViewAdapter nurseRecyclerViewAdapter;
    private List<Nurse> nurses;
    // Variables

    // Methods
    private void init() {
        nurseRV = findViewById(R.id.nurseRV);
        mLoadingRL = findViewById(R.id.mLoadingRL);
        nurseRV.setHasFixedSize(true);
        nurseRV.setLayoutManager(new LinearLayoutManager(this));

        nurseRecyclerViewAdapter = new NurseRecyclerViewAdapter();
        nurseRV.setAdapter(nurseRecyclerViewAdapter);
        nurses = new ArrayList<>();
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
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse);
        init();
        loadList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Show animation at the starting
//        overridePendingTransition(R.anim.slide_right_to_position, R.anim.slide_position_to_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        nurseRV = null;
    }
}
