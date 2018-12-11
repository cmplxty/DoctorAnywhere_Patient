package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blood;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.Calendar;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.BloodDonorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodDonor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.Validator;

public class CreateBloodDonorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText donorNameDET;
    private EditText donorPhoneDET;
    private EditText donorCityDET;
    private EditText donorAgeDET;
    private EditText donorWeightDET;
    private EditText lastDateOfDonationDET;
    private Spinner donorGenderDSPN;
    private Spinner donorBloodGroupDSPN;
    private DatePickerDialog lastDateOfDonationDatePicker;

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

        lastDateOfDonationDET.setKeyListener(null);
        setupDatePicker();
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
        donor.setAvailable(AFModel.available);
        donor.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return donor;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blood_donor);
        RefActivity.updateACActivity(this);
        init();
        setupToolbar();
        event();
    }

    private void saveDonor() {
        if (validator()) {
            BloodDonorController.SaveBloodDonor(bindDonorData());
            onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveMI: {
                saveDonor();
                break;
            }
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(BloodDonorActivity.instance.get());
        super.onBackPressed();
    }
}
