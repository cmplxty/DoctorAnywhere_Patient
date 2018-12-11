package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blood;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.BloodDonorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodPost;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.Validator;

public class NewBloodPostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText bloodPostNameBPET;
    private EditText bloodPostPhoneBPET;
    private EditText bloodPostCityBPET;
    private Spinner bloodPostGroupBPSPN;
    private EditText bloodPostDescriptionBPET;

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        bloodPostNameBPET = findViewById(R.id.bloodPostNameBPET);
        bloodPostPhoneBPET = findViewById(R.id.bloodPostPhoneBPET);
        bloodPostCityBPET = findViewById(R.id.bloodPostCityBPET);
        bloodPostGroupBPSPN = findViewById(R.id.bloodPostGroupBPSPN);
        bloodPostDescriptionBPET = findViewById(R.id.bloodPostDescriptionBPET);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private boolean validate() {
        String name = bloodPostNameBPET.getText().toString();
        String phone = bloodPostPhoneBPET.getText().toString();
        String city = bloodPostCityBPET.getText().toString();
        String desc = bloodPostDescriptionBPET.getText().toString();

        if (
                Validator.empty(name) ||
                Validator.empty(phone) ||
                Validator.empty(city) ||
                Validator.empty(desc)
                ) {

            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AllFieldsAreRequeired, Toast.LENGTH_LONG).show();
            return false;
        } else if (desc.length() < 10) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.Describe20OrMoreChars, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private BloodPost bindData() {
        BloodPost post = new BloodPost();
        post.setName(bloodPostNameBPET.getText().toString());
        post.setCity(bloodPostCityBPET.getText().toString());
        post.setDescription(bloodPostDescriptionBPET.getText().toString());
        post.setGroup(bloodPostGroupBPSPN.getSelectedItem().toString());
        post.setPhone(bloodPostPhoneBPET.getText().toString());
        post.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return post;
    }

    private void sendPost() {
        if (validate()) {
            BloodDonorController.NewBloodPost(bindData());
            onBackPressed();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_blood_post);
        init();
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sendMI: {
                sendPost();
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
        super.onBackPressed();
    }
}
