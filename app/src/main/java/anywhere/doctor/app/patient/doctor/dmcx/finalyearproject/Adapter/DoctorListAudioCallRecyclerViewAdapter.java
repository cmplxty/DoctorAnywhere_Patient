package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AudioCallController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.ACDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.Utils;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorListAudioCallRecyclerViewAdapter extends RecyclerView.Adapter<DoctorListAudioCallRecyclerViewAdapter.DoctorListAudioCallRecyclerViewHolder> {
    private List<ACDoctor> acDoctors = new ArrayList<>();

    public void setAcDoctors(List<ACDoctor> acDoctors) {
        this.acDoctors = acDoctors;
    }

    private boolean checkPermission() {
        String record_audio = Manifest.permission.RECORD_AUDIO;
        String modify_audio_settings = Manifest.permission.MODIFY_AUDIO_SETTINGS;
        String read_phone_state = Manifest.permission.READ_PHONE_STATE;
        if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), record_audio) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), modify_audio_settings) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), read_phone_state) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(RefActivity.refACActivity.get(), new String[] {record_audio, read_phone_state, modify_audio_settings}, Vars.RequestCode.REQUEST_FOR_ONLINE_AUDIO_CALL);
            return false;
        }

        return true;
    }

    @NonNull
    @Override
    public DoctorListAudioCallRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_list_doctor_audio_call, parent, false);
        return new DoctorListAudioCallRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorListAudioCallRecyclerViewHolder holder, int position) {
        holder.doctorNameACDLTV.setText(acDoctors.get(position).getDoctor().getName());
        holder.doctorSpecialistACDLTV.setText(new StringBuilder("Specialist: ").append(acDoctors.get(position).getDoctor().getSpecialist()));
        holder.doctorStatusACDLTV.setText(new StringBuilder("Status: ").append(Utils.firstCap(acDoctors.get(position).getAudio_call_status())));

        final Doctor doctor = acDoctors.get(position).getDoctor();
        holder.callDoctorACDLIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission())
                    return;

                AudioCallController.SetCurrentDeviceId(new IAction() {
                    @Override
                    public void onCompleteAction(Object object) {
                        if ((Boolean) object)
                            ActivityTrigger.AudioCallActivity(doctor);
                        else
                            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.FailedToStoreDeviceId, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (acDoctors.size() - 1 == position && acDoctors.size() > 1) {
            holder.relativeLayoutLineRL.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return acDoctors.size();
    }

    class DoctorListAudioCallRecyclerViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayoutLineRL;
        private TextView doctorNameACDLTV;
        private TextView doctorSpecialistACDLTV;
        private TextView doctorStatusACDLTV;
        private ImageButton callDoctorACDLIB;

        DoctorListAudioCallRecyclerViewHolder(View itemView) {
            super(itemView);

            relativeLayoutLineRL = itemView.findViewById(R.id.relativeLayoutLineRL);
            doctorNameACDLTV = itemView.findViewById(R.id.doctorNameACDLTV);
            doctorSpecialistACDLTV = itemView.findViewById(R.id.doctorSpecialistACDLTV);
            doctorStatusACDLTV = itemView.findViewById(R.id.doctorStatusACDLTV);
            callDoctorACDLIB = itemView.findViewById(R.id.callDoctorACDLIB);
        }
    }
}
