package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model;

import java.util.List;

public class AppointmentDoctor {

    private String id;
    private String name;
    private String specialist;
    private String phone;
    private String clinic;
    private String email;
    private String appointments;
    private List<Appointment> appointmentsList;

    public AppointmentDoctor() {}

    public List<Appointment> getAppointmentsList() {
        return appointmentsList;
    }

    public void setAppointmentsList(List<Appointment> appointmentsList) {
        this.appointmentsList = appointmentsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppointments() {
        return appointments;
    }

    public void setAppointments(String appointments) {
        this.appointments = appointments;
    }

}
