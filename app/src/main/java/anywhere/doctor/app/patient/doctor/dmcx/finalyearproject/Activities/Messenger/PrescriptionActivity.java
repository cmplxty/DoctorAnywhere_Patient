package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.MedicineRecylerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Medicine;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Prescription;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class PrescriptionActivity extends AppCompatActivity {

    // Variables
    private RecyclerView medicineListRV;
    private TextView doctorsNamePTV;
    private TextView patientNamePTV;
    private TextView prescriptionDatePTV;
    private TextView patientPhonePTV;
    private TextView patientAgePTV;
    private TextView patientAddressPTV;

    private MedicineRecylerViewAdapter medicineRecylerViewAdapter;
    private List<Medicine> medicines;
    private Prescription prescription;
    // Variables

    // Methods
    private void init() {
        doctorsNamePTV = findViewById(R.id.doctorsNamePTV);
        patientNamePTV = findViewById(R.id.patientNamePTV);
        prescriptionDatePTV = findViewById(R.id.prescriptionDatePTV);
        patientPhonePTV = findViewById(R.id.patientPhonePTV);
        patientAgePTV = findViewById(R.id.patientAgePTV);
        patientAddressPTV = findViewById(R.id.patientAddressPTV);

        medicineListRV = findViewById(R.id.medicineListRV);
        medicineListRV.setHasFixedSize(true);
        medicineListRV.setLayoutManager(new LinearLayoutManager(this));
        medicineRecylerViewAdapter = new MedicineRecylerViewAdapter(this);
        medicineListRV.setAdapter(medicineRecylerViewAdapter);

        medicines = new ArrayList<>();
        prescription = getIntent().getParcelableExtra(Vars.Connector.PERSCRIPTION_ACTIVITY_DATA);

        if (prescription == null) return;

        doctorsNamePTV.setText(prescription.getDoctor_name());
        patientNamePTV.setText(new StringBuilder("Name: ").append(prescription.getPatient_name()));
        patientPhonePTV.setText(new StringBuilder("Phone: ").append(prescription.getPatient_phone() == null || prescription.getPatient_phone().equals(AFModel.deflt) ? "NaN" : prescription.getPatient_phone()));
        patientAgePTV.setText(new StringBuilder("Age: ").append(prescription.getPatient_age() == null || prescription.getPatient_age().equals(AFModel.deflt) ? "NaN" : prescription.getPatient_age()));
        patientAddressPTV.setText(new StringBuilder("Address: ").append(prescription.getPatient_address() == null || prescription.getPatient_address().equals(AFModel.deflt) ? "NaN" : prescription.getPatient_address()));
        prescriptionDatePTV.setText(new StringBuilder("Date: ").append(prescription.getDate()));

        loadMedicines(prescription.getMedicines());
    }

    private void loadMedicines(String medicineJson) {
        if (medicineJson != null) {
            Gson gson = new Gson();
            medicines = gson.fromJson(medicineJson, new TypeToken<List<Medicine>>(){}.getType());

            medicineRecylerViewAdapter.setMedicines(medicines);
            medicineRecylerViewAdapter.notifyDataSetChanged();
        }
    }
    // Methods


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
