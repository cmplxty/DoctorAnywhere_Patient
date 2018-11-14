package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Appointment;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.DoctorListAppointmentRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AppointmentController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.APDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class DoctorListAppointmentActivity extends AppCompatActivity {

    // Variables
    private TextView noDataFoundDLATV;
    private RecyclerView appointmentDoctorListDLARV;
    private RotateLoading mLoadingRL;

    private DoctorListAppointmentRecyclerViewAdapter doctorListAppointmentRecyclerViewAdapter;
    // Variables

    // Methods
    private void init() {
        noDataFoundDLATV = findViewById(R.id.noDataFoundDLATV);
        appointmentDoctorListDLARV = findViewById(R.id.appointmentDoctorListDLARV);
        mLoadingRL = findViewById(R.id.mLoadingRL);

        appointmentDoctorListDLARV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        appointmentDoctorListDLARV.setHasFixedSize(true);

        doctorListAppointmentRecyclerViewAdapter = new DoctorListAppointmentRecyclerViewAdapter();
        appointmentDoctorListDLARV.setAdapter(doctorListAppointmentRecyclerViewAdapter);
    }

    private void loadData() {
        mLoadingRL.start();

        AppointmentController.LoadAllAppointmentDoctors(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                List<APDoctor> apDoctors = new ArrayList<>();

                if (object != null) {
                    for (Object obj : (List<?>) object) {
                        apDoctors.add((APDoctor) obj);
                    }
                }

                if (apDoctors.size() > 0) {
                    updateUi(View.GONE, View.VISIBLE);

                    doctorListAppointmentRecyclerViewAdapter.setApDoctors(apDoctors);
                    doctorListAppointmentRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    updateUi(View.VISIBLE, View.GONE);
                }
            }
        });
    }

    private void updateUi(int v1, int v2) {
        noDataFoundDLATV.setVisibility(v1);
        appointmentDoctorListDLARV.setVisibility(v2);
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list_appointment);
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
