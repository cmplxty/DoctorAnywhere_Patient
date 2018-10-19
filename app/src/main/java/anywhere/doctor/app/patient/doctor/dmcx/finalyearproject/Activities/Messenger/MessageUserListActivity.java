package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.MessageUserRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.MessageController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.MessageUser;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class MessageUserListActivity extends AppCompatActivity {

    // Variables
    public static MessageUserListActivity instance;

    private RecyclerView messagesHFMRV;
    private RotateLoading mLoadingRL;

    private MessageUserRecyclerViewAdapter messageUserRecyclerViewAdapter;
    // Variables

    // Methods
    private void init() {
        mLoadingRL = findViewById(R.id.mLoadingRL);

        messageUserRecyclerViewAdapter = new MessageUserRecyclerViewAdapter();
        messagesHFMRV = findViewById(R.id.messagesHFMRV);
        messagesHFMRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        messagesHFMRV.setHasFixedSize(true);
        messagesHFMRV.setAdapter(messageUserRecyclerViewAdapter);
    }

    private void loadData() {
        mLoadingRL.start();

        MessageController.LoadMessgeUsersList(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                if (object instanceof String) {
                    String errorCode = (String) object;
                    Toast.makeText(RefActivity.refACActivity.get(), errorCode, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<MessageUser> messageUsers = new ArrayList<>();
                for (Object data : (List<?>) object) {
                    if (data instanceof MessageUser)
                        messageUsers.add((MessageUser) data);
                }

                messageUserRecyclerViewAdapter.setMessageUsers(messageUsers);
                messageUserRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        RefActivity.updateACActivity(this);
        instance = (MessageUserListActivity) RefActivity.refACActivity.get();

        init();
        loadData();
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance);
        super.onBackPressed();
    }
}
