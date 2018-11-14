package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.ProfileController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Patient;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditFragment extends Fragment {

    // Variables
    private CircleImageView profileImagePECIV;
    private FloatingActionButton changeProfileImagePEFAB;
    private EditText profileNamePEET;
    private EditText profileAgePEET;
    private EditText profilePhonePEET;
    private EditText profileAddressPEET;
    private EditText profileDobPEET;
    private EditText profileCountryPEET;
    private Spinner profileGenderPESP;
    private Button savePEBTN;

    private Patient patient;
    private Uri profileImageUri = null;
    // Variables

    // Methods
    private void init(View view) {
        profileImagePECIV = view.findViewById(R.id.profileImagePECIV);
        changeProfileImagePEFAB = view.findViewById(R.id.changeProfileImagePEFAB);
        profileNamePEET = view.findViewById(R.id.profileNamePEET);
        profileAgePEET = view.findViewById(R.id.profileAgePEET);
        profilePhonePEET = view.findViewById(R.id.profilePhonePEET);
        profileAddressPEET = view.findViewById(R.id.profileAddressPEET);
        profileDobPEET = view.findViewById(R.id.profileDobPEET);
        profileCountryPEET = view.findViewById(R.id.profileCountryPEET);
        profileGenderPESP = view.findViewById(R.id.profileGenderPESP);
        savePEBTN = view.findViewById(R.id.savePEBTN);

        if (getArguments() != null) {
            patient = getArguments().getParcelable(Vars.Connector.PROFILE_EDIT_FRAGMENT_DATA);
            if (patient != null) {
                updateUi();
            }
        }
    }

    private void event() {
        changeProfileImagePEFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    cropImager();
                }
            }
        });

        savePEBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = profileNamePEET.getText().toString();
                String age = profileAgePEET.getText().toString();
                String phone = profilePhonePEET.getText().toString();
                String address = profileAddressPEET.getText().toString();
                String dob = profileDobPEET.getText().toString();
                String country = profileCountryPEET.getText().toString();
                String gender = profileGenderPESP.getSelectedItem().toString();
                String link = patient.getLink();

                ProfileController.UpdateProfileDetail(name, patient.getEmail(), age, phone, address, dob, country, gender, link, profileImageUri);
            }
        });
    }

    private void cropImager() {
        CropImage.activity()
                .setAspectRatio(1, 1)
                .setMaxCropResultSize(1500, 1500)
                .start(RefActivity.refACActivity.get(), this);
    }

    private void updateUi() {
        Picasso.with(RefActivity.refACActivity.get()).load(patient.getLink()).placeholder(R.drawable.noperson).into(profileImagePECIV);
        profileNamePEET.setText(patient.getName());
        profileAgePEET.setText(patient.getAge() == null || patient.getAge().equals(AFModel.deflt) ? "" : patient.getAge());
        profilePhonePEET.setText(patient.getPhone() == null || patient.getPhone().equals(AFModel.deflt) ? "" : patient.getPhone());
        profileAddressPEET.setText(patient.getAddress() == null || patient.getAddress().equals(AFModel.deflt) ? "" : patient.getAddress());
        profileDobPEET.setText(patient.getDob() == null || patient.getDob().equals(AFModel.deflt) ? "" : patient.getDob());
        profileCountryPEET.setText(patient.getCountry() == null || patient.getCountry().equals(AFModel.deflt) ? "" : patient.getCountry());

        String[] genderArray = getResources().getStringArray(R.array.gender_array);
        if (patient.getGender() != null && patient.getGender().equals(genderArray[1])) {
            profileGenderPESP.setSelection(1);
        } else {
            profileGenderPESP.setSelection(0);
        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA }, Vars.RequestCode.REQUEST_ACCESS_IMAGE_CODE_PEF);
            return false;
        }

        return true;
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_profile_edit, container, false);
        init(view);
        event();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (result != null) {
                profileImageUri = result.getUri();
                profileImagePECIV.setImageURI(profileImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                if (result != null) {
                    Exception error = result.getError();
                    Toast.makeText(RefActivity.refACActivity.get(), "Error! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_ACCESS_IMAGE_CODE_PEF) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
                builder.setTitle("Permission")
                        .setCancelable(false)
                        .setMessage("Need permission to get the image from the storage. We don't access any of your private data. Be safe stay safe.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkPermission();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                cropImager();
            }
        }
    }
}
