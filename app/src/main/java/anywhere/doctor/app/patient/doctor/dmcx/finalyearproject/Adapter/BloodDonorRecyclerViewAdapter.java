package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.HomeServiceController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICall;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodDonor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class BloodDonorRecyclerViewAdapter extends RecyclerView.Adapter<BloodDonorRecyclerViewAdapter.BloodDonorRecyclerViewHolder> implements ICall {

    private List<BloodDonor> bloodDonors = new ArrayList<>();
    private int lastPosition = 0;

    public void setBloodDonors(List<BloodDonor> bloodDonors) {
        this.bloodDonors = bloodDonors;
    }

    @Override
    public ICall getiCall() {
        return this;
    }

    private class DonorDialog {
        private String[] options = new String[] {"Call User"};
        private Context context;
        private int position;

        private DonorDialog(Context context, int position) {
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

            builder.show();
        }
    }

    private void callDonor(int position) {
        Intent callIntent = new Intent();
        callIntent.setAction(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + bloodDonors.get(position).getPhone()));
        RefActivity.refACActivity.get().startActivity(callIntent);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                RefActivity.refACActivity.get().requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, Vars.RequestCode.REQUEST_CALL_CODE_BD);
                return false;
            }
        }
        return true;
    }

    @NonNull
    @Override
    public BloodDonorRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layuot_rv_single_blood_donor, parent, false);
        return new BloodDonorRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodDonorRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        holder.donorNameBDTV.setText(bloodDonors.get(position).getName());
        holder.donorAgeBDTV.setText(new StringBuilder(bloodDonors.get(position).getAge()).append(" years old"));
        holder.donorCityBDTV.setText(bloodDonors.get(position).getCity());

        if (bloodDonors.get(position).getGender().toLowerCase().equals(AFModel.male.toLowerCase())) {
            holder.donorGenderBDIV.setImageResource(R.drawable.male);
        } else if (bloodDonors.get(position).getGender().toLowerCase().equals(AFModel.female.toLowerCase())) {
            holder.donorGenderBDIV.setImageResource(R.drawable.female);
        }

        holder.donorBloodGroupBDTV.setText(bloodDonors.get(position).getGroup());

        if (!bloodDonors.get(position).getAge().equals("") && !bloodDonors.get(position).getWeight().equals("")) {
            int age = Integer.valueOf(bloodDonors.get(position).getAge());
            int weight = Integer.valueOf(bloodDonors.get(position).getWeight());
            int month = 4;

            if (!bloodDonors.get(position).getLast_donation_date().equals("")) {
                String lastDonationDate = bloodDonors.get(position).getLast_donation_date();

                try {
                    String[] date = lastDonationDate.split("-");
                    month = Integer.valueOf(date[1]);
                    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                    month = Math.abs(month - currentMonth);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (age > 16 && weight > 50 && month > 3) {
                holder.donorEligibleBDIV.setVisibility(View.VISIBLE);
            } else {
                holder.donorEligibleBDIV.setVisibility(View.INVISIBLE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DonorDialog dialog = new DonorDialog(RefActivity.refACActivity.get(), itemPosition);
                dialog.create();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bloodDonors.size();
    }

    class BloodDonorRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView donorNameBDTV;
        private TextView donorAgeBDTV;
        private TextView donorCityBDTV;
        private ImageView donorGenderBDIV;
        private TextView donorBloodGroupBDTV;
        private ImageView donorEligibleBDIV;

        BloodDonorRecyclerViewHolder(View itemView) {
            super(itemView);

            donorNameBDTV = itemView.findViewById(R.id.donorNameBDTV);
            donorAgeBDTV = itemView.findViewById(R.id.donorAgeBDTV);
            donorCityBDTV = itemView.findViewById(R.id.donorCityBDTV);
            donorGenderBDIV = itemView.findViewById(R.id.donorGenderBDIV);
            donorBloodGroupBDTV = itemView.findViewById(R.id.donorBloodGroupBDTV);
            donorEligibleBDIV = itemView.findViewById(R.id.donorEligibleBDIV);
        }
    }

    @Override
    public void call() {
        callDonor(lastPosition);
    }
}
