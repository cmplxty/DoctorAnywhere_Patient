package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICall;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodPost;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class BloodPostRecyclerViewAdapter extends RecyclerView.Adapter<BloodPostRecyclerViewAdapter.BloodPostRecyclerViewHolder> implements ICall {

    private List<BloodPost> bloodPosts = new ArrayList<>();
    private int lastPosition = 0;

    public void setBloodPosts(List<BloodPost> bloodPosts) {
        this.bloodPosts = bloodPosts;
    }

    @Override
    public ICall getiCall() {
        return this;
    }

    private class PostDialog {
        private String[] options = new String[] {"Call User"};
        private Context context;
        private int position;

        private PostDialog(Context context, int position) {
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
                        lastPosition = position;
                        if (checkPermission()) {
                            callDonor(position);
                        }
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

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                RefActivity.refACActivity.get().requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, Vars.RequestCode.REQUEST_CALL_CODE_BP);
                return false;
            }
        }
        return true;
    }

    private void callDonor(int position) {
        Intent callIntent = new Intent();
        callIntent.setAction(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + bloodPosts.get(position).getPhone()));
        RefActivity.refACActivity.get().startActivity(callIntent);
    }

    @NonNull
    @Override
    public BloodPostRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_blood_post, parent, false);
        return new BloodPostRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodPostRecyclerViewAdapter.BloodPostRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        holder.bloodPostNameBPTV.setText(bloodPosts.get(position).getName());
        holder.bloodPostCityBPTV.setText(bloodPosts.get(position).getCity());
        holder.bloodGroupBPTV.setText(bloodPosts.get(position).getGroup());
        holder.bloodPostDescriptionBPTV.setText(bloodPosts.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostDialog dialog = new PostDialog(RefActivity.refACActivity.get(), itemPosition);
                dialog.create();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bloodPosts.size();
    }

    class BloodPostRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView bloodPostNameBPTV;
        private TextView bloodPostCityBPTV;
        private TextView bloodGroupBPTV;
        private TextView bloodPostDescriptionBPTV;

        BloodPostRecyclerViewHolder(View itemView) {
            super(itemView);

            bloodPostNameBPTV = itemView.findViewById(R.id.bloodPostNameBPTV);
            bloodPostCityBPTV = itemView.findViewById(R.id.bloodPostCityBPTV);
            bloodGroupBPTV = itemView.findViewById(R.id.bloodGroupBPTV);
            bloodPostDescriptionBPTV = itemView.findViewById(R.id.bloodPostDescriptionBPTV);
        }
    }

    @Override
    public void call() {
        callDonor(lastPosition);
    }
}
