package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileActivity extends AppCompatActivity {

    // Variables
    private CircleImageView doctorImageDPCIV;
    private TextView doctorNameDPTV;
    private TextView doctorSpecialistDPTV;
    private TextView doctorDegreeDPTV;
    private TextView doctorChamberDPTV;
    private TextView doctorHospitalDPTV;
    private TextView doctorAboutDPTV;
    private TextView doctorCityDPTV;
    // Variables

    // Methods
    private void init() {
        doctorImageDPCIV = findViewById(R.id.doctorImageDPCIV);
        doctorNameDPTV = findViewById(R.id.doctorNameDPTV);
        doctorSpecialistDPTV = findViewById(R.id.doctorSpecialistDPTV);
        doctorDegreeDPTV = findViewById(R.id.doctorDegreeDPTV);
        doctorChamberDPTV = findViewById(R.id.doctorChamberDPTV);
        doctorHospitalDPTV = findViewById(R.id.doctorHospitalDPTV);
        doctorAboutDPTV = findViewById(R.id.doctorAboutDPTV);
        doctorCityDPTV = findViewById(R.id.doctorCityDPTV);
    }

    private void updateUi() {
        Doctor doctor = getIntent().getParcelableExtra(Vars.Connector.DOCTOR_PROFILE_ACTIVITY_DATA);

        if (doctor != null) {
            Picasso.with(this).load(doctor.getImage_link()).placeholder(R.drawable.noperson).into(doctorImageDPCIV);

            doctorNameDPTV.setText(doctor.getName());
            doctorSpecialistDPTV.setText(new StringBuilder("Specialist: ").append(doctor.getSpecialist()));
            doctorDegreeDPTV.setText(new StringBuilder("Degree: ").append(doctor.getDegree()));
            doctorChamberDPTV.setText(new StringBuilder("Chamber: ").append(doctor.getChamber()));
            doctorHospitalDPTV.setText(new StringBuilder("Hospital: ").append(doctor.getHospital()));
            doctorCityDPTV.setText(new StringBuilder("City: ").append(doctor.getCity()));
            doctorAboutDPTV.setText(doctor.getAbout());
        } else {
            doctorNameDPTV.setText("NaN");
            doctorSpecialistDPTV.setText("NaN");
            doctorDegreeDPTV.setText("NaN");
            doctorChamberDPTV.setText("NaN");
            doctorHospitalDPTV.setText("NaN");
            doctorAboutDPTV.setText("NaN");
            doctorCityDPTV.setText("NaN");
        }
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        init();
        updateUi();
    }
}
