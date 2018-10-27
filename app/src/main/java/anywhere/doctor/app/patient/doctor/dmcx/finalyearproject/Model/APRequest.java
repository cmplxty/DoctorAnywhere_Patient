package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model;

public class APRequest {

    private String doctor_name;
    private String doctor_clinic;
    private String doctor_phone;
    private String doctor_id;
    private String patient_phone;
    private String patient_name;
    private String time;
    private String date;
    private String status;
    private String timestamp;

    public APRequest() {
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
