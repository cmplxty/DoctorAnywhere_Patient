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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.AppointmentRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.FragmentNames;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Appointment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class AppointmentFragment extends Fragment {

    public static final String TAG = FragmentNames.Appintment;

    // Variables
    private RecyclerView apptRV;
    private TextView noApptTV;

    private AppointmentRecyclerViewAdapter appointmentRecyclerViewAdapter;
    private List<Appointment> appointments;
    // Variables

    // Methods
    private void init(View view) {
        apptRV = view.findViewById(R.id.apptRV);
        noApptTV = view.findViewById(R.id.noApptTV);

        apptRV.setLayoutManager(new LinearLayoutManager(HomeActivity.instance));
        apptRV.setHasFixedSize(true);

        appointments = new ArrayList<>();
    }

    private void loadList() {
        boolean isSetAdapter = false;
        appointments.add(new Appointment("Sadman", "Dr. Nurul Hoque", "East Razabazar, Dhaka", "02.08.2018 - 6:30 PM", "5:00 PM"));
        appointments.add(new Appointment("Sadman", "Dr. Nurul Hoque", "East Razabazar, Dhaka", "02.08.2018 - 6:30 PM", "5:00 PM"));
        appointments.add(new Appointment("Sadman", "Dr. Nurul Hoque", "East Razabazar, Dhaka", "02.08.2018 - 6:30 PM", "5:00 PM"));
        appointments.add(new Appointment("Sadman", "Dr. Nurul Hoque", "East Razabazar, Dhaka", "02.08.2018 - 6:30 PM", "5:00 PM"));
        appointments.add(new Appointment("Sadman", "Dr. Nurul Hoque", "East Razabazar, Dhaka", "02.08.2018 - 6:30 PM", "5:00 PM"));

        if (appointments.size() == 0) {
            noApptTV.setVisibility(View.VISIBLE);
            apptRV.setVisibility(View.GONE);
        } else {
            noApptTV.setVisibility(View.GONE);
            apptRV.setVisibility(View.VISIBLE);
            isSetAdapter = true;
        }

        if (isSetAdapter) {
            appointmentRecyclerViewAdapter = new AppointmentRecyclerViewAdapter(HomeActivity.instance, appointments);
            apptRV.setAdapter(appointmentRecyclerViewAdapter);
        }
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_appointment, container, false);

        // initialize
        init(view);

        // Load doctors list
        loadList();


        return view;
    }
}
