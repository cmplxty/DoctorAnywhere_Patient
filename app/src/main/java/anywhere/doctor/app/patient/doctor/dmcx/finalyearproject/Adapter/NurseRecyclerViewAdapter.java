package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.NurseController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Nurse;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;
import de.hdodenhof.circleimageview.CircleImageView;

public class NurseRecyclerViewAdapter extends RecyclerView.Adapter<NurseRecyclerViewAdapter.NurseRecyclerViewHolder> {

    private final String TAG_CROSS = "cross";
    private final String TAG_SEND = "send";
    private List<Nurse> nurses = new ArrayList<>();

    public void setNurses(List<Nurse> nurses) {
        this.nurses = nurses;
    }

    private class NurseDialog {
        private Context context;
        private int position;

        private NurseDialog(Context context, int position) {
            this.context = context;
            this.position = position;
        }

        public void create() {
            String about = nurses.get(position).getAbout();
            if (about.equals("")) {
                about = "No about text of this Nurse!";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(nurses.get(position).getName());
            builder.setMessage(about);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1EC8C8"));
                }
            });
            dialog.show();
        }
    }

    @NonNull
    @Override
    public NurseRecyclerViewAdapter.NurseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_nurse, parent, false);
        return new NurseRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NurseRecyclerViewAdapter.NurseRecyclerViewHolder holder, int position) {
        final NurseRecyclerViewHolder viewHolder = holder;
        final int itemPosition = position;

        if (!nurses.get(position).getImage_link().equals("")) {
            Picasso.with(RefActivity.refACActivity.get()).load(nurses.get(position).getImage_link()).placeholder(R.drawable.noperson).into(holder.sNursePicCIV);
        }

        holder.sNurseNameTV.setText(nurses.get(position).getName());
        holder.sNurseTypeTV.setText(nurses.get(position).getType());
        holder.sNurseGenderTV.setText(nurses.get(position).getGender());
        holder.sNurseDegreeTV.setText(nurses.get(position).getDegree());
        holder.sendRequestNurseIB.setVisibility(View.GONE);

        if (Objects.equals(nurses.get(position).getGender().toLowerCase(), AFModel.male.toLowerCase())) {
            holder.sNurseGenderImageIV.setImageResource(R.drawable.nuese_male_cyan_0);
        } else {
            holder.sNurseGenderImageIV.setImageResource(R.drawable.nurse_cyan_dk);
        }

        if (Objects.equals(position, nurses.size() - 1) && nurses.size() > 1) {
            holder.borderRL.setVisibility(View.GONE);
        }

        NurseController.CheckRequesNurse(nurses.get(position).getId(), new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if ((Boolean) object) {
                    viewHolder.sendRequestNurseIB.setImageResource(R.drawable.cross_cyan_md);
                    viewHolder.sendRequestNurseIB.setTag(TAG_CROSS);
                } else {
                    viewHolder.sendRequestNurseIB.setImageResource(R.drawable.send_request_cyan);
                    viewHolder.sendRequestNurseIB.setTag(TAG_SEND);
                }

                viewHolder.sendRequestNurseIB.setVisibility(View.VISIBLE);
            }
        });

        holder.sendRequestNurseIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.sendRequestNurseIB.getTag().equals(TAG_SEND)) {
                    NurseController.SendRequestNurse(nurses.get(itemPosition).getId());
                } else {
                    NurseController.CancelRequestNurse(nurses.get(itemPosition).getId());
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NurseDialog dialog = new NurseDialog(RefActivity.refACActivity.get(), itemPosition);
                dialog.create();
            }
        });
    }

    @Override
    public int getItemCount() {
        return nurses.size();
    }

    class NurseRecyclerViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView sNursePicCIV;
        private TextView sNurseNameTV;
        private TextView sNurseGenderTV;
        private TextView sNurseTypeTV;
        private TextView sNurseDegreeTV;
        private ImageButton sendRequestNurseIB;
        private ImageView sNurseGenderImageIV;
        private RelativeLayout borderRL;

        NurseRecyclerViewHolder(View itemView) {
            super(itemView);

            sNursePicCIV = itemView.findViewById(R.id.sNursePicCIV);
            sNurseDegreeTV = itemView.findViewById(R.id.sNurseDegreeTV);
            sNurseNameTV = itemView.findViewById(R.id.sNurseNameTV);
            sNurseGenderTV = itemView.findViewById(R.id.sNurseGenderTV);
            sNurseTypeTV = itemView.findViewById(R.id.sNurseTypeTV);
            sendRequestNurseIB = itemView.findViewById(R.id.sendRequestNurseIB);
            sNurseGenderImageIV = itemView.findViewById(R.id.sNurseGenderImageIV);
            borderRL = itemView.findViewById(R.id.borderRL);
        }

    }
}
