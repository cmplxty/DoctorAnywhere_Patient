package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Appointment.DoctorListAppointmentActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.AudioCall.AudioCallActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.AudioCall.AudioCallHistoryActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.AudioCall.DoctorListAudioCallActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blog.BlogActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blog.BlogViewerActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blood.MyBloodDonorActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blood.MyBloodPostActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blood.CreateBloodDonorActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blood.BloodDonorActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blood.NewBloodPostActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeService.DoctorListHomeServiceActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeService.HomeServiceListActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger.DoctorsListMessageActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger.PrescriptionActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger.MessageUserListActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger.ViewImageActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger.MessageActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Nurse.NurseActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Prescription.PrescriptionListActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Blog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodDonor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class ActivityTrigger {

    /*
    * Auth Activity
    * */
    public static void AuthActivity() {
        Activity activity = RefActivity.refACActivity.get();
        activity.startActivity(new Intent(RefActivity.refACActivity.get(), AuthActivity.class));
        activity.finish();
    }

    /*
    * Message Activity
    * */
    public static void MessageActivity(Parcelable parcelable, String prevActivity) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, MessageActivity.class);
        intent.putExtra(Vars.ActivityOverrider.OPEN_MESSAGE_ACTIVITY, prevActivity);
        intent.putExtra(Vars.Connector.MESSAGE_ACTIVITY_DATA, parcelable);
        activity.startActivity(intent);
    }

    /*
     * Home Activity
     * */
    public static void HomeActivity() {
        Activity activity = RefActivity.refACActivity.get();
        activity.startActivity(new Intent(activity, HomeActivity.class));
        activity.finish();
    }

    /*
     * View Image Activity
     * */
    public static void ViewImageActivity(String url) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, ViewImageActivity.class);
        intent.putExtra(Vars.Connector.VIEW_IMAGE_DATA, url);
        activity.startActivity(intent);
    }

    /*
     * Prescription Activity
     * */
    public static void PrescriptionActivity(Parcelable parcelable) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, PrescriptionActivity.class);
        intent.putExtra(Vars.Connector.PERSCRIPTION_ACTIVITY_DATA, parcelable);
        activity.startActivity(intent);
    }

    /*
     * Prescription List Activity
     * */
    public static void PrescriptionListActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, PrescriptionListActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Nurse Activity
     * */
    public static void NurseActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, NurseActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Message List Activity
     * */
    public static void MessageListActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, MessageUserListActivity.class);
        activity.startActivity(intent);
    }

    /*
    * Doctor List Message Activity
    * */
    public static void DoctorListMessageActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, DoctorsListMessageActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Doctor List Home Service Activity
     * */
    public static void DoctorListHomeServiceActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, DoctorListHomeServiceActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Home Service List Activity
     * */
    public static void HomeServiceListActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, HomeServiceListActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Appointment List Activity
     * */
    public static void DoctorListAppointmentActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, DoctorListAppointmentActivity.class);
        activity.startActivity(intent);
    }

    /*
    * Doctor Profile
    * */
    public static void DoctorProfileActivity(Parcelable parcelable) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, DoctorProfileActivity.class);
        intent.putExtra(Vars.Connector.DOCTOR_PROFILE_ACTIVITY_DATA, parcelable);
        activity.startActivity(intent);
    }

    /*
     * Audio Call List Activity
     * */
    public static void DoctorListAudioCallctivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, DoctorListAudioCallActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Audio Call Activity
     * */
    public static void AudioCallActivity(Doctor doctor, String parentActivity) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, AudioCallActivity.class);
        intent.putExtra(Vars.Connector.AUDIO_CALL_ACTIVITY_DATA, doctor);
        intent.putExtra(Vars.ParentActivity.TRIG_AUDIO_CALL_ACTIVITY, parentActivity);
        activity.startActivity(intent);
    }

    /*
     * Blog Activity
     * */
    public static void BlogActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, BlogActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Blog Viewer Activity
     * */
    public static void BlogViewerActivity(String parentActivity, Blog blog) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, BlogViewerActivity.class);
        intent.putExtra(Vars.ParentActivity.TRIG_BLOG_VIEWER_ACTIVITY, parentActivity);
        intent.putExtra(Vars.Connector.BLOG_VIEWER_ACTIVITY_DATA, blog);
        activity.startActivity(intent);
    }

    /*
     * Blood Donor Activity
     * */
    public static void BloodDonorActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, BloodDonorActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Blood Donor Activity
     * */
    public static void AddNewBloodDonorActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, CreateBloodDonorActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Blood Post Activity
     * */
    public static void NewBloodPostActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, NewBloodPostActivity.class);
        activity.startActivity(intent);
    }

    /*
     * My Blood Donor Activity
     * */
    public static void MyBloodDonorActivity(BloodDonor bloodDonor) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, MyBloodDonorActivity.class);
        intent.putExtra(Vars.Connector.MY_BLOOD_DONOR_ACTIVITY_DATA, bloodDonor);
        activity.startActivity(intent);
    }

    /*
     * My Blood Donor Activity
     * */
    public static void MyBloodPostActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, MyBloodPostActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Audio Call History Activity
     * */
    public static void AudioCallHistoryActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, AudioCallHistoryActivity.class);
        activity.startActivity(intent);
    }
}
