package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.DoctorsRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.DoctorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.FragmentNames;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class DoctorsFragment extends Fragment {

    public static final String TAG = FragmentNames.Doctor;

    // Variables
    private RecyclerView doctorsRV;

    private DoctorsRecyclerViewAdapter doctorsRecyclerViewAdapter;
    private List<Doctor> doctors;
    // Variables

    // Methods
    private void init(View view) {
        doctorsRV = view.findViewById(R.id.doctorsRV);

        doctorsRV.setLayoutManager(new LinearLayoutManager(HomeActivity.instance));
        doctorsRV.setHasFixedSize(true);

        doctors = new ArrayList<>();
    }

    private void loadList() {
        DoctorController.LoadDoctors(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                doctors = (List<Doctor>) object;
                doctorsRecyclerViewAdapter = new DoctorsRecyclerViewAdapter(RefActivity.refACActivity.get(), doctors);
                doctorsRV.setAdapter(doctorsRecyclerViewAdapter);
            }
        });
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_doctors, container, false);

        // initialize
        init(view);

        // Load doctors list
        loadList();

        return view;
    }
}
