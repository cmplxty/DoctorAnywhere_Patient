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
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AudioCallDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingDialog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.LoadingText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.Utils;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorListAudioCallRecyclerViewAdapter extends RecyclerView.Adapter<DoctorListAudioCallRecyclerViewAdapter.DoctorListAudioCallRecyclerViewHolder> {
    private List<AudioCallDoctor> audioCallDoctors = new ArrayList<>();

    public void setAcDoctors(List<AudioCallDoctor> audioCallDoctors) {
        this.audioCallDoctors = audioCallDoctors;
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
        holder.doctorNameACDLTV.setText(audioCallDoctors.get(position).getDoctor().getName());
        holder.doctorSpecialistACDLTV.setText(new StringBuilder("Specialist: ").append(audioCallDoctors.get(position).getDoctor().getSpecialist()));
        holder.doctorStatusACDLTV.setText(new StringBuilder("Status: ").append(Utils.firstCap(audioCallDoctors.get(position).getAudio_call_status())));

        final Doctor doctor = audioCallDoctors.get(position).getDoctor();

        holder.callDoctorACDLIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission())
                    return;

                LoadingDialog.start(LoadingText.PleaseWait);
                AudioCallController.SetCurrentDeviceId(new IAction() {
                    @Override
                    public void onCompleteAction(Object object) {
                        LoadingDialog.stop();

                        if ((Boolean) object)
                            ActivityTrigger.AudioCallActivity(doctor, Vars.ParentActivity.DOCTOR_LIST_AUDIO_CALL);
                        else
                            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.FailedToStoreDeviceId, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (audioCallDoctors.size() - 1 == position && audioCallDoctors.size() > 1) {
            holder.relativeLayoutLineRL.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return audioCallDoctors.size();
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
