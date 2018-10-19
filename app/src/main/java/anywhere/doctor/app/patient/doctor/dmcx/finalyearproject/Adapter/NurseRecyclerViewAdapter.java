package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Nurse;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class NurseRecyclerViewAdapter extends RecyclerView.Adapter<NurseRecyclerViewAdapter.NurseRecyclerViewHolder> {

    private Context context;
    private List<Nurse> nurses;

    public NurseRecyclerViewAdapter(Context context, List<Nurse> nurses) {
        this.context = context;
        this.nurses = nurses;
    }

    @NonNull
    @Override
    public NurseRecyclerViewAdapter.NurseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_single_nurse, parent, false);
        return new NurseRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NurseRecyclerViewAdapter.NurseRecyclerViewHolder holder, int position) {

        holder.sNurseNameTV.setText(nurses.get(position).getName());
        holder.sNurseTypeTV.setText(nurses.get(position).getType());
        holder.sNurseGenderTV.setText(nurses.get(position).getGender());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return nurses.size();
    }

    class NurseRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView sNurseNameTV;
        private TextView sNurseGenderTV;
        private TextView sNurseTypeTV;

        NurseRecyclerViewHolder(View itemView) {
            super(itemView);

            sNurseNameTV = itemView.findViewById(R.id.sNurseNameTV);
            sNurseGenderTV = itemView.findViewById(R.id.sNurseGenderTV);
            sNurseTypeTV = itemView.findViewById(R.id.sNurseTypeTV);
        }

    }
}
