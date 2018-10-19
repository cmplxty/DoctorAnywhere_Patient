package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AppFirebase;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AuthController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AuthActivity extends AppCompatActivity {

    // Variables
    private ConstraintLayout signInCL, signUpCL;
    private TextView switchToSignUpTV;
    private Button signInBTN, signUpBTN;
    private EditText nameSuET;
    private EditText emailSuET;
    private EditText passwordSuET;
    private EditText emailSiET;
    private EditText passwordSiET;
    private TextView forgetPasswordTV;

    private enum Page { SIGNUP, SIGNIN }
    private Page currentPage;
    // Variables

    // Methods
    private void init() {
        signInCL = findViewById(R.id.signInCL);
        signUpCL = findViewById(R.id.signUpCL);
        switchToSignUpTV = findViewById(R.id.switchToSignUpTV);
        signInBTN = findViewById(R.id.signInBTN);
        signUpBTN = findViewById(R.id.signUpBTN);
        nameSuET = findViewById(R.id.nameSuET);
        emailSuET = findViewById(R.id.emailSuET);
        passwordSuET = findViewById(R.id.passwordSuET);
        emailSiET = findViewById(R.id.emailSiET);
        passwordSiET = findViewById(R.id.passwordSiET);
        forgetPasswordTV = findViewById(R.id.forgetPasswordTV);

        signUpCL.setVisibility(View.INVISIBLE);

        // Firabase instance
        Vars.appFirebase = new AppFirebase();
    }

    /*
    * Switch between two states
    * */
    private void switchPages(View show, View hide, Page current) {
        hide.setVisibility(View.GONE);
        show.setVisibility(View.VISIBLE);
        currentPage = current;
    }

    /*
    * Organize all click events
    * */
    private void ClickEventListener() {
        // sign up text action
        switchToSignUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPages(signUpCL, signInCL, Page.SIGNUP);
            }
        });

        // sign in action
        signInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailSiET.getText().toString();
                final String password = passwordSiET.getText().toString();

                AuthController.SignIn(email, password, new IAction() {
                    @Override
                    public void onCompleteAction(Object object) {
                        if (object instanceof Boolean) {
                            Boolean isSuccessful = (Boolean) object;
                            if (isSuccessful) {
                                ActivityTrigger.HomeActivity();
                            } else {
                                Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AuthenticationFailed, Toast.LENGTH_SHORT).show();
                            }
                        } else if (object instanceof String) {
                            String errorCode = (String) object;
                            Toast.makeText(RefActivity.refACActivity.get(), errorCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // sign up action
        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameSuET.getText().toString();
                final String email = emailSuET.getText().toString();
                final String password = passwordSuET.getText().toString();

                AuthController.SignUp(name, email, password, new IAction() {
                    @Override
                    public void onCompleteAction(Object object) {
                        Boolean isSuccessful = (Boolean) object;
                        if (isSuccessful) {
                            onSignUp(email, password);
                        }
                    }
                });
            }
        });
    }

    /*
    * On sign up action
    * */
    private void onSignUp(String email, String password) {
        emailSiET.setText(email);
        passwordSiET.setText(password);
        switchPages(signInCL, signUpCL, Page.SIGNUP);
    }
    // Methods

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        RefActivity.updateACActivity(this);

        init();
        ClickEventListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Vars.appFirebase.getCurrentUser() != null) {
            ActivityTrigger.HomeActivity();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentPage == Page.SIGNUP) {
            switchPages(signInCL, signUpCL, Page.SIGNIN);
        } else {
            super.onBackPressed();
        }
    }
}
