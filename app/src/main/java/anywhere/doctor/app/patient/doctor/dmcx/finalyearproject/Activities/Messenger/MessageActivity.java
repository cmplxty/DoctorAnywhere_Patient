package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.AudioCall.AudioCallHistoryActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.MessageRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.DoctorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.MessageController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Message;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class MessageActivity extends AppCompatActivity {

    // Variables
    private final int GALLARY_INTENT_REQUEST_CODE = 9991;
    private final int ACCESS_GALLARY_PERMISSION_CODE = 9992;

    private Toolbar toolbar;
    private RecyclerView messagesAMRV;
    private EditText messageAreaAMET;
    private ImageButton cameraAMIB;
    private FloatingActionButton sendAMFAB;

    private MessageRecyclerViewAdapter messageRecyclerViewAdapter;
    private Doctor doctor;
    private String fromActivity;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        messageAreaAMET = findViewById(R.id.messageAreaAMET);
        cameraAMIB = findViewById(R.id.cameraAMIB);
        sendAMFAB = findViewById(R.id.sendAMFAB);

        messageRecyclerViewAdapter = new MessageRecyclerViewAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(RefActivity.refACActivity.get());
        messagesAMRV = findViewById(R.id.messagesAMRV);
        messagesAMRV.setLayoutManager(manager);
        messagesAMRV.setHasFixedSize(true);
        messagesAMRV.setAdapter(messageRecyclerViewAdapter);

        doctor = getIntent().getParcelableExtra(Vars.Connector.MESSAGE_ACTIVITY_DATA);
        fromActivity = getIntent().getStringExtra(Vars.ActivityOverrider.OPEN_MESSAGE_ACTIVITY);
        MessageController.UpdateMessageNotViewedToViewedMessage();

        if (doctor.checkEmptyValues()) {
            DoctorController.LoadDoctor(doctor.getId(), new IAction() {
                @Override
                public void onCompleteAction(Object object) {
                    if (object != null)
                        doctor = (Doctor) object;
                }
            });
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(doctor.getName());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void event() {
        sendAMFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getMessage().equals("")) {
                    MessageController.sendMessageText(getMessage(), doctor.getId());
                    resetMessage();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.EnterSomething, Toast.LENGTH_SHORT).show();
                }
            }
        });

        cameraAMIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, GALLARY_INTENT_REQUEST_CODE);
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.PermissionNotGranted, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA};

        if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), permissions[0]) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), permissions[1]) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), permissions[1]) != PackageManager.PERMISSION_GRANTED
                ) {

            ActivityCompat.requestPermissions(RefActivity.refACActivity.get(), permissions, ACCESS_GALLARY_PERMISSION_CODE);
            return false;
        }

        return true;
    }

    /*
    * Get Message
    * */
    private String getMessage() {
        return messageAreaAMET.getText().toString();
    }

    /*
    * Reset Message
    * */
    private void resetMessage() {
        messageAreaAMET.setText("");
    }

    /*
    * Load Messages List
    * */
    private void loadList() {
        MessageController.showMessages(doctor.getId(), new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object instanceof String) {
                    String errorCode = (String) object;
                    Log.d("NO MESSAGE", "onCompleteAction: " + errorCode);
                } else {
                    List<Message> messages = new ArrayList<>();

                    if (object != null) {
                        for (Object message : (List<?>) object)
                            messages.add((Message) message);
                    }

                    messageRecyclerViewAdapter.setMessages(messages);
                    messageRecyclerViewAdapter.notifyDataSetChanged();
                    messagesAMRV.scrollToPosition(messages.size() - 1);
                }
            }
        });
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        RefActivity.updateACActivity(this);
        init();
        setupToolbar();
        event();
        loadList();
        checkPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (checkPermission()) {
            if (requestCode == GALLARY_INTENT_REQUEST_CODE && resultCode == RESULT_OK) {
                Uri uri = data.getData();
                MessageController.sendMessageImage(uri, doctor.getId());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        switch (fromActivity) {
            case Vars.ActivityOverrider.FROM_HOME_ACTIVITY:
                RefActivity.updateACActivity(HomeActivity.instance.get());
                break;
            case Vars.ActivityOverrider.FROM_MESSAGE_USER_LIST_ACTIVITY:
                RefActivity.updateACActivity(MessageUserListActivity.instance);
                break;
            case Vars.ActivityOverrider.FROM_DOCTOR_LIST_ACTIVITY:
                RefActivity.updateACActivity(DoctorsListMessageActivity.instance.get());
                break;
            case Vars.ActivityOverrider.FROM_AUDIO_CALL_HISTORY:
                RefActivity.updateACActivity(AudioCallHistoryActivity.instance.get());
                break;
            default:
                RefActivity.updateACActivity(HomeActivity.instance.get());
                break;
        }

        super.onBackPressed();
    }
}
