package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.util.Objects;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.HomeServiceController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICall;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HomeService;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class HomeServiceRequestRecyclerViewAdapter extends RecyclerView.Adapter<HomeServiceRequestRecyclerViewAdapter.HomeServiceRequestRecyclerViewHolder> implements ICall {

    private List<HomeService> homeServices = new ArrayList<>();
    private ICall iCall;
    private int lastPosition;

    @Override
    public ICall getiCall() {
        return this;
    }

    public void setHomeServices(List<HomeService> homeServices) {
        this.homeServices = homeServices;
    }

    private class HomeServiceDialog {
        private String[] options = new String[] {"Call Doctor", "Remove"};
        private Context context;
        private int position;

        private HomeServiceDialog(Context context, int position) {
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
                            callDoctor(position);
                        }
                    } else if (selectedPosition == 1) {
                        HomeServiceController.CancelHomeServiceRequest(homeServices.get(position).getDoctor_id());
                    } else {
                        Log.d(Vars.TAG, "Dialog: Unknown Call");
                    }

                }
            });

            builder.show();
        }
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
                RefActivity.refACActivity.get().requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, Vars.RequestCode.REQUEST_CALL_CODE_HS);
                return false;
            }
        }
        return true;
    }

    @NonNull
    @Override
    public HomeServiceRequestRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_home_service_request, parent, false);
        return new HomeServiceRequestRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeServiceRequestRecyclerViewHolder holder, int position) {

        final int itemPosition = position;

        holder.doctorNameHSTV.setText(homeServices.get(position).getDoctor_name());
        holder.doctorTimeHSTV.setText(homeServices.get(position).getDoctor_time());
        holder.doctorSpecialistHSTV.setText(homeServices.get(position).getDoctor_specialist());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeServiceDialog dialog = new HomeServiceDialog(RefActivity.refACActivity.get(), itemPosition);
                dialog.create();
            }
        });

        if (Objects.equals(position, homeServices.size() - 1) && homeServices.size() > 1) {
            holder.bottomLineView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return homeServices.size();
    }

    @Override
    public void call() {
        callDoctor(lastPosition);
    }

    class HomeServiceRequestRecyclerViewHolder extends RecyclerView.ViewHolder {

        private View bottomLineView;
        private TextView doctorNameHSTV;
        private TextView doctorTimeHSTV;
        private TextView doctorSpecialistHSTV;

        HomeServiceRequestRecyclerViewHolder(View itemView) {
            super(itemView);

            bottomLineView = itemView.findViewById(R.id.bottomLineView);
            doctorNameHSTV = itemView.findViewById(R.id.doctorNameHSTV);
            doctorTimeHSTV = itemView.findViewById(R.id.doctorTimeHSTV);
            doctorSpecialistHSTV = itemView.findViewById(R.id.doctorSpecialistHSTV);
        }
    }
}
