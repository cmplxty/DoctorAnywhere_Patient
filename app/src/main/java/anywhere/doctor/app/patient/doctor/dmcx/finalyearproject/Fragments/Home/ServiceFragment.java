package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger.DoctorsListMessageActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.FragmentNames;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class ServiceFragment extends Fragment {

    // Variables
    private Button messageServiceBTN;
    private Button homeServiceBTN;
    private Button apptBTN;
    private Button audioCallBTN;
    // Variables

    // Methods
    private void init(View view) {
        messageServiceBTN = view.findViewById(R.id.messageServiceBTN);
        apptBTN = view.findViewById(R.id.apptBTN);
        homeServiceBTN = view.findViewById(R.id.homeServiceBTN);
        audioCallBTN = view.findViewById(R.id.audioCallBTN);
    }

    private void event() {
        messageServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.DoctorListMessageActivity();
            }
        });

        homeServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.DoctorListHomeServiceActivity();
            }
        });

        apptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.DoctorListAppointmentActivity();
            }
        });

        audioCallBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.DoctorListAudioCallctivity();
            }
        });
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_service, container, false);

        // Initialize
        init(view);

        // Event
        event();

        return view;
    }
}
