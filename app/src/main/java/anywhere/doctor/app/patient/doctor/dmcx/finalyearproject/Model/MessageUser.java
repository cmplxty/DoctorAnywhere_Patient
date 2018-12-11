package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model;

public class MessageUser {

    private String content;
    private String doctor;
    private String patient;
    private String timestamp;
    private String type;
    private String notification_status;

    public String getNotification_status() {
        return notification_status;
    }

    public String getContent() {
        return content;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getPatient() {
        return patient;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }
}
