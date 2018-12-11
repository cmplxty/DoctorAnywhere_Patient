package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.TimeSpinner;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class AppointmentTimeArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> times;

    AppointmentTimeArrayAdapter(@NonNull Context context, @NonNull List<String> times) {
        super(context, 0, times);

        this.context = context;
        this.times = times;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_appointment_time, parent, false);
        }

        TextView text = convertView.findViewById(R.id.text);
        text.setText(times.get(position));

        return convertView;
    }
}
