package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PrivateKey;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AppFirebase;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AuthController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AuthActivity extends AppCompatActivity {

    // Variables
    private ConstraintLayout signInCL, signUpCL;
    private TextView switchToSignUpTV;
    private TextView switchToSignInTV;
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

    // Class
    public class AuthDialog {
        public void create() {
            int px16 = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
            int px14 = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 0, 0);

            final EditText emailET = new EditText(RefActivity.refACActivity.get());
            emailET.setHint("Enter your email...");
            emailET.setPadding(px16+px14, px16, px16+px14, px16);
            emailET.setLayoutParams(params);
            emailET.setBackground(getResources().getDrawable(R.drawable.edit_text_clear_bg, null));

            AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
            builder.setTitle("Enter your email");
            builder.setView(emailET);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    String email = emailET.getText().toString();
                    if (!email.equals(""))
                        AuthController.ForgetPassword(email.trim());
                    else
                        Toast.makeText(RefActivity.refACActivity.get(), ValidationText.EnterEmail, Toast.LENGTH_SHORT).show();
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
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1EC8C8"));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1EC8C8"));
                }
            });
            dialog.show();

        }
    }

    // Methods
    private void init() {
        signInCL = findViewById(R.id.signInCL);
        signUpCL = findViewById(R.id.signUpCL);
        switchToSignUpTV = findViewById(R.id.switchToSignUpTV);
        switchToSignInTV = findViewById(R.id.switchToSignInTV);
        signInBTN = findViewById(R.id.signInBTN);
        signUpBTN = findViewById(R.id.signUpBTN);
        nameSuET = findViewById(R.id.nameSuET);
        emailSuET = findViewById(R.id.emailSuET);
        passwordSuET = findViewById(R.id.passwordSuET);
        emailSiET = findViewById(R.id.emailSiET);
        passwordSiET = findViewById(R.id.passwordSiET);
        forgetPasswordTV = findViewById(R.id.forgetPasswordTV);

        signUpCL.setVisibility(View.INVISIBLE);
        Vars.appFirebase = AppFirebase.Instance();
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
        // switch sign up
        switchToSignUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPages(signUpCL, signInCL, Page.SIGNUP);
            }
        });

        // switch sign in
        switchToSignInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPages(signInCL, signUpCL, Page.SIGNIN);
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

        // forget password
        forgetPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthDialog dialog = new AuthDialog();
                dialog.create();;
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
