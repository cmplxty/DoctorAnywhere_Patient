package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        holder.doctorLocationTV.setText(new StringBuilder(doctors.get(itemPosition).getCity() + ", ").append(doctors.get(itemPosition).getCountry()));
        holder.doctorRatingTV.setText(doctors.get(itemPosition).getRating());

        holder.doctorMessageIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Show Payment
                ActivityTrigger.MessageActivity(doctors.get(itemPosition), Vars.ActivityOverrider.FROM_HOME_ACTIVITY);
            }
        });

        holder.doctorAppointmentIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "GO TO APPOINTMENT", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "GO TO DOCTORS PAGE", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class DoctorsRecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView doctorPicIV;
        private TextView doctorNameTV;
        private TextView doctorDegreeTV;
        private TextView doctorSpecialistTV;
        private TextView doctorLocationTV;
        private TextView doctorRatingTV;
        private ImageButton doctorMessageIB;
        private ImageButton doctorAppointmentIB;

        public DoctorsRecyclerViewHolder(View itemView) {
            super(itemView);

            doctorPicIV = itemView.findViewById(R.id.doctorPicIV);

            doctorNameTV = itemView.findViewById(R.id.doctorNameTV);
            doctorDegreeTV = itemView.findViewById(R.id.doctorDegreeTV);
            doctorSpecialistTV = itemView.findViewById(R.id.doctorSpecialistTV);
            doctorLocationTV = itemView.findViewById(R.id.doctorLocationTV);
            doctorRatingTV = itemView.findViewById(R.id.doctorRatingTV);

            doctorMessageIB = itemView.findViewById(R.id.doctorMessageIB);
            doctorAppointmentIB = itemView.findViewById(R.id.doctorAppointmentIB);

            doctorNameTV.setSingleLine(true);
            doctorDegreeTV.setSingleLine(true);
            doctorSpecialistTV.setSingleLine(true);
            doctorLocationTV.setSingleLine(true);
            doctorRatingTV.setSingleLine(true);
        }
    }
}
