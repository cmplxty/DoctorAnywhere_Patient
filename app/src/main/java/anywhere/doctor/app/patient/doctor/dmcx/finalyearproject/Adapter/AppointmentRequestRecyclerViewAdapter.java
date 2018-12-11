package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AppointmentController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICall;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AppointmentRequest;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AppointmentRequestRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRequestRecyclerViewAdapter.AppointmentRecyclerViewHolder> implements ICall {

    private List<AppointmentRequest> appointmentRequests;
    private int lastPosition;

    @Override
    public ICall getiCall() {
        return this;
    }

    public void setAppointments(List<AppointmentRequest> appointmentRequests) {
        this.appointmentRequests = appointmentRequests;
    }

    private void callDoctor(int position) {
        Intent callIntent = new Intent();
        callIntent.setAction(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + appointmentRequests.get(position).getDoctor_phone()));
        RefActivity.refACActivity.get().startActivity(callIntent);
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Vars.currentFragment.requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, Vars.RequestCode.REQUEST_CALL_CODE_AP);
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public AppointmentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_appt, parent, false);
        return new AppointmentRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppointmentRecyclerViewHolder holder, int position) {
        final AppointmentRecyclerViewHolder viewHolder = holder;
        final int itemPosition = position;

        holder.apptDoctorNameTV.setText(appointmentRequests.get(itemPosition).getDoctor_name());
        holder.apptDateTV.setText(new StringBuilder("Date: ").append(appointmentRequests.get(itemPosition).getDate()));
        holder.apptTimeTV.setText(new StringBuilder("Time: ").append(appointmentRequests.get(itemPosition).getTime()));
        holder.apptStatusTV.setText(new StringBuilder("Status: ").append(appointmentRequests.get(itemPosition).getStatus()));
        holder.apptClinicTV.setText(new StringBuilder("Clinic: ").append(appointmentRequests.get(itemPosition).getDoctor_clinic()));

        holder.apptCancelIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appointmentRequests.get(itemPosition).getStatus().equals(AFModel.request)) {
                    AppointmentController.DeleteAppointmentRequest(appointmentRequests.get(itemPosition).getDoctor_id());
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.YouAreAbleToDeleteAfterAcceptOrCancel, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.apptPhoneIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPosition = itemPosition;

                if (appointmentRequests.get(itemPosition).getStatus().equals(AFModel.accept)) {
                    if (checkPermission()) {
                        callDoctor(lastPosition);
                    }
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.YouAreAbleToCallAfterAccept, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.othersCL.getVisibility() == View.GONE) {
                    holder.othersCL.setVisibility(View.VISIBLE);
                } else {
                    holder.othersCL.setVisibility(View.GONE);
                }
            }
        });

        if (Objects.equals(position, appointmentRequests.size() - 1) && !Objects.equals(appointmentRequests.size(), 1)) {
            holder.bottomLineView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return appointmentRequests.size();
    }

    @Override
    public void call() {
        callDoctor(lastPosition);
    }

    class AppointmentRecyclerViewHolder extends RecyclerView.ViewHolder {

        private View bottomLineView;
        private TextView apptDoctorNameTV;
        private TextView apptDateTV;
        private TextView apptStatusTV;
        private TextView apptClinicTV;
        private TextView apptTimeTV;
        private ImageButton apptCancelIB;
        private ImageButton apptPhoneIB;
        private ConstraintLayout othersCL;

        AppointmentRecyclerViewHolder(View itemView) {
            super(itemView);

            bottomLineView = itemView.findViewById(R.id.bottomLineView);
            apptDoctorNameTV = itemView.findViewById(R.id.apptDoctorNameTV);
            apptDateTV = itemView.findViewById(R.id.apptDateTV);
            apptStatusTV = itemView.findViewById(R.id.apptStatusTV);
            apptClinicTV = itemView.findViewById(R.id.apptClinicTV);
            apptCancelIB = itemView.findViewById(R.id.apptCancelIB);
            apptTimeTV = itemView.findViewById(R.id.apptTimeTV);
            apptPhoneIB = itemView.findViewById(R.id.apptPhoneIB);
            othersCL = itemView.findViewById(R.id.othersCL);
        }
    }
}
