package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.AudioCall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.DoctorListAudioCallRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AudioCallController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.ACDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;

public class DoctorListAudioCallActivity extends AppCompatActivity {

    // Variables
    private TextView noDataFoundDLACTV;
    private RecyclerView audioCallDoctorListDLACRV;
    private RotateLoading mLoadingRL;

    private DoctorListAudioCallRecyclerViewAdapter doctorListAudioCallRecyclerViewAdapter;
    // Variables

    // Methods
    private void init() {
        noDataFoundDLACTV = findViewById(R.id.noDataFoundDLACTV);
        audioCallDoctorListDLACRV = findViewById(R.id.audioCallDoctorListDLACRV);
        mLoadingRL = findViewById(R.id.mLoadingRL);

        audioCallDoctorListDLACRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        audioCallDoctorListDLACRV.setHasFixedSize(true);
        doctorListAudioCallRecyclerViewAdapter = new DoctorListAudioCallRecyclerViewAdapter();
        audioCallDoctorListDLACRV.setAdapter(doctorListAudioCallRecyclerViewAdapter);
    }

    private void event() {

    }

    private void loadAudioCallUsers() {
        mLoadingRL.start();

        AudioCallController.LoadAllAudioCallDoctors(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                if (object != null) {
                    List<ACDoctor> acDoctors = new ArrayList<>();
                    for (Object data : (List<?>) object) {
                        acDoctors.add((ACDoctor) data);
                    }

                    doctorListAudioCallRecyclerViewAdapter.setAcDoctors(acDoctors);
                    doctorListAudioCallRecyclerViewAdapter.notifyDataSetChanged();

                    if (acDoctors.size() > 0)
                        updateUi(View.VISIBLE, View.GONE);
                    else
                        updateUi(View.GONE, View.VISIBLE);
                } else {
                    updateUi(View.GONE, View.VISIBLE);
                }
            }
        });
    }

    private void updateUi(int i, int i1) {
        audioCallDoctorListDLACRV.setVisibility(i);
        noDataFoundDLACTV.setVisibility(i1);
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RefActivity.updateACActivity(this);
        setContentView(R.layout.activity_doctors_list_audio_call);
        init();
        event();
        loadAudioCallUsers();
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }
}
