package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AudioCallController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AudioCallHistory;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;
import de.hdodenhof.circleimageview.CircleImageView;

public class AudioCallHistoryRecyclerViewAdapter extends RecyclerView.Adapter<AudioCallHistoryRecyclerViewAdapter.AudioCallHistoryRecyclerViewHolder> {

    private List<AudioCallHistory> histories = new ArrayList<>();

    public void setHistories(List<AudioCallHistory> histories) {
        this.histories = histories;
    }

    private class HistoryDialog {
        private String[] options = new String[] {"Call Doctor", "Message", "Remove"};
        private Context context;
        private int position;

        private HistoryDialog(Context context, int position) {
            this.context = context;
            this.position = position;
        }

        public void create() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select an options");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int selectedPosition) {
                    dialogInterface.dismiss();

                    if (selectedPosition == 0) {
                        ActivityTrigger.AudioCallActivity((Doctor) histories.get(position).getUser(), Vars.ParentActivity.AUDIO_CALL_HISTORY);
                    } else if (selectedPosition == 1) {
                        ActivityTrigger.MessageActivity((Doctor) histories.get(position).getUser(), Vars.ActivityOverrider.FROM_AUDIO_CALL_HISTORY);
                    } else if (selectedPosition == 2) {
                        AudioCallController.DeleteCurrentHistory(histories.get(position).getId());
                    } else {
                        Log.d(Vars.TAG, "Dialog: Unknown Call");
                    }

                }
            });

            builder.show();
        }
    }

    @NonNull
    @Override
    public AudioCallHistoryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_audio_call_history, parent, false);
        return new AudioCallHistoryRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioCallHistoryRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        if (histories.size() > 1 && position == 0) {
            holder.relativeLayoutLineRL.setVisibility(View.INVISIBLE);
        }

        if (histories.get(position).getCall_status().equals(AFModel.missed)) {
            holder.historyUserCallStatusHIV.setImageResource(R.drawable.missed_call_red_10);
        } else if (histories.get(position).getCall_status().equals(AFModel.received)) {
            holder.historyUserCallStatusHIV.setImageResource(R.drawable.received_call_green_10);
        }

        if (histories.get(position).getUser() instanceof Doctor) {
            Doctor doctor = (Doctor) histories.get(position).getUser();
            if (doctor != null) {
                if (!doctor.getImage_link().equals("")) {
                    holder.historyUserNameHTV.setText(doctor.getName());
                    Picasso.with(RefActivity.refACActivity.get()).load(doctor.getImage_link())
                            .placeholder(R.drawable.noperson)
                            .into(holder.historyCallerImageHCIV);
                }
            }
        }

        holder.historyCallDateHTV.setText(histories.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryDialog dialog = new HistoryDialog(RefActivity.refACActivity.get(), itemPosition);
                dialog.create();
            }
        });
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    class AudioCallHistoryRecyclerViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayoutLineRL;
        private CircleImageView historyCallerImageHCIV;
        private TextView historyUserNameHTV;
        private ImageView historyUserCallStatusHIV;
        private TextView historyCallDateHTV;

        private AudioCallHistoryRecyclerViewHolder(View itemView) {
            super(itemView);

            relativeLayoutLineRL = itemView.findViewById(R.id.relativeLayoutLineRL);
            historyCallerImageHCIV = itemView.findViewById(R.id.historyCallerImageHCIV);
            historyUserNameHTV = itemView.findViewById(R.id.historyUserNameHTV);
            historyUserCallStatusHIV = itemView.findViewById(R.id.historyUserCallStatusHIV);
            historyCallDateHTV = itemView.findViewById(R.id.historyCallDateHTV);
        }
    }
}
