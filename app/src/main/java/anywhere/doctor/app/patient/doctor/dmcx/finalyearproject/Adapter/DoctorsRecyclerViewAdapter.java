package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorsRecyclerViewAdapter extends RecyclerView.Adapter<DoctorsRecyclerViewAdapter.DoctorsRecyclerViewHolder> {

    private List<Doctor> doctors;
    private Context context;

    public DoctorsRecyclerViewAdapter(Context context, List<Doctor> doctors) {
        this.context = context;
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public DoctorsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_single_doctor, parent, false);
        return new DoctorsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsRecyclerViewHolder holder, int position) {

        final int itemPosition = position;

        // TODO: set doctor picture -> picasso
        holder.doctorPicIV.setImageResource(R.drawable.noperson);

        holder.doctorNameTV.setText(doctors.get(itemPosition).getName());
        holder.doctorDegreeTV.setText(doctors.get(itemPosition).getDegree());
        holder.doctorSpecialistTV.setText(new StringBuilder("Specialist: ").append(doctors.get(itemPosition).getSpecialist()));

        holder.messageDBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Show Payment
                ActivityTrigger.MessageActivity(doctors.get(itemPosition), Vars.ActivityOverrider.FROM_HOME_ACTIVITY);
            }
        });

        holder.profileDBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.DoctorProfileActivity(doctors.get(itemPosition));
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    class DoctorsRecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView doctorPicIV;
        private TextView doctorNameTV;
        private TextView doctorDegreeTV;
        private TextView doctorSpecialistTV;
        private Button messageDBTN;
        private Button profileDBTN;

        DoctorsRecyclerViewHolder(View itemView) {
            super(itemView);

            doctorPicIV = itemView.findViewById(R.id.doctorPicIV);

            doctorNameTV = itemView.findViewById(R.id.doctorNameTV);
            doctorDegreeTV = itemView.findViewById(R.id.doctorDegreeTV);
            doctorSpecialistTV = itemView.findViewById(R.id.doctorSpecialistTV);

            messageDBTN = itemView.findViewById(R.id.messageDBTN);
            profileDBTN = itemView.findViewById(R.id.profileDBTN);

            doctorNameTV.setSingleLine(true);
            doctorDegreeTV.setSingleLine(true);
            doctorSpecialistTV.setSingleLine(true);
        }
    }
}
