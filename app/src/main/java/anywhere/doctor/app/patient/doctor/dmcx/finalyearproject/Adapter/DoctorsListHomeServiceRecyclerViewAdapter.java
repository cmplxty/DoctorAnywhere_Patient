package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.HomeServiceController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.ProfileController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HSDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorsListHomeServiceRecyclerViewAdapter extends RecyclerView.Adapter<DoctorsListHomeServiceRecyclerViewAdapter.DoctorsListHomeServiceRecyclerViewHolder> {

    private List<HSDoctor> hsdoctors = new ArrayList<>();

    public void setHsdoctors(List<HSDoctor> hsdoctors) {
        this.hsdoctors = hsdoctors;
    }

    @NonNull
    @Override
    public DoctorsListHomeServiceRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_list_doctor_home_service, parent, false);
        return new DoctorsListHomeServiceRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsListHomeServiceRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        holder.doctorsNameLDHS.setText(hsdoctors.get(position).getName());
        holder.doctorsSpecialistLDHS.setText(new StringBuilder("Specialist: ").append(hsdoctors.get(position).getSpecialist()));
        holder.doctorsTimeLDHS.setText(new StringBuilder("Time: ").append(hsdoctors.get(position).getTime()));
        holder.doctorsLocationLDHS.setText(new StringBuilder("Location: ").append(hsdoctors.get(position).getLocation()));

        final DoctorsListHomeServiceRecyclerViewHolder tempVH = holder;
        tempVH.sendAndCancelRequestHSIB.setVisibility(View.INVISIBLE);
        tempVH.mLoadingRL.start();
        HomeServiceController.CheckIsPatientRequestedYet(hsdoctors.get(position).getId(), new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                tempVH.mLoadingRL.stop();
                tempVH.sendAndCancelRequestHSIB.setVisibility(View.VISIBLE);

                boolean isRequestSent = (boolean) object;
                if (isRequestSent) {
                    tempVH.sendAndCancelRequestHSIB.setTag(true);
                    tempVH.sendAndCancelRequestHSIB.setImageResource(R.drawable.cancel_request_cyan);
                } else {
                    tempVH.sendAndCancelRequestHSIB.setTag(false);
                    tempVH.sendAndCancelRequestHSIB.setImageResource(R.drawable.send_request_cyan);
                }
            }
        });

        holder.sendAndCancelRequestHSIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Boolean) tempVH.sendAndCancelRequestHSIB.getTag()) {
                    HomeServiceController.CancelHomeServiceRequest(hsdoctors.get(itemPosition).getId());
                } else {
                    String did = hsdoctors.get(itemPosition).getId();
                    String pid = Vars.appFirebase.getCurrentUser().getUid();
                    String dname = hsdoctors.get(itemPosition).getName();
                    String dlocation = hsdoctors.get(itemPosition).getLocation();
                    String dphone = hsdoctors.get(itemPosition).getPhone();
                    String dspecialist = hsdoctors.get(itemPosition).getSpecialist();
                    String dtime = hsdoctors.get(itemPosition).getTime();
                    String pname = ProfileController.GetLocalProfile().getName();
                    String paddress = ProfileController.GetLocalProfile().getAddress();
                    String pphone = ProfileController.GetLocalProfile().getPhone();

                    HomeServiceController.SendHomeServiceRequest(did, pid, dname, dlocation, dphone, dspecialist, dtime, pname, paddress, pphone);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hsdoctors.size();
    }

    class DoctorsListHomeServiceRecyclerViewHolder extends RecyclerView.ViewHolder {

        private RotateLoading mLoadingRL;
        private TextView doctorsNameLDHS;
        private TextView doctorsSpecialistLDHS;
        private TextView doctorsTimeLDHS;
        private TextView doctorsLocationLDHS;
        private ImageButton sendAndCancelRequestHSIB;

        DoctorsListHomeServiceRecyclerViewHolder(View itemView) {
            super(itemView);

            mLoadingRL = itemView.findViewById(R.id.mLoadingRL);
            doctorsNameLDHS = itemView.findViewById(R.id.doctorsNameLDHS);
            doctorsSpecialistLDHS = itemView.findViewById(R.id.doctorsSpecialistLDHS);
            doctorsTimeLDHS = itemView.findViewById(R.id.doctorsTimeLDHS);
            doctorsLocationLDHS = itemView.findViewById(R.id.doctorsLocationLDHS);
            sendAndCancelRequestHSIB = itemView.findViewById(R.id.sendAndCancelRequestHSIB);
        }
    }
}
