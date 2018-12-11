package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.ProfileController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Patient;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    // Variables
    private CircleImageView patientProfilePicCIV;
    private TextView patientNameTV;
    private TextView patientEmailTV;
    private TextView patientPhoneTV;
    private TextView patientAddressTV;
    private TextView patientGenderTV;
    private TextView patientDOBTV;
    private TextView patientCountryTV;

    private Patient patient;
    // Variables

    // Methods
    private void init(View view) {
        patientProfilePicCIV = view.findViewById(R.id.patientProfilePicCIV);
        patientNameTV = view.findViewById(R.id.patientNameTV);
        patientEmailTV = view.findViewById(R.id.patientEmailTV);
        patientPhoneTV = view.findViewById(R.id.patientPhoneTV);
        patientAddressTV = view.findViewById(R.id.patientAddressTV);
        patientGenderTV = view.findViewById(R.id.patientGenderTV);
        patientDOBTV = view.findViewById(R.id.patientDOBTV);
        patientCountryTV = view.findViewById(R.id.patientCountryTV);
    }

    private void loadProfile() {
        ProfileController.CheckForProfileData(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object instanceof String) {
                    Toast.makeText(RefActivity.refACActivity.get(), (String) object, Toast.LENGTH_SHORT).show();
                } else if (object instanceof Patient) {
                    patient = (Patient) object;
                    Picasso.with(RefActivity.refACActivity.get()).load(patient.getLink()).placeholder(R.drawable.noperson).into(patientProfilePicCIV);
                    patientNameTV.setText(patient.getName());
                    patientEmailTV.setText(patient.getEmail());
                    patientPhoneTV.setText(patient.getPhone() == null || patient.getPhone().equals(AFModel.deflt) ? "Update Your Phone No." : patient.getPhone());
                    patientAddressTV.setText(patient.getAddress() == null || patient.getAddress().equals(AFModel.deflt) ? "Update Your Address" : patient.getAddress());
                    patientGenderTV.setText(patient.getGender() == null || patient.getGender().equals(AFModel.deflt) ? "Update Your Gender" : patient.getGender());
                    patientDOBTV.setText(patient.getDob() == null || patient.getDob().equals(AFModel.deflt) ? "Update Your DOB" : patient.getDob());
                    patientCountryTV.setText(patient.getCountry() == null || patient.getCountry().equals(AFModel.deflt) ? "Update Your Country" : patient.getCountry());
                }
            }
        });
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_profile, container, false);
        init(view);
        loadProfile();
        return view;
    }
}
