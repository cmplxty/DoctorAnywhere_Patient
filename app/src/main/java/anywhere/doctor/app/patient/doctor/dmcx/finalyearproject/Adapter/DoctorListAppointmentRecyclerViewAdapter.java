package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victor.loading.rotate.RotateLoading;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AppointmentController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.ProfileController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.APDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Appointment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class DoctorListAppointmentRecyclerViewAdapter extends RecyclerView.Adapter<DoctorListAppointmentRecyclerViewAdapter.DoctorListAppointmentRecyclerViewHolder> implements DatePickerDialog.OnDateSetListener {

    private List<APDoctor> apDoctors = new ArrayList<>();
    private List<String> gDays = new ArrayList<>();
    private List<String> gTimes = new ArrayList<>();
    private boolean allowDismissDateDialog;
    private int lastPosition;
    private boolean isPatientSendAppointmentRequest;

    public void setApDoctors(List<APDoctor> apDoctors) {
        this.apDoctors = apDoctors;
    }

    private int getDay(String day) {
        switch (day) {
            case "SAT":
                return Calendar.SATURDAY;
            case "SUN":
                return Calendar.SUNDAY;
            case "MON":
                return Calendar.MONDAY;
            case "TUE":
                return Calendar.TUESDAY;
            case "WED":
                return Calendar.WEDNESDAY;
            case "THU":
                return Calendar.THURSDAY;
            case "FRI":
                return Calendar.FRIDAY;
            default:
                return 0;
        }
    }

    private void setConsultDate(List<String> days) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dateFragment = new DatePickerDialog(
                RefActivity.refACActivity.get(),
                R.style.AppTheme_DateTimePicker,
                DoctorListAppointmentRecyclerViewAdapter.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ) {
            @Override
            public void onClick(@NonNull DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    allowDismissDateDialog = false;
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    allowDismissDateDialog = true;
                }

                super.onClick(dialog, which);
            }

            @Override
            public void onBackPressed() {
                allowDismissDateDialog = true;
                super.onBackPressed();
            }

            @Override
            public void dismiss() {
                if (allowDismissDateDialog)
                    super.dismiss();
            }
        };

        List<String> spilledDays = new ArrayList<>();
        for (String day : days) {
            Collections.addAll(spilledDays, day.split(" "));
        }

        String formattedDays = "";
        for (String day : spilledDays) {
            day = day.toLowerCase().substring(0, 1).toUpperCase().concat(day.toLowerCase().substring(1));
            if (day.toLowerCase().equals(spilledDays.get(spilledDays.size() - 1).toLowerCase()))
                formattedDays = formattedDays.concat(day);
            else
                formattedDays = formattedDays.concat(day) + " - ";
        }
        dateFragment.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dateFragment.setMessage(new StringBuilder("Days: ").append(formattedDays));
        dateFragment.show();
    }

    private void openAppointmentDialog(final String date, final String time) {
        View dialogAppointment = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.dialog_add_new_appointment, null);
        TextView nameTV = dialogAppointment.findViewById(R.id.nameTV);
        TextView phoneTV = dialogAppointment.findViewById(R.id.phoneTV);
        TextView dateTV = dialogAppointment.findViewById(R.id.dateTV);
        TextView timeTV = dialogAppointment.findViewById(R.id.timeTV);
        Button saveApptBTN = dialogAppointment.findViewById(R.id.saveApptBTN);
        ImageButton closeIB = dialogAppointment.findViewById(R.id.closeIB);

        String phone = ProfileController.GetLocalProfile().getPhone() == null || ProfileController.GetLocalProfile().getPhone().equals(AFModel.deflt)
                            ? "No Phone" : ProfileController.GetLocalProfile().getPhone();

        nameTV.setText(ProfileController.GetLocalProfile().getName());
        phoneTV.setText(phone);
        dateTV.setText(new StringBuilder("Date: ").append(date));
        timeTV.setText(new StringBuilder("Consult: ").append(time));

        AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
        builder.setView(dialogAppointment);

        final AlertDialog apptDialog = builder.create();
        apptDialog.show();

        // Events
        saveApptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apptDialog.dismiss();

                String name = ProfileController.GetLocalProfile().getName();
                String thePhone = ProfileController.GetLocalProfile().getPhone();
                String did = apDoctors.get(lastPosition).getId();

                AppointmentController.SendAppointmentRequest(name, thePhone, date, time, did, apDoctors.get(lastPosition));
            }
        });

        closeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apptDialog.dismiss();
            }
        });
    }

    private void checkAppointmentIsSent(final DoctorListAppointmentRecyclerViewHolder holder, final int position) {
        holder.mLoadingRL.start();
        holder.addCancelAppointmentLDAIB.setVisibility(View.INVISIBLE);

        AppointmentController.CheckAppointmentIsSent(apDoctors.get(position).getId(), new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                holder.mLoadingRL.stop();

                isPatientSendAppointmentRequest = (boolean) object;
                if (!isPatientSendAppointmentRequest) holder.addCancelAppointmentLDAIB.setVisibility(View.VISIBLE);
                else holder.addCancelAppointmentLDAIB.setVisibility(View.INVISIBLE);
            }
        });
    }

    private String getRangeTime(List<String> days, String day) {
        int index = 0;
        for (String theDay : days) {
            if (theDay.contains(day)) {
                break;
            }
            index++;
        }
        return gTimes.get(index);
    }

    @NonNull
    @Override
    public DoctorListAppointmentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_list_doctor_appointment, parent, false);
        return new DoctorListAppointmentRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorListAppointmentRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        holder.doctorNameLDATV.setText(apDoctors.get(position).getName());
        holder.doctorSpecialistLDATV.setText(new StringBuilder("Specialist: ").append(apDoctors.get(position).getSpecialist()));
        holder.doctorClinicLDATV.setText(new StringBuilder("Clinic: ").append(apDoctors.get(position).getClinic()));

        Gson gson = new Gson();
        final List<String> days = new ArrayList<>();
        final List<String> times = new ArrayList<>();
        List<Appointment> appointments = gson.fromJson(apDoctors.get(position).getAppointments(), new TypeToken<List<Appointment>>(){}.getType());
        if (appointments != null) {
            for (Appointment appointment : appointments) {
                View field = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.field_single_doctor_appointment_schedule, null);
                TextView daysTV = field.findViewById(R.id.daysTV);
                TextView timeTV = field.findViewById(R.id.timeTV);

                daysTV.setText(appointment.getDays());
                timeTV.setText(appointment.getTime());
                holder.appointmentListLDALL.addView(field, holder.appointmentListLDALL.getChildCount());

                days.add(appointment.getDays());
                times.add(appointment.getTime());
            }
        }

        checkAppointmentIsSent(holder, itemPosition);
        holder.addCancelAppointmentLDAIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPosition = itemPosition;
                gDays = days;
                gTimes = times;

                if (!isPatientSendAppointmentRequest) setConsultDate(days);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apDoctors.size();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        List<String> splitted = new ArrayList<>();
        List<Integer> schedules = new ArrayList<>();
        for (String day : gDays)
            Collections.addAll(splitted, day.split(" "));
        for (String day : splitted)
            schedules.add(getDay(day));

        int week = calendar.get(Calendar.DAY_OF_WEEK);
        allowDismissDateDialog = schedules.contains(week);

        if (allowDismissDateDialog) {
            String date = DateFormat.getDateInstance().format(new Date(calendar.getTimeInMillis()));
            String day = splitted.get(schedules.indexOf(week));
            openAppointmentDialog(date, getRangeTime(gDays, day));
        }
    }

    class DoctorListAppointmentRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView doctorNameLDATV;
        private TextView doctorSpecialistLDATV;
        private TextView doctorClinicLDATV;
        private ImageButton addCancelAppointmentLDAIB;
        private LinearLayout appointmentListLDALL;
        private RotateLoading mLoadingRL;

        DoctorListAppointmentRecyclerViewHolder(View itemView) {
            super(itemView);

            doctorNameLDATV = itemView.findViewById(R.id.doctorNameLDATV);
            doctorSpecialistLDATV = itemView.findViewById(R.id.doctorSpecialistLDATV);
            doctorClinicLDATV = itemView.findViewById(R.id.doctorClinicLDATV);
            addCancelAppointmentLDAIB = itemView.findViewById(R.id.addCancelAppointmentLDAIB);
            appointmentListLDALL = itemView.findViewById(R.id.appointmentListLDALL);
            mLoadingRL = itemView.findViewById(R.id.mLoadingRL);
        }
    }
}
