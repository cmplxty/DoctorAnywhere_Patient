package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Nurse;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class NurseRecyclerViewAdapter extends RecyclerView.Adapter<NurseRecyclerViewAdapter.NurseRecyclerViewHolder> {

    private List<Nurse> nurses = new ArrayList<>();

    public void setNurses(List<Nurse> nurses) {
        this.nurses = nurses;
    }

    @NonNull
    @Override
    public NurseRecyclerViewAdapter.NurseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_nurse, parent, false);
        return new NurseRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NurseRecyclerViewAdapter.NurseRecyclerViewHolder holder, int position) {

        if (!nurses.get(position).getImage_link().equals("")) {
            Picasso.with(RefActivity.refACActivity.get()).load(nurses.get(position).getImage_link()).placeholder(R.drawable.noperson).into(holder.sNursePicCIV);
        }
        holder.sNurseNameTV.setText(nurses.get(position).getName());
        holder.sNurseTypeTV.setText(nurses.get(position).getType());
        holder.sNurseGenderTV.setText(nurses.get(position).getGender());
        holder.sNurseDegreeTV.setText(nurses.get(position).getDegree());

        if (Objects.equals(position, nurses.size() - 1) && nurses.size() > 1) {
            holder.borderRL.setVisibility(View.GONE);
        }

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

        private CircleImageView sNursePicCIV;
        private TextView sNurseNameTV;
        private TextView sNurseGenderTV;
        private TextView sNurseTypeTV;
        private TextView sNurseDegreeTV;
        private RelativeLayout borderRL;

        NurseRecyclerViewHolder(View itemView) {
            super(itemView);

            sNursePicCIV = itemView.findViewById(R.id.sNursePicCIV);
            sNurseDegreeTV = itemView.findViewById(R.id.sNurseDegreeTV);
            sNurseNameTV = itemView.findViewById(R.id.sNurseNameTV);
            sNurseGenderTV = itemView.findViewById(R.id.sNurseGenderTV);
            sNurseTypeTV = itemView.findViewById(R.id.sNurseTypeTV);
            borderRL = itemView.findViewById(R.id.borderRL);
        }

    }
}
