package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blood;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NavigationRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.BloodDonorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodDonor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.Validator;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class MyBloodDonorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText donorNameDET;
    private EditText donorPhoneDET;
    private EditText donorCityDET;
    private EditText donorAgeDET;
    private EditText donorWeightDET;
    private EditText lastDateOfDonationDET;
    private Spinner donorGenderDSPN;
    private Spinner donorBloodGroupDSPN;
    private Switch donorAvailableDSWH;
    private DatePickerDialog lastDateOfDonationDatePicker;

    private BloodDonor bloodDonor;

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        donorNameDET = findViewById(R.id.donorNameDET);
        donorPhoneDET = findViewById(R.id.donorPhoneDET);
        donorCityDET = findViewById(R.id.donorCityDET);
        donorAgeDET = findViewById(R.id.donorAgeDET);
        donorWeightDET = findViewById(R.id.donorWeightDET);
        lastDateOfDonationDET = findViewById(R.id.lastDateOfDonationDET);
        donorGenderDSPN = findViewById(R.id.donorGenderDSPN);
        donorBloodGroupDSPN = findViewById(R.id.donorBloodGroupDSPN);
        donorAvailableDSWH = findViewById(R.id.donorAvailableDSWH);

        bloodDonor = getIntent().getParcelableExtra(Vars.Connector.MY_BLOOD_DONOR_ACTIVITY_DATA);
        lastDateOfDonationDET.setKeyListener(null);

        setupDatePicker();
        setDonorData(bloodDonor);
    }

    private void setupDatePicker() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        lastDateOfDonationDatePicker = new DatePickerDialog(
                RefActivity.refACActivity.get(),
                R.style.AppTheme_DateTimePicker,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = String.valueOf(day) + "-" + month + "-" + year;;
                        if (month >= 1 && month <= 9)
                            date = String.valueOf(day) + "-0" + month + "-" + year;

                        lastDateOfDonationDET.setText(date);
                    }
                }, year, month, day);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void event() {
        lastDateOfDonationDET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                lastDateOfDonationDatePicker.show();
                return true;
            }
        });
    }

    private void saveDonorDetail() {
        if (validator()) {
            BloodDonorController.SaveBloodDonor(bindDonorData());
            onBackPressed();
        }
    }

    private void deleteDonor() {
        removeBloodDonorDialog();
    }

    private boolean validator() {
        String name = donorNameDET.getText().toString();
        String phone = donorPhoneDET.getText().toString();
        String city = donorCityDET.getText().toString();
        String group = donorBloodGroupDSPN.getSelectedItem().toString();
        String gender = donorGenderDSPN.getSelectedItem().toString();
        String age = donorAgeDET.getText().toString();
        String weight = donorWeightDET.getText().toString();
        String lastDateOfDonation = lastDateOfDonationDET.getText().toString();

        if (
                Validator.empty(name) ||
                Validator.empty(phone) ||
                Validator.empty(city) ||
                Validator.empty(group) ||
                Validator.empty(gender) ||
                Validator.empty(age) ||
                Validator.empty(weight) ||
                Validator.empty(lastDateOfDonation)
                ) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AllFieldsAreRequeired, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            int ageInt = Integer.valueOf(age);
            int weightInt = Integer.valueOf(weight);

            if (ageInt < 16) {
                Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AgeValidity, Toast.LENGTH_SHORT).show();
                return false;
            } else if (weightInt < 50) {
                Toast.makeText(RefActivity.refACActivity.get(), ValidationText.WeightValidity, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void setDonorData(BloodDonor bloodDonor) {
        if (bloodDonor != null) {
            donorNameDET.setText(bloodDonor.getName());
            donorPhoneDET.setText(bloodDonor.getPhone());
            donorCityDET.setText(bloodDonor.getCity());
            donorAgeDET.setText(bloodDonor.getAge());
            donorWeightDET.setText(bloodDonor.getWeight());
            lastDateOfDonationDET.setText(bloodDonor.getLast_donation_date());
            donorAvailableDSWH.setChecked(bloodDonor.getAvailable().equals(AFModel.available));

            List<String> group = Arrays.asList(getResources().getStringArray(R.array.blood_group_array));
            donorBloodGroupDSPN.setSelection(group.indexOf(bloodDonor.getGroup()));

            List<String> gender = Arrays.asList(getResources().getStringArray(R.array.gender_array));
            donorGenderDSPN.setSelection(gender.indexOf(bloodDonor.getGender()));
        }
    }

    private BloodDonor bindDonorData() {
        BloodDonor donor = new BloodDonor();
        donor.setName(donorNameDET.getText().toString());
        donor.setPhone(donorPhoneDET.getText().toString());
        donor.setCity(donorCityDET.getText().toString());
        donor.setGroup(donorBloodGroupDSPN.getSelectedItem().toString());
        donor.setGender(donorGenderDSPN.getSelectedItem().toString());
        donor.setAge(donorAgeDET.getText().toString());
        donor.setWeight(donorWeightDET.getText().toString());
        donor.setLast_donation_date(lastDateOfDonationDET.getText().toString());
        donor.setAvailable(donorAvailableDSWH.isChecked() ? AFModel.available : AFModel.not_available);
        donor.setTimestamp(bloodDonor.getTimestamp());
        return donor;
    }

    private void removeBloodDonorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
        builder.setTitle("Notice");
        builder.setMessage("Are you sure? you want to delete your account.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                BloodDonorController.DeleteCurrentDonorData();
                onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1EC8C8"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1EC8C8"));
            }
        });

        dialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_blood_donor);
        RefActivity.updateACActivity(this);
        init();
        event();
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_blood_donor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.deleteMBDMI: {
                deleteDonor();
                break;
            }
            case R.id.saveMBDMI: {
                saveDonorDetail();
                break;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(BloodDonorActivity.instance.get());
        super.onBackPressed();
    }
}
