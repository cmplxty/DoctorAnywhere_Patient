package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.MessageController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.MessageUser;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Patient;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class MessageUserRecyclerViewAdapter extends RecyclerView.Adapter<MessageUserRecyclerViewAdapter.MessageUserRecyclerViewHolder> {

    private List<MessageUser> messageUsers = new ArrayList<>();

    public void setMessageUsers(List<MessageUser> messageUsers) {
        this.messageUsers = messageUsers;
    }

    @NonNull
    @Override
    public MessageUserRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_message_list_item, parent, false);
        return new MessageUserRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageUserRecyclerViewHolder holder, int position) {

        final int itemPosition = position;
        final MessageUserRecyclerViewHolder viewHolder = holder;

        MessageController.LoadMessageUserContent(messageUsers.get(itemPosition).getDoctor(), new IAction() {
            @Override
            public void onCompleteAction(Object object) {

                if (object instanceof Doctor) {
                    final Doctor doctor = (Doctor) object;
                    Picasso.with(RefActivity.refACActivity.get())
                            .load(doctor.getImage_link() == null ? AFModel.deflt : doctor.getImage_link())
                            .placeholder(R.drawable.noperson)
                            .into(viewHolder.userImageIV);

                    viewHolder.fromTV.setText(doctor.getName());
                    switch (messageUsers.get(itemPosition).getType()) {
                        case AFModel.text:
                            viewHolder.messageContentTV.setText(messageUsers.get(itemPosition).getContent());
                            break;
                        case AFModel.image:
                            viewHolder.messageContentTV.setText(new StringBuilder("Photo"));
                            break;
                        case AFModel.prescription:
                            viewHolder.messageContentTV.setText(new StringBuilder("Prescription"));
                            break;
                    }
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityTrigger.MessageActivity(doctor, Vars.ActivityOverrider.FROM_MESSAGE_USER_LIST_ACTIVITY);
                        }
                    });

                } else {
                    String errorCode = String.valueOf(object);
                    Toast.makeText(RefActivity.refACActivity.get(), errorCode, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return messageUsers.size();
    }

    class MessageUserRecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImageIV;
        private TextView fromTV;
        private TextView messageContentTV;

        MessageUserRecyclerViewHolder(View itemView) {
            super(itemView);

            userImageIV = itemView.findViewById(R.id.userImageIV);
            fromTV = itemView.findViewById(R.id.fromTV);
            messageContentTV = itemView.findViewById(R.id.messageContentTV);
        }
    }
}
