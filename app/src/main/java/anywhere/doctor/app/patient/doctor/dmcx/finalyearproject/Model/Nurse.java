package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model;

public class Nurse {

    private String id;
    private String name;
    private String gender;
    private String type;
    private String reference;
    private String about;

    public Nurse(String id, String name, String gender, String type, String reference, String about) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.type = type;
        this.reference = reference;
        this.about = about;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getType() {
        return type;
    }

    public String getReference() {
        return reference;
    }

    public String getAbout() {
        return about;
    }
}
