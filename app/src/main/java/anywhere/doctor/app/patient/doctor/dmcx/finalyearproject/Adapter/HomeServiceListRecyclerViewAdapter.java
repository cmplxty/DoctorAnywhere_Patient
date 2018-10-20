package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.HomeServiceController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HomeService;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class HomeServiceListRecyclerViewAdapter extends RecyclerView.Adapter<HomeServiceListRecyclerViewAdapter.HomeServiceListRecyclerViewHolder> implements ICallDoctor {

    private List<HomeService> homeServices = new ArrayList<>();
    private ICallDoctor iCallDoctor;
    private int lastPosition;

    public ICallDoctor getiCallDoctor() {
        return this;
    }

    public void setHomeServices(List<HomeService> homeServices) {
        this.homeServices = homeServices;
    }

    private void callDoctor(int position) {
        Intent callIntent = new Intent();
        callIntent.setAction(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + homeServices.get(position).getDoctor_phone()));
        RefActivity.refACActivity.get().startActivity(callIntent);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                RefActivity.refACActivity.get().requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, Vars.RequeseCode.REQUEST_CALL_CODE_HS);
                return false;
            }
        }
        return true;
    }

    @NonNull
    @Override
    public HomeServiceListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_home_service_request, parent, false);
        return new HomeServiceListRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeServiceListRecyclerViewHolder holder, int position) {

        final int itemPosition = position;

        holder.doctorNameHSTV.setText(homeServices.get(position).getDoctor_name());
        holder.doctorTimeHSTV.setText(new StringBuilder("Time: ").append(homeServices.get(position).getDoctor_time()));
        holder.doctorSpecialistHSTV.setText(new StringBuilder("Specialist: ").append(homeServices.get(position).getDoctor_specialist()));

        holder.phoneHSIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPosition = itemPosition;

                if (checkPermission()) {
                    callDoctor(itemPosition);
                }
            }
        });

        holder.cancelHSIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeServiceController.CancelHomeServiceRequest(homeServices.get(itemPosition).getDoctor_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeServices.size();
    }

    @Override
    public void call() {
        callDoctor(lastPosition);
    }

    class HomeServiceListRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView doctorNameHSTV;
        private TextView doctorTimeHSTV;
        private TextView doctorSpecialistHSTV;
        private ImageButton phoneHSIB;
        private ImageButton cancelHSIB;

        HomeServiceListRecyclerViewHolder(View itemView) {
            super(itemView);

            doctorNameHSTV = itemView.findViewById(R.id.doctorNameHSTV);
            doctorTimeHSTV = itemView.findViewById(R.id.doctorTimeHSTV);
            doctorSpecialistHSTV = itemView.findViewById(R.id.doctorSpecialistHSTV);
            phoneHSIB = itemView.findViewById(R.id.phoneHSIB);
            cancelHSIB = itemView.findViewById(R.id.cancelHSIB);
        }
    }
}
