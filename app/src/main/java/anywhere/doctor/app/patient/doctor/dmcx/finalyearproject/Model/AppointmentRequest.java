package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model;

public class AppointmentRequest {

    private String doctor_name = "";
    private String doctor_clinic = "";
    private String doctor_phone = "";
    private String doctor_id = "";
    private String patient_phone = "";
    private String patient_name = "";
    private String time = "";
    private String date = "";
    private String status = "";
    private String notification_status = "";
    private String timestamp = "";

    public AppointmentRequest() {
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public void setDoctor_clinic(String doctor_clinic) {
        this.doctor_clinic = doctor_clinic;
    }

    public void setDoctor_phone(String doctor_phone) {
        this.doctor_phone = doctor_phone;
    }

    public void setPatient_phone(String patient_phone) {
        this.patient_phone = patient_phone;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public void setNotification_status(String notification_status) {
        this.notification_status = notification_status;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getDoctor_clinic() {
        return doctor_clinic;
    }

    public String getDoctor_phone() {
        return doctor_phone;
    }

    public String getPatient_phone() {
        return patient_phone;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
