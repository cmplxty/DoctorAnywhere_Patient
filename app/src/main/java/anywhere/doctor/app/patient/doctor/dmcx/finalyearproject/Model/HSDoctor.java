package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model;

public class HSDoctor {

    private String id;
    private String name;
    private String time;
    private String location;
    private String phone;
    private String specialist;

    public HSDoctor() {
    }

    public HSDoctor(String name, String time, String location, String phone, String specialist) {
        this.name = name;
        this.time = time;
        this.location = location;
        this.phone = phone;
        this.specialist = specialist;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public String getSpecialist() {
        return specialist;
    }
}
