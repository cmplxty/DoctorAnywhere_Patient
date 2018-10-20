package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.FragmentNames;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingText;

public class DashboardFragment extends Fragment {

    // Variables
    public static final String TAG = FragmentNames.Dashboard;

    private ConstraintLayout messagesPageCL;
    private ConstraintLayout prescriptionPageCL;
    private ConstraintLayout blogPageCL;
    private ConstraintLayout bloodPageCL;
    private ConstraintLayout nursePageCL;
    private ConstraintLayout helpPageCL;
    private ConstraintLayout homeServicePageCL;
    // Variables

    // Methods
    private void init(View view) {
        messagesPageCL = view.findViewById(R.id.messagesPageCL);
        prescriptionPageCL = view.findViewById(R.id.prescriptionPageCL);
        blogPageCL = view.findViewById(R.id.blogPageCL);
        bloodPageCL = view.findViewById(R.id.bloodPageCL);
        helpPageCL = view.findViewById(R.id.helpPageCL);
        nursePageCL = view.findViewById(R.id.nursePageCL);
        homeServicePageCL = view.findViewById(R.id.homeServicePageCL);
    }

    private void event() {
        messagesPageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.MessageListActivity();
            }
        });

        prescriptionPageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.PrescriptionListActivity();
            }
        });

        nursePageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.NurseActivity();
            }
        });

        homeServicePageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.HomeServiceListActivity();
            }
        });
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_dashboard, container, false);

        // Initialize
        init(view);

        // Event
        event();

        return view;
    }


}
