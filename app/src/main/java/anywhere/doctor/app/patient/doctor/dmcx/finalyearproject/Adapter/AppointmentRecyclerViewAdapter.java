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

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Appointment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class AppointmentRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRecyclerViewAdapter.AppointmentRecyclerViewHolder> {

    private Context context;
    private List<Appointment> appointments;

    public AppointmentRecyclerViewAdapter(Context context, List<Appointment> appointments) {
        this.appointments = appointments;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_single_appt, parent, false);
        return new AppointmentRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppointmentRecyclerViewHolder holder, int position) {

        // TODO: Update image with Picasso
        holder.apptDoctorPicIV.setImageResource(R.drawable.noperson);

        holder.apptDoctorNameTV.setText(appointments.get(position).getDoctorName());
        holder.apptLocationTV.setText(appointments.get(position).getLocation());
        holder.apptDateTimeTV.setText(appointments.get(position).getDateTime());
        holder.apptRemainerTV.setText(appointments.get(position).getRemainderTime());

        holder.apptDoneIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Done! Now you can delete the appontment.", Toast.LENGTH_SHORT).show();

                holder.apptTrashIB.setVisibility(View.VISIBLE);
                holder.apptCancelIB.setVisibility(View.GONE);
                holder.apptDoneIB.setVisibility(View.GONE);
            }
        });

        holder.apptCancelIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Appontment has been removed.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class AppointmentRecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView apptDoctorPicIV;
        private TextView apptDoctorNameTV;
        private TextView apptLocationTV;
        private TextView apptDateTimeTV;
        private TextView apptRemainerTV;
        private ImageButton apptCancelIB;
        private ImageButton apptDoneIB;
        private ImageButton apptTrashIB;

        AppointmentRecyclerViewHolder(View itemView) {
            super(itemView);

            apptDoctorPicIV = itemView.findViewById(R.id.apptDoctorPicIV);
            apptDoctorNameTV = itemView.findViewById(R.id.apptDoctorNameTV);
            apptLocationTV = itemView.findViewById(R.id.apptLocationTV);
            apptDateTimeTV = itemView.findViewById(R.id.apptDateTimeTV);
            apptRemainerTV = itemView.findViewById(R.id.apptRemainerTV);

            apptCancelIB = itemView.findViewById(R.id.apptCancelIB);
            apptDoneIB = itemView.findViewById(R.id.apptDoneIB);
            apptTrashIB = itemView.findViewById(R.id.apptTrashIB);
        }
    }
}
