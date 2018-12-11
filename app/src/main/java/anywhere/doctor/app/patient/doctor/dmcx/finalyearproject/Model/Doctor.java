package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.Validator;

public class Doctor implements Parcelable {

    private String id;
    private String about;
    private String chamber;
    private String city;
    private String country;
    private String degree;
    private String email;
    private String hospital;
    private String image_link;
    private String name;
    private String phone;
    private String rating;
    private String registration;
    private String specialist;

    public Doctor() {
    }

    protected Doctor(Parcel in) {
        id = in.readString();
        about = in.readString();
        chamber = in.readString();
        city = in.readString();
        country = in.readString();
        degree = in.readString();
        email = in.readString();
        hospital = in.readString();
        image_link = in.readString();
        name = in.readString();
        phone = in.readString();
        rating = in.readString();
        registration = in.readString();
        specialist = in.readString();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChamber() {
        return chamber;
    }

    public String getDegree() {
        return degree;
    }

    public String getHospital() {
        return hospital;
    }

    public String getRating() {
        return rating;
    }

    public String getRegistration() {
        return registration;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getCity() {
        return city;
    }

    public String getAbout() {
        return about;
    }

    public String getSpecialist() {
        return specialist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(about);
        parcel.writeString(chamber);
        parcel.writeString(city);
        parcel.writeString(country);
        parcel.writeString(degree);
        parcel.writeString(email);
        parcel.writeString(hospital);
        parcel.writeString(image_link);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(rating);
        parcel.writeString(registration);
        parcel.writeString(specialist);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + id + '\'' +
                ", about='" + about + '\'' +
                ", chamber='" + chamber + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", degree='" + degree + '\'' +
                ", email='" + email + '\'' +
                ", hospital='" + hospital + '\'' +
                ", image_link='" + image_link + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", rating='" + rating + '\'' +
                ", registration='" + registration + '\'' +
                ", specialist='" + specialist + '\'' +
                '}';
    }

    public boolean checkEmptyValues() {
        return  Validator.empty(about) &&
                Validator.empty(city) &&
                Validator.empty(name) &&
                Validator.empty(phone) &&
                Validator.empty(email) &&
                Validator.empty(rating) &&
                Validator.empty(degree) &&
                Validator.empty(country) &&
                Validator.empty(chamber) &&
                Validator.empty(hospital) &&
                Validator.empty(specialist) &&
                Validator.empty(image_link) &&
                Validator.empty(registration);
    }
}
