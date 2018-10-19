package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Prescription;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.PrescriptionListRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.PrescriptionController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Prescription;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class PrescriptionListActivity extends AppCompatActivity {

    // Variables
    private Toolbar toolbar;
    private RecyclerView prescriptionListPLRV;

    private PrescriptionListRecyclerViewAdapter prescriptionListRecyclerViewAdapter;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);

        prescriptionListRecyclerViewAdapter = new PrescriptionListRecyclerViewAdapter(RefActivity.refACActivity.get());
        prescriptionListPLRV = findViewById(R.id.prescriptionListPLRV);
        prescriptionListPLRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        prescriptionListPLRV.setHasFixedSize(true);
        prescriptionListPLRV.setAdapter(prescriptionListRecyclerViewAdapter);
    }

    private void init_toolbar() {
        setSupportActionBar(toolbar);
    }

    private void loadData() {
        PrescriptionController.LoadAllPrescriptions(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                List<Prescription> prescriptions = new ArrayList<>();
                if (object instanceof List) {
                    for (Object prescription : (List<?>) object) {
                        prescriptions.add((Prescription) prescription);
                    }
                }

                prescriptionListRecyclerViewAdapter.setPrescription(prescriptions);
                prescriptionListRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);
        RefActivity.updateACActivity(this);

        init();
        init_toolbar();
        loadData();
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance);
        super.onBackPressed();
    }
}
