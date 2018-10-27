package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home;

import android.content.pm.PackageManager;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.AppointmentRequestRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AppointmentController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.FragmentNames;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.APRequest;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AppointmentFragment extends Fragment {

    public static final String TAG = FragmentNames.Appintment;

    // Variables
    private RecyclerView apptRV;
    private TextView noApptTV;

    private AppointmentRequestRecyclerViewAdapter appointmentRecyclerViewAdapter;
    private ICallDoctor iCallDoctor;
    // Variables

    // Methods
    private void init(View view) {
        apptRV = view.findViewById(R.id.apptRV);
        noApptTV = view.findViewById(R.id.noApptTV);

        apptRV.setLayoutManager(new LinearLayoutManager(HomeActivity.instance));
        apptRV.setHasFixedSize(true);

        appointmentRecyclerViewAdapter = new AppointmentRequestRecyclerViewAdapter();
        iCallDoctor = appointmentRecyclerViewAdapter.getiCallDoctor();
    }

    private void loadList() {

        AppointmentController.LoadAppointmentRequests(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                List<APRequest> apRequests = new ArrayList<>();

                if (object != null) {
                    for (Object request : (List<?>) object) {
                        apRequests.add((APRequest) request);
                    }
                }

                appointmentRecyclerViewAdapter.setAppointments(apRequests);
                appointmentRecyclerViewAdapter.notifyDataSetChanged();
                updateUi(apRequests);
            }
        });

        apptRV.setAdapter(appointmentRecyclerViewAdapter);
    }

    private void updateUi(List<APRequest> apRequests) {
        if (apRequests.size() == 0) {
            noApptTV.setVisibility(View.VISIBLE);
            apptRV.setVisibility(View.GONE);
        } else {
            noApptTV.setVisibility(View.GONE);
            apptRV.setVisibility(View.VISIBLE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_CALL_CODE_AP) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iCallDoctor.call();
            } else {
                Toast.makeText(RefActivity.refACActivity.get(), ErrorText.PermissionNeedToCallDoctor, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
