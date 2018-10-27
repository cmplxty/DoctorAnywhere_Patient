package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AppointmentController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.APRequest;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AppointmentRequestRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRequestRecyclerViewAdapter.AppointmentRecyclerViewHolder> implements ICallDoctor {

    private List<APRequest> apRequests;
    private int lastPosition;

    public ICallDoctor getiCallDoctor() {
        return this;
    }

    public void setAppointments(List<APRequest> apRequests) {
        this.apRequests = apRequests;
    }

    private void callDoctor(int position) {
        Intent callIntent = new Intent();
        callIntent.setAction(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + apRequests.get(position).getDoctor_phone()));
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

        final int itemPosition = position;

        holder.apptDoctorNameTV.setText(apRequests.get(itemPosition).getDoctor_name());
        holder.apptDateTV.setText(new StringBuilder("Date: ").append(apRequests.get(itemPosition).getDate()));
        holder.apptStatusTV.setText(new StringBuilder("Status: ").append(apRequests.get(itemPosition).getStatus()));
        holder.apptClinicTV.setText(new StringBuilder("Clinic: ").append(apRequests.get(itemPosition).getDoctor_clinic()));

        holder.apptCancelIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!apRequests.get(itemPosition).getStatus().equals(AFModel.request)) {
                    AppointmentController.DeleteAppointmentRequest(apRequests.get(itemPosition).getDoctor_id());
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.YouAreAbleToDeleteAfterAcceptOrCancel, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.apptPhoneIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPosition = itemPosition;

                if (apRequests.get(itemPosition).getStatus().equals(AFModel.accept)) {
                    if (checkPermission()) {
                        callDoctor(lastPosition);
                    }
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.YouAreAbleToCallAfterAccept, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return apRequests.size();
    }

    @Override
    public void call() {
        callDoctor(lastPosition);
    }

    class AppointmentRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView apptDoctorNameTV;
        private TextView apptDateTV;
        private TextView apptStatusTV;
        private TextView apptClinicTV;
        private ImageButton apptCancelIB;
        private ImageButton apptPhoneIB;

        AppointmentRecyclerViewHolder(View itemView) {
            super(itemView);

            apptDoctorNameTV = itemView.findViewById(R.id.apptDoctorNameTV);
            apptDateTV = itemView.findViewById(R.id.apptDateTV);
            apptStatusTV = itemView.findViewById(R.id.apptStatusTV);
            apptClinicTV = itemView.findViewById(R.id.apptClinicTV);
            apptCancelIB = itemView.findViewById(R.id.apptCancelIB);
            apptPhoneIB = itemView.findViewById(R.id.apptPhoneIB);
        }
    }
}
