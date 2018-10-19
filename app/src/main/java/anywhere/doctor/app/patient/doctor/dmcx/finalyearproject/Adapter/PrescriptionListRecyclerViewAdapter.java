package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Prescription;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class PrescriptionListRecyclerViewAdapter extends RecyclerView.Adapter<PrescriptionListRecyclerViewAdapter.PrescriptionListRecyclerViewHolder> {

    private Context context;
    private List<Prescription> prescriptions = new ArrayList<>();

    public PrescriptionListRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setPrescription(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    @NonNull
    @Override
    public PrescriptionListRecyclerViewAdapter.PrescriptionListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_single_prescription, parent, false);
        return new PrescriptionListRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionListRecyclerViewAdapter.PrescriptionListRecyclerViewHolder holder, int position) {

        final int itemPosition = position;

        holder.pDoctorsNamePTV.setText(prescriptions.get(position).getDoctor_name());
        holder.pDatePTV.setText(prescriptions.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.PrescriptionActivity(prescriptions.get(itemPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return prescriptions.size();
    }

    class PrescriptionListRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView pDoctorsNamePTV;
        private TextView pDatePTV;

        PrescriptionListRecyclerViewHolder(View itemView) {
            super(itemView);

            pDoctorsNamePTV = itemView.findViewById(R.id.pDoctorsNamePTV);
            pDatePTV = itemView.findViewById(R.id.pDatePTV);
        }
    }
}
