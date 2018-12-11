package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Appointment;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AudioCallDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AppointmentDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AppointmentRequest;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.AudioCallHistory;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Blog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodDonor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.BloodPost;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HomeServiceDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HomeService;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.MessageUser;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Nurse;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Prescription;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class SearchController implements MaterialSearchView.SearchViewListener, MaterialSearchView.OnQueryTextListener {

    private static SearchController instance;
    private static ISearch iSearch;
    private static String page;

    public static void setPage(String page) {
        SearchController.page = page;
    }

    public static void setiSearch(ISearch iSearch) {
        SearchController.iSearch = iSearch;
    }

    public static SearchController getInstance() {
        if (instance == null)
            instance = new SearchController();

        return instance;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        switch (page) {
            case Vars.Search.PAGE_HOME_DOCTOR: {
                if (newText == null || newText.isEmpty()) {
                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<Doctor> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof Doctor) {
                            Doctor doctor = (Doctor) object;
                            if (doctor.getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    doctor.getSpecialist().toLowerCase().contains(newText.toLowerCase()) ||
                                    doctor.getCity().toLowerCase().contains(newText.toLowerCase()) ||
                                    doctor.getChamber().toLowerCase().contains(newText.toLowerCase()) ||
                                    doctor.getHospital().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(doctor);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_HOME_APPOINTMENT: {
                if (newText == null || newText.isEmpty()) {
                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<AppointmentRequest> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof AppointmentRequest) {
                            AppointmentRequest appointmentRequest = (AppointmentRequest) object;
                            if (appointmentRequest.getDate().toLowerCase().contains(newText.toLowerCase()) ||
                                    appointmentRequest.getDoctor_name().toLowerCase().contains(newText.toLowerCase()) ||
                                    appointmentRequest.getDoctor_clinic().toLowerCase().contains(newText.toLowerCase()) ||
                                    appointmentRequest.getStatus().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(appointmentRequest);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_PRESCRIPTION_LIST: {
                if (newText == null || newText.isEmpty()) {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<Prescription> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof Prescription) {
                            Prescription prescription = (Prescription) object;
                            if (prescription.getDate().toLowerCase().contains(newText.toLowerCase()) ||
                                    prescription.getDoctor_name().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(prescription);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_MESSAGE_USER_LIST: {
                if (newText == null || newText.isEmpty()) {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<List<?>> data = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        data.add((List<?>) object);
                    }

                    iSearch.onSearch(data.get(1));
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<List<?>> data = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        data.add((List<?>) object);
                    }

                    if (data.get(0) == null || data.get(1) == null) {
                        return false;
                    }

                    List<MessageUser> searches = new ArrayList<>();
                    for (Object doctorObj : data.get(0)) {
                        Doctor doctor = (Doctor) doctorObj;

                        if (doctor != null) {
                            for (Object messageUsersObj : data.get(1)) {
                                MessageUser messageUser = (MessageUser) messageUsersObj;

                                if (messageUser != null) {
                                    if (Objects.equals(doctor.getId(), messageUser.getDoctor())) {
                                        if (doctor.getName().toLowerCase().contains(newText.toLowerCase())) {
                                            searches.add(messageUser);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_HOME_SERVICE_LIST: {
                if (newText == null || newText.isEmpty()) {
                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<HomeService> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof HomeService) {
                            HomeService homeService = (HomeService) object;
                            if (homeService.getDoctor_name().toLowerCase().contains(newText.toLowerCase()) ||
                                    homeService.getDoctor_time().toLowerCase().contains(newText.toLowerCase()) ||
                                    homeService.getDoctor_specialist().toLowerCase().contains(newText.toLowerCase())
                                    ) {

                                searches.add(homeService);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_NURSE: {
                if (newText == null || newText.isEmpty()) {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<Nurse> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof Nurse) {
                            Nurse nurse = (Nurse) object;
                            if (nurse.getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    nurse.getGender().contains(newText) ||
                                    nurse.getType().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(nurse);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_BLOG: {
                if (newText == null || newText.isEmpty()) {
                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }
                    
                    List<Blog> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof Blog) {
                            Blog blog = (Blog) object;
                            if (blog.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                                    blog.getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    blog.getDate().toLowerCase().contains(newText.toLowerCase()) ||
                                    blog.getDetail().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(blog);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_DOCTOR_LIST_MESSAGE: {
                if (newText == null || newText.isEmpty()) {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<Doctor> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof Doctor) {
                            Doctor doctor = (Doctor) object;
                            if (doctor.getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    doctor.getSpecialist().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(doctor);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_DOCTOR_LIST_HOME_SERVICE: {
                if (newText == null || newText.isEmpty()) {
                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<HomeServiceDoctor> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof HomeServiceDoctor) {
                            HomeServiceDoctor homeServiceDoctor = (HomeServiceDoctor) object;
                            if (homeServiceDoctor.getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    homeServiceDoctor.getSpecialist().toLowerCase().contains(newText.toLowerCase()) ||
                                    homeServiceDoctor.getTime().toLowerCase().contains(newText.toLowerCase())||
                                    homeServiceDoctor.getLocation().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(homeServiceDoctor);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_DOCTOR_LIST_AUDIO_CALL: {
                if (newText == null || newText.isEmpty()) {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<AudioCallDoctor> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof AudioCallDoctor) {
                            AudioCallDoctor audioCallDoctor = (AudioCallDoctor) object;
                            if (audioCallDoctor.getDoctor().getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    audioCallDoctor.getDoctor().getCity().toLowerCase().contains(newText.toLowerCase()) ||
                                    audioCallDoctor.getDoctor().getSpecialist().toLowerCase().contains(newText.toLowerCase()) ||
                                    audioCallDoctor.getDoctor().getHospital().toLowerCase().contains(newText.toLowerCase())
                                    ) {

                                searches.add(audioCallDoctor);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_DOCTOR_LIST_APPOINTMENT: {
                if (newText == null || newText.isEmpty()) {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<AppointmentDoctor> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof AppointmentDoctor) {
                            AppointmentDoctor appointmentDoctor = (AppointmentDoctor) object;
                            if (appointmentDoctor.getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    appointmentDoctor.getSpecialist().toLowerCase().contains(newText.toLowerCase()) ||
                                    appointmentDoctor.getClinic().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(appointmentDoctor);
                            } else {
                                for (Appointment appointment : appointmentDoctor.getAppointmentsList()) {
                                    if (appointment.getDays().toLowerCase().contains(newText.toLowerCase()) ||
                                            appointment.getTime().toLowerCase().contains(newText.toLowerCase())) {

                                        searches.add(appointmentDoctor);
                                        break;
                                    }
                                }
                            }

                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_BLOOD_DONOR_LIST: {
                if (newText == null || newText.isEmpty()) {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<BloodDonor> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof BloodDonor) {
                            BloodDonor bloodDonor = (BloodDonor) object;
                            if (bloodDonor.getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    bloodDonor.getCity().toLowerCase().contains(newText.toLowerCase()) ||
                                    bloodDonor.getGender().toLowerCase().contains(newText.toLowerCase()) ||
                                    bloodDonor.getGroup().toLowerCase().contains(newText.toLowerCase()) ||
                                    bloodDonor.getAge().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(bloodDonor);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_BLOOD_POST_LIST: {
                if (newText == null || newText.isEmpty()) {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<BloodPost> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof BloodPost) {
                            BloodPost bloodPost = (BloodPost) object;
                            if (bloodPost.getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    bloodPost.getCity().toLowerCase().contains(newText.toLowerCase()) ||
                                    bloodPost.getGroup().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(bloodPost);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
            case Vars.Search.PAGE_AUDIO_CALL_HISTORY_LIST: {
                if (newText == null || newText.isEmpty()) {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    iSearch.onSearch(iSearch.getList());
                } else {
                    if (iSearch.getList().size() <= 0) {
                        return false;
                    }

                    List<AudioCallHistory> searches = new ArrayList<>();
                    for (Object object : iSearch.getList()) {
                        if (object instanceof AudioCallHistory) {
                            AudioCallHistory history = (AudioCallHistory) object;
                            Doctor doctor = (Doctor) history.getUser();
                            if (history.getDate().toLowerCase().contains(newText.toLowerCase()) ||
                                    history.getCall_status().toLowerCase().contains(newText.toLowerCase()) ||
                                    doctor.getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    doctor.getSpecialist().toLowerCase().contains(newText.toLowerCase()) ||
                                    history.getDate().toLowerCase().contains(newText.toLowerCase())) {

                                searches.add(history);
                            }
                        }
                    }

                    iSearch.onSearch(searches);
                }
                break;
            }
        }

        return true;
    }

    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {
        switch (page) {
            case Vars.Search.PAGE_MESSAGE_USER_LIST: {
                if (iSearch.getList().size() <= 0) {
                    return;
                }

                List<List<?>> data = new ArrayList<>();
                for (Object object : iSearch.getList()) {
                    data.add((List<?>) object);
                }
                iSearch.onSearch(data.get(1));
                break;
            }
            default:
                iSearch.onSearch(iSearch.getList());
                break;
        }
    }
}
