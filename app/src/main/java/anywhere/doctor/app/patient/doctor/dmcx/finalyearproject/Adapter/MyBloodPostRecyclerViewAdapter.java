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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.BloodDonorController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodPost;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class MyBloodPostRecyclerViewAdapter extends RecyclerView.Adapter<MyBloodPostRecyclerViewAdapter.MyBloodPostRecyclerViewHolder> {

    private List<BloodPost> bloodPosts = new ArrayList<>();

    public void setBloodPosts(List<BloodPost> bloodPosts) {
        this.bloodPosts = bloodPosts;
    }

    private class MyPostDialog {
        private String[] options = new String[] {"Remove Post"};
        private Context context;
        private int position;

        private MyPostDialog(Context context, int position) {
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
                        BloodDonorController.DeleteCurrentPost(bloodPosts.get(position).getId());
                    } else {
                        Log.d(Vars.TAG, "Dialog: Unknown Call");
                    }

                }
            });

            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1EC8C8"));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1EC8C8"));
                }
            });

            dialog.show();
        }
    }

    @NonNull
    @Override
    public MyBloodPostRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_my_blood_post, parent, false);
        return new MyBloodPostRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBloodPostRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        holder.bloodPostNameBPTV.setText(bloodPosts.get(position).getName());
        holder.bloodPostCityBPTV.setText(bloodPosts.get(position).getCity());
        holder.bloodGroupBPTV.setText(bloodPosts.get(position).getGroup());
        holder.bloodPostDescriptionBPTV.setText(bloodPosts.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPostDialog dialog = new MyPostDialog(RefActivity.refACActivity.get(), itemPosition);
                dialog.create();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bloodPosts.size();
    }

    class MyBloodPostRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView bloodPostNameBPTV;
        private TextView bloodPostCityBPTV;
        private TextView bloodGroupBPTV;
        private TextView bloodPostDescriptionBPTV;

        MyBloodPostRecyclerViewHolder(View itemView) {
            super(itemView);

            bloodPostNameBPTV = itemView.findViewById(R.id.bloodPostNameBPTV);
            bloodPostCityBPTV = itemView.findViewById(R.id.bloodPostCityBPTV);
            bloodGroupBPTV = itemView.findViewById(R.id.bloodGroupBPTV);
            bloodPostDescriptionBPTV = itemView.findViewById(R.id.bloodPostDescriptionBPTV);
        }
    }

}
