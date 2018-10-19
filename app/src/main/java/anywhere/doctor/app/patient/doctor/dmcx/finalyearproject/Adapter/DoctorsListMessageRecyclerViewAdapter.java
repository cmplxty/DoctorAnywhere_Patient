package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DoctorsListMessageRecyclerViewAdapter extends RecyclerView.Adapter<DoctorsListMessageRecyclerViewAdapter.DoctorsListRecyclerViewHolder> {

    private List<Doctor> doctors;

    public DoctorsListMessageRecyclerViewAdapter(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public DoctorsListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_list_doctor_message, parent, false);
        return new DoctorsListRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsListRecyclerViewHolder holder, int position) {

        final int itemPosition = position;

        // TODO: set doctor picture -> picasso
        holder.doctorCptPicIV.setImageResource(R.drawable.noperson);

        holder.doctorCptNameTV.setText(doctors.get(itemPosition).getName());
        holder.doctorCptSpecialistTV.setText(new StringBuilder("Specialist: ").append(doctors.get(itemPosition).getSpecialist()));
        holder.doctorCptLocationTV.setText(new StringBuilder(doctors.get(itemPosition).getCity() + ", ").append(doctors.get(itemPosition).getCountry()));
        holder.doctorCptRatingTV.setText(doctors.get(itemPosition).getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.MessageActivity(doctors.get(itemPosition), Vars.ActivityOverrider.FROM_DOCTOR_LIST_ACTIVITY);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    class DoctorsListRecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView doctorCptPicIV;
        private TextView doctorCptNameTV;
        private TextView doctorCptSpecialistTV;
        private TextView doctorCptLocationTV;
        private TextView doctorCptRatingTV;

        DoctorsListRecyclerViewHolder(View itemView) {
            super(itemView);

            doctorCptPicIV = itemView.findViewById(R.id.doctorCptPicIV);
            doctorCptNameTV = itemView.findViewById(R.id.doctorCptNameTV);
            doctorCptSpecialistTV = itemView.findViewById(R.id.doctorCptSpecialistTV);
            doctorCptLocationTV = itemView.findViewById(R.id.doctorCptLocationTV);
            doctorCptRatingTV = itemView.findViewById(R.id.doctorCptRatingTV);

            doctorCptNameTV.setSingleLine(true);
            doctorCptSpecialistTV.setSingleLine(true);
            doctorCptLocationTV.setSingleLine(true);
            doctorCptRatingTV.setSingleLine(true);
        }
    }
}
