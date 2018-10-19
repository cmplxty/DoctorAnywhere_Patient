package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Nurse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.NurseRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Nurse;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class NurseActivity extends AppCompatActivity {

    // Variables
    private RecyclerView nurseRV;

    private NurseRecyclerViewAdapter nurseRecyclerViewAdapter;
    private List<Nurse> nurses;
    // Variables

    // Methods
    private void init() {
        nurseRV = findViewById(R.id.nurseRV);
        nurseRV.setHasFixedSize(true);
        nurseRV.setLayoutManager(new LinearLayoutManager(this));

        nurses = new ArrayList<>();
    }

    private void loadList() {
        nurses.add(new Nurse("1", "Marcelo Junior", "Female", "Montly", "", ""));
        nurses.add(new Nurse("1", "Marcelo Junior", "Female", "Montly", "", ""));
        nurses.add(new Nurse("1", "Marcelo Junior", "Female", "Montly", "", ""));
        nurses.add(new Nurse("1", "Marcelo Junior", "Female", "Montly", "", ""));
        nurses.add(new Nurse("1", "Marcelo Junior", "Female", "Montly", "", ""));
        nurses.add(new Nurse("1", "Marcelo Junior", "Female", "Montly", "", ""));
        nurses.add(new Nurse("1", "Marcelo Junior", "Female", "Montly", "", ""));
        nurses.add(new Nurse("1", "Marcelo Junior", "Female", "Montly", "", ""));

        nurseRecyclerViewAdapter = new NurseRecyclerViewAdapter(this, nurses);
        nurseRV.setAdapter(nurseRecyclerViewAdapter);
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse);

        // Initialization
        init();

        // Load Nurse List
        loadList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Show animation at the starting
//        overridePendingTransition(R.anim.slide_right_to_position, R.anim.slide_position_to_left);
    }
}
