package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.AuthActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AuthController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.ProfileController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Patient;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.FragmentNames;

public class ProfileFragment extends Fragment {

    public static final String TAG = FragmentNames.Profile;

    // Variables
    private ImageView patientPaymentIV;

    private TextView patientNameTV;
    private TextView patientEmailTV;
    private TextView patientPhoneTV;
    private TextView patientAddressTV;
    private TextView patientGenderTV;
    private TextView patientDOBTV;
    private TextView patientCountryTV;

    private Button helpBTN;
    private Button signOutBTN;

    // Variables

    // Methods
    private void init(View view) {
        patientPaymentIV = view.findViewById(R.id.patientPaymentIV);

        patientNameTV = view.findViewById(R.id.patientNameTV);
        patientEmailTV = view.findViewById(R.id.patientEmailTV);
        patientPhoneTV = view.findViewById(R.id.patientPhoneTV);
        patientAddressTV = view.findViewById(R.id.patientAddressTV);
        patientGenderTV = view.findViewById(R.id.patientGenderTV);
        patientDOBTV = view.findViewById(R.id.patientDOBTV);
        patientCountryTV = view.findViewById(R.id.patientCountryTV);
        helpBTN = view.findViewById(R.id.helpBTN);
        signOutBTN = view.findViewById(R.id.signOutBTN);
    }

    private void event() {
        patientPaymentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(HomeActivity.instance).inflate(R.layout.dialog_layout_payment_edit, null);

                Button closeBTN = dialogView.findViewById(R.id.closeBTN);

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.instance);
                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                closeBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

        signOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthController.SignOut();
                startActivity(new Intent(RefActivity.refACActivity.get(), AuthActivity.class));
                RefActivity.refACActivity.get().finish();
            }
        });
    }

    private void loadProfile() {
        ProfileController.CheckForProfileData(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object instanceof String) {
                    Toast.makeText(RefActivity.refACActivity.get(), (String) object, Toast.LENGTH_SHORT).show();
                } else if (object instanceof Patient) {
                    Patient patient = (Patient) object;
                    patientNameTV.setText(patient.getName());
                    patientEmailTV.setText(patient.getEmail());

                    patientPhoneTV.setText(patient.getPhone() == null || patient.getPhone().equals(AFModel.deflt) ? "Update Your Phone No." : patient.getPhone());
                    patientAddressTV.setText(patient.getAddress() == null || patient.getAddress().equals(AFModel.deflt) ? "Update Your Address" : patient.getPhone());
                    patientGenderTV.setText(patient.getGender() == null || patient.getGender().equals(AFModel.deflt) ? "Update Your Gender" : patient.getPhone());
                    patientDOBTV.setText(patient.getDob() == null || patient.getDob().equals(AFModel.deflt) ? "Update Your DOB" : patient.getPhone());
                    patientCountryTV.setText(patient.getCountry() == null || patient.getCountry().equals(AFModel.deflt) ? "Update Your Country" : patient.getPhone());
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
        event();
        loadProfile();

        return view;
    }
}
