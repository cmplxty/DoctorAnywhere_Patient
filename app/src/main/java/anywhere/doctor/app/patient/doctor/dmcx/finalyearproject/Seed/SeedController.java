package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Seed;

public class SeedController {

    public static void SeedAppointment() {
        Seeder.Instance().create().showToast().seedAppointment();
    }

    public static void SeedUserMessage() {
        Seeder.Instance().create().showToast().seedMessageUser();
    }

    public static void RemoveUserMessage() {
        Seeder.Instance().create().seedMessageUserRemove();
    }

}
