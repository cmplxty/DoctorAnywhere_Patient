package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class BloodDonor implements Parcelable{

    private String id = "";
    private String name = "";
    private String age = "";
    private String phone = "";
    private String city = "";
    private String gender = "";
    private String group = "";
    private String weight = "";
    private String available = ""; // enable / disable
    private String last_donation_date = "";
    private String timestamp = "";

    public BloodDonor() {}

    protected BloodDonor(Parcel in) {
        id = in.readString();
        name = in.readString();
        age = in.readString();
        phone = in.readString();
        city = in.readString();
        gender = in.readString();
        group = in.readString();
        weight = in.readString();
        available = in.readString();
        last_donation_date = in.readString();
        timestamp = in.readString();
    }

    public static final Creator<BloodDonor> CREATOR = new Creator<BloodDonor>() {
        @Override
        public BloodDonor createFromParcel(Parcel in) {
            return new BloodDonor(in);
        }

        @Override
        public BloodDonor[] newArray(int size) {
            return new BloodDonor[size];
        }
    };

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLast_donation_date() {
        return last_donation_date;
    }

    public void setLast_donation_date(String last_donation_date) {
        this.last_donation_date = last_donation_date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(age);
        parcel.writeString(phone);
        parcel.writeString(city);
        parcel.writeString(gender);
        parcel.writeString(group);
        parcel.writeString(weight);
        parcel.writeString(available);
        parcel.writeString(last_donation_date);
        parcel.writeString(timestamp);
    }
}
