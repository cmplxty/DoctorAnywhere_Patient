package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.PrescriptionController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Message;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Prescription;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.FormetterDateTime;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageRecyclerViewHolder> {

    private List<Message> messages;

    public MessageRecyclerViewAdapter() {
        this.messages = new ArrayList<>();
    }

    public void setMessages(List<Message> list) {
        messages = list;
    }

    @NonNull
    @Override
    public MessageRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_message, parent, false);
        return new MessageRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecyclerViewHolder holder, int position) {

        final int itemPosition = position;

        switch (messages.get(position).getType()) {
            case AFModel.image:
                holder.messageContent.setVisibility(View.GONE);
                holder.viewPrescriptionBTN.setVisibility(View.GONE);
                holder.messageImage.setVisibility(View.VISIBLE);

                Picasso.with(RefActivity.refACActivity.get()).load(messages.get(position).getContent()).placeholder(R.drawable.nophoto).into(holder.messageImage);
                break;
            case AFModel.text:
                holder.messageImage.setVisibility(View.GONE);
                holder.viewPrescriptionBTN.setVisibility(View.GONE);
                holder.messageContent.setVisibility(View.VISIBLE);

                holder.messageContent.setText(messages.get(position).getContent());
                break;
            case AFModel.prescription:
                holder.messageContent.setVisibility(View.GONE);
                holder.messageImage.setVisibility(View.GONE);
                holder.viewPrescriptionBTN.setVisibility(View.VISIBLE);

                holder.viewPrescriptionBTN.setText(new StringBuilder("View Prescription\n").append(FormetterDateTime.getDate(messages.get(position).getTimestamp())));

                holder.viewPrescriptionBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PrescriptionController.LoadSinglePrescription(messages.get(itemPosition).getContent(), new IAction() {
                            @Override
                            public void onCompleteAction(Object object) {
                                if (object instanceof Prescription) {
                                    ActivityTrigger.PrescriptionActivity((Prescription) object);
                                }
                            }
                        });
                    }
                });
                break;
        }

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageParams.width = Math.round(RefActivity.refACActivity.get().getResources().getDisplayMetrics().density * 250);
        imageParams.height = Math.round(RefActivity.refACActivity.get().getResources().getDisplayMetrics().density * 250);

        LinearLayout.LayoutParams generalParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        generalParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;

        if (messages.get(position).getFrom().equals(Vars.appFirebase.getCurrentUser().getUid())) {
            imageParams.gravity = Gravity.END;
            generalParams.gravity = Gravity.END;

            holder.messageImage.setBackground(RefActivity.refACActivity.get().getDrawable(R.drawable.message_from));
            holder.messageImage.setLayoutParams(imageParams);

            holder.messageContent.setBackground(RefActivity.refACActivity.get().getDrawable(R.drawable.message_from));
            holder.messageContent.setTextColor(Color.BLACK);
            holder.messageContent.setLayoutParams(generalParams);
        } else {
            imageParams.gravity = Gravity.START;
            generalParams.gravity = Gravity.START;

            holder.messageImage.setBackground(RefActivity.refACActivity.get().getDrawable(R.drawable.message_to));
            holder.messageImage.setLayoutParams(imageParams);

            holder.messageContent.setBackground(RefActivity.refACActivity.get().getDrawable(R.drawable.message_to));
            holder.messageContent.setTextColor(Color.WHITE);
            holder.messageContent.setLayoutParams(generalParams);
        }

        holder.messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!messages.get(itemPosition).getContent().equals(AFModel.deflt))
                    ActivityTrigger.ViewImageActivity(messages.get(itemPosition).getContent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView messageContent;
        private ImageView messageImage;
        private Button viewPrescriptionBTN;

        MessageRecyclerViewHolder(View itemView) {
            super(itemView);

            messageContent = itemView.findViewById(R.id.messageContent);
            messageImage = itemView.findViewById(R.id.messageImage);
            viewPrescriptionBTN = itemView.findViewById(R.id.viewPrescriptionBTN);
        }
    }
}
