package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.DoctorsRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.DoctorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.FragmentNames;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class DoctorsFragment extends Fragment {

    // Variables
    private final int WAIT_TIME_DOCTOR_LIST_LOAD = 500;
    private final int WAIT_TIME_END_LOADING = 10000;
    private final int WAIT_TIME_UPDATE_DOCTOR_LIST = 300;

    private RecyclerView doctorsRV;
    private RotateLoading mLoadingRL;

    private DoctorsRecyclerViewAdapter doctorsRecyclerViewAdapter;
    private List<Doctor> doctors;
    private ModelHandler modelHandler = new ModelHandler();
    // Variables

    // Class
    private static class ModelHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class DoctorListLoadRunnable implements Runnable {
        @Override
        public void run() {
            loadDoctors();
        }
    }

    private class EndLoadingRunnable implements Runnable {
        @Override
        public void run() {
            stopLoader();
        }
    }

    private class UpdateDoctorListRunnable implements Runnable {
        @Override
        public void run() {
            updateDoctorsList();
        }
    }
    // Class

    // Methods
    private void init(View view) {
        doctorsRV = view.findViewById(R.id.doctorsRV);
        mLoadingRL = view.findViewById(R.id.mLoadingRL);

        doctorsRecyclerViewAdapter = new DoctorsRecyclerViewAdapter();
        doctorsRV.setLayoutManager(new LinearLayoutManager(HomeActivity.instance.get()));
        doctorsRV.setHasFixedSize(true);
        doctorsRV.setAdapter(doctorsRecyclerViewAdapter);

        doctors = new ArrayList<>();
    }

    private void task() {
        modelHandler.postDelayed(new DoctorListLoadRunnable(), WAIT_TIME_DOCTOR_LIST_LOAD);
        modelHandler.postDelayed(new EndLoadingRunnable(), WAIT_TIME_END_LOADING);
    }

    private void loadDoctors() {
        mLoadingRL.start();

        DoctorController.LoadDoctors(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                doctors = new ArrayList<>();
                if (object != null) {
                    for (Object doctor : (List<?>) object) {
                        doctors.add((Doctor) doctor);
                    }
                }

                modelHandler.postDelayed(new UpdateDoctorListRunnable(), WAIT_TIME_UPDATE_DOCTOR_LIST);
            }
        });
    }

    private void stopLoader() {
        if (mLoadingRL.isStart())
            mLoadingRL.stop();
    }

    private void updateDoctorsList() {
        doctorsRecyclerViewAdapter.setDoctors(doctors);
        doctorsRecyclerViewAdapter.notifyDataSetChanged();
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_doctors, container, false);
        init(view);
        task();
        return view;
    }
}
