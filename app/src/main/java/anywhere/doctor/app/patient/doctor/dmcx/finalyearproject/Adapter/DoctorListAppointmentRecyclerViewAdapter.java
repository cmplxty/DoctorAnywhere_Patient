package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.security.PrivateKey;
import java.sql.Ref;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.AppointmentController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.ProfileController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase.AFModel;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AppointmentDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Appointment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;

public class DoctorListAppointmentRecyclerViewAdapter extends RecyclerView.Adapter<DoctorListAppointmentRecyclerViewAdapter.DoctorListAppointmentRecyclerViewHolder> implements DatePickerDialog.OnDateSetListener {

    private List<AppointmentDoctor> appointmentDoctors = new ArrayList<>();
    private List<String> gDays = new ArrayList<>();
    private List<String> gTimes = new ArrayList<>();
    private boolean isPatientSendAppointmentRequest;
    private boolean allowDismissDateDialog;
    private int lastPosition;

    public void setApDoctors(List<AppointmentDoctor> appointmentDoctors) {
        this.appointmentDoctors = appointmentDoctors;
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

    private void setConsultDate() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dateFragment = new DatePickerDialog(
                RefActivity.refACActivity.get(),
                R.style.AppTheme_DateTimePicker,
                this,
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

        dateFragment.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dateFragment.show();
    }

    private void openAppointmentDialog(final String date, final List<String> times) {
        View dialogAppointment = LayoutInflater.from(RefActivity.refACActivity.get())
                .inflate(R.layout.dialog_add_new_appointment, null);

        TextView nameTV = dialogAppointment.findViewById(R.id.nameTV);
        TextView phoneTV = dialogAppointment.findViewById(R.id.phoneTV);
        TextView dateTV = dialogAppointment.findViewById(R.id.dateTV);
        final Spinner timeSPN = dialogAppointment.findViewById(R.id.timeSPN);
        Button saveApptBTN = dialogAppointment.findViewById(R.id.saveApptBTN);
        ImageButton closeIB = dialogAppointment.findViewById(R.id.closeIB);

        String phone = ProfileController.GetLocalProfile().getPhone() == null || ProfileController.GetLocalProfile().getPhone().equals(AFModel.deflt)
                ? "No Phone" : ProfileController.GetLocalProfile().getPhone();

        nameTV.setText(ProfileController.GetLocalProfile().getName());
        phoneTV.setText(new StringBuilder("Phone: ").append(phone));
        dateTV.setText(new StringBuilder("Date: ").append(date));

        AppointmentTimeArrayAdapter adapter = new AppointmentTimeArrayAdapter(RefActivity.refACActivity.get(), times);
        timeSPN.setAdapter(adapter);

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
                String did = appointmentDoctors.get(lastPosition).getId();
                String time = timeSPN.getSelectedItem().toString();

                AppointmentController.SendAppointmentRequest(name, thePhone, date, time, did, appointmentDoctors.get(lastPosition));
            }
        });

        closeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apptDialog.dismiss();
            }
        });
    }

    private void checkAndSetAddCancelAppointButton(final DoctorListAppointmentRecyclerViewHolder holder, final int position, final List<String> days, final List<String> times) {
        holder.mLoadingRL.start();
        holder.addCancelAppointmentLDAIB.setVisibility(View.INVISIBLE);

        AppointmentController.CheckAppointmentIsSent(appointmentDoctors.get(position).getId(), new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                holder.mLoadingRL.stop();

                if (!(Boolean) object) {
                    holder.addCancelAppointmentLDAIB.setVisibility(View.VISIBLE);
                    holder.addCancelAppointmentLDAIB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            lastPosition = position;
                            gDays = days;
                            gTimes = times;

                            setConsultDate();
                        }
                    });
                } else {
                    holder.addCancelAppointmentLDAIB.setVisibility(View.INVISIBLE);
                }
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

    private List<String> timeRangeGenerator(List<String> dayList, List<String> timeList, String day) {
        List<String> times = new ArrayList<>();
        int index = 0;
        for (String theDays : dayList) {
            if (theDays.contains(day)) {
                times.add(timeList.get(index));
            }

            index++;
        }

        return times;
    }

    @NonNull
    @Override
    public DoctorListAppointmentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_list_doctor_appointment, parent, false);
        return new DoctorListAppointmentRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorListAppointmentRecyclerViewHolder holder, int position) {
        holder.doctorNameLDATV.setText(appointmentDoctors.get(position).getName());
        holder.doctorSpecialistLDATV.setText(new StringBuilder("Specialist: ").append(appointmentDoctors.get(position).getSpecialist()));
        holder.doctorClinicLDATV.setText(new StringBuilder("Clinic: ").append(appointmentDoctors.get(position).getClinic()));
        holder.addCancelAppointmentLDAIB.setVisibility(View.INVISIBLE);
        holder.appointmentListLDALL.removeAllViews();

        final List<String> days = new ArrayList<>();
        final List<String> times = new ArrayList<>();
        List<Appointment> appointments = appointmentDoctors.get(position).getAppointmentsList();
        int i = 0;
        if (appointments != null) {
            for (Appointment appointment : appointments) {
                i++;

                @SuppressLint("InflateParams")
                View field = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.field_single_doctor_appointment_schedule, null);
                LinearLayout fieldLayout = field.findViewById(R.id.fieldLayout);
                TextView daysTV = field.findViewById(R.id.daysTV);
                TextView timeTV = field.findViewById(R.id.timeTV);

                daysTV.setText(appointment.getDays());
                timeTV.setText(appointment.getTime());
                holder.appointmentListLDALL.addView(field, holder.appointmentListLDALL.getChildCount());

                days.add(appointment.getDays());
                times.add(appointment.getTime());

                if (appointments.size() == i) {
                    fieldLayout.setBackground(null);
                }
            }
        }

        checkAndSetAddCancelAppointButton(holder, position, days, times);
    }

    @Override
    public int getItemCount() {
        return appointmentDoctors.size();
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

            openAppointmentDialog(date, timeRangeGenerator(gDays, gTimes, day));
        } else {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.NotAvailable, Toast.LENGTH_SHORT).show();
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
