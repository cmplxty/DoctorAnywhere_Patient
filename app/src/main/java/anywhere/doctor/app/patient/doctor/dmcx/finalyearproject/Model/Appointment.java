package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model;

public class Appointment {

    private String days;
    private String time;

    public Appointment() {
    }

    public Appointment(String days, String time) {
        this.days = days;
        this.time = time;
    }

    public String getDays() {
        return days;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "days='" + days + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
