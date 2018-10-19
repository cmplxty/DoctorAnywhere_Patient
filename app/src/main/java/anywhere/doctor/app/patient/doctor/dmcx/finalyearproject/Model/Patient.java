package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model;

public class Patient {

    private String id;
    private String name;
    private String email;
    private String link;
    private String phone;
    private String address;
    private String country;
    private String gender;
    private String dob;
    private String age;
    private String type;

    public void setId(String id) {
        this.id = id;
    }

    public String getDob() {
        return dob;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLink() {
        return link;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getType() {
        return type;
    }
}
