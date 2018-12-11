package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.LocalDatabase.LDBModel;
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
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Message;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.MessageUser;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Nurse;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Patient;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Prescription;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class AppFirebase {

    private static AppFirebase instance;

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private StorageReference mStorage;

    public static AppFirebase Instance() {
        if (instance == null)
            instance = new AppFirebase();

        return instance;
    }

    public AppFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mReference = FirebaseDatabase.getInstance().getReference();
        this.mStorage = FirebaseStorage.getInstance().getReference();
    }

    // Developer Private
    private void callOnErrorOccured(String error) {
        Toast.makeText(RefActivity.refACActivity.get(), error, Toast.LENGTH_SHORT).show();
    }
    // Developer Private

    /*
    * Get Current User
    * */
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    /*
     * Set Token Id
     * */
    public void setTokenId() {
        if (getCurrentUser() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(AFModel.token_id, FirebaseInstanceId.getInstance().getToken());

            mReference.child(AFModel.database)
                    .child(AFModel.notification)
                    .child(AFModel.token)
                    .child(getCurrentUser().getUid())
                    .updateChildren(map);
        }
    }

    /*
     * Forget Password
     * */
    public void forgetPassword(String email, final ICallback callback) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onCallback(task.isSuccessful(), null);
            }
        });
    }

    /*
    * Get Push Id
    * */
    public String getPushId() {
        return mReference.push().getKey();
    }

    /*
    * Sign In
    * */
    public void signIn(String email, String password, final ICallback callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    callback.onCallback(task.isSuccessful(), task.getException() == null ? null : Objects.requireNonNull(task.getException()).getMessage());
                } catch (Exception ex) {
                    callOnErrorOccured(ErrorText.ErrorServiceBlocked);
                }
            }
        });
    }

    /*
    * Sign Up
    * */
    public void signUp(final String name, final String email, final String password, final ICallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(AFModel.name, name);
                    map.put(AFModel.email, email);
                    map.put(AFModel.link, AFModel.deflt);
                    map.put(AFModel.phone, AFModel.deflt);
                    map.put(AFModel.address, AFModel.deflt);
                    map.put(AFModel.country, AFModel.deflt);
                    map.put(AFModel.gender, AFModel.deflt);
                    map.put(AFModel.dob, AFModel.deflt);
                    map.put(AFModel.age, AFModel.deflt);
                    map.put(AFModel.type, AFModel._new);

                    mReference.child(AFModel.database)
                            .child(AFModel.patient)
                            .child(getCurrentUser().getUid())
                            .setValue(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    callback.onCallback(task.isSuccessful(),  null);
                                }
                            });
                } else {
                    callback.onCallback(task.isSuccessful(),  task.getException() != null ? task.getException().getMessage() : null);
                }
            }
        });
    }

    /*
    * Sign Out
    * */
    public void SignOut() {
        mAuth.signOut();
    }

    /*
    * Get User Info
    * */
    public void getCurrentUserInfo(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.patient)
                .child(getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        callback.onCallback(dataSnapshot.exists(), dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onCallback(false, databaseError.getMessage());
                    }
                });
    }

    /*
    * Get User Info by Email of (Patient)
    * */
    public void getUserByEmail(final String email, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.patient)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isEmailExists = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Patient patient = snapshot.getValue(Patient.class);
                            if (patient != null) {
                                isEmailExists = patient.getEmail().equals(email);
                                if (isEmailExists) {
                                    break;
                                }
                            }
                        }

                        callback.onCallback(isEmailExists, dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Load all doctors
    * */
    public void getDoctors(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.doctor)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Doctor> doctors = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Doctor doctor = snapshot.getValue(Doctor.class);
                            if (doctor != null) {
                                doctor.setId(snapshot.getKey());
                                doctors.add(doctor);
                            }
                        }

                        if (doctors.size() > 0)
                            callback.onCallback(true, doctors);
                        else
                            callback.onCallback(false, null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Load doctor
    * */
    public void getDoctor(String id, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.doctor)
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Doctor doctor = dataSnapshot.getValue(Doctor.class);
                        if (doctor != null) {
                            doctor.setId(dataSnapshot.getKey());
                            callback.onCallback(true, doctor);
                        } else
                            callback.onCallback(false, null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Save Message
    * */
    public void saveMessage(Map<String, Object> value, final Map<String, Object> userMessage, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.message)
                .updateChildren(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mReference.child(AFModel.database)
                                .child(AFModel.message_user)
                                .updateChildren(userMessage);

                        callback.onCallback(task.isSuccessful(), task.isSuccessful() ? ValidationText.MessageSent : ValidationText.MessageNotSent);
                    }
                });
    }

    /*
     * Save Image Message
     * TODO:
     * */
    public void saveImageMessage(Uri uri, final Map<String, Object> map, final Map<String, Object> userMessage, final String pFromUserId, final String dToUserId, final ICallback callback) {
        final StorageReference sRef = mStorage.child(AFModel.message + "/" + UUID.randomUUID().toString());

        sRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        sRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    String downloadUrl = task.getResult().toString();
                                    map.put(AFModel.content, downloadUrl);
                                    userMessage.put(AFModel.content, downloadUrl);

                                    Map<String, Object> mainMap = new HashMap<>();
                                    mainMap.put(pFromUserId + "/" + getPushId(), map);
                                    mainMap.put(dToUserId + "/" + getPushId(), map);

                                    Map<String, Object> userMessageMap = new HashMap<>();
                                    userMessage.put(AFModel.notification_status, AFModel.viewed);
                                    userMessageMap.put(pFromUserId + "/" + dToUserId, userMessage);
                                    userMessage.put(AFModel.notification_status, AFModel.not_viewed);
                                    userMessageMap.put(dToUserId + "/" + pFromUserId, userMessage);

                                    saveMessage(mainMap, userMessageMap, callback);
                                } else {
                                    callback.onCallback(false, ErrorText.ErrorFileNotSent);
                                }
                            }
                        });
                    }
                });

    }

    /*
    * Show Message
    * */
    public void showMessages(final String toUserId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.message)
                .child(getCurrentUser().getUid())
                .orderByChild(AFModel.timestamp)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Message> messages = new ArrayList<>();
                        boolean isMessagesFound = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);
                            if (message != null) {
                                if (message.getFrom().equals(getCurrentUser().getUid()) && message.getTo().equals(toUserId)
                                    || message.getTo().equals(getCurrentUser().getUid()) && message.getFrom().equals(toUserId)) {
                                    messages.add(message);
                                    isMessagesFound = true;
                                }
                            }
                        }

                        callback.onCallback(isMessagesFound, messages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
     * Load Prescription
     * */
    public void loadSpecificPrescription(final String timestamp, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.prescription)
                .child(getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isMatchFound = false;
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Prescription prescription = snapshot.getValue(Prescription.class);
                                if (prescription != null) {
                                    if (prescription.getTimestamp().equals(timestamp)) {
                                        isMatchFound = true;
                                        callback.onCallback(true, prescription);
                                    }
                                }
                            }

                            if (!isMatchFound)
                                callback.onCallback(false, ErrorText.ErrorNoPrescriptionFound);
                        } else {
                            callback.onCallback(false, ErrorText.ErrorNoDatabaseFound);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Load All Prescriptions
    * */
    public void loadPrescriptions(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.prescription)
                .child(getCurrentUser().getUid())
                .orderByChild(AFModel.timestamp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ArrayList<Prescription> prescriptions = new ArrayList<>();
                            boolean isPrescriptionsFound = false;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Prescription prescription = snapshot.getValue(Prescription.class);
                                if (prescription != null) {
                                    if (!isPrescriptionsFound) isPrescriptionsFound = true;
                                    prescriptions.add(prescription);
                                }
                            }

                            Collections.sort(prescriptions, new Comparator<Prescription>() {
                                @Override
                                public int compare(Prescription p1, Prescription p2) {
                                    return Long.valueOf(p2.getTimestamp()).compareTo(Long.valueOf(p1.getTimestamp()));
                                }
                            });
                            callback.onCallback(isPrescriptionsFound, prescriptions);
                        } else {
                            callback.onCallback(false, ErrorText.ErrorNoPrescriptionFound);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
     * Users who messeages doctor
     * */
    public void loadMessageUsers(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.message_user)
                .child(getCurrentUser().getUid())
                .orderByChild(AFModel.timestamp)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<MessageUser> messageUsers = new ArrayList<>();
                        boolean isDataFound = false;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                MessageUser messageUser = snapshot.getValue(MessageUser.class);
                                if (messageUser != null) {
                                    if (!isDataFound) isDataFound = true;
                                    messageUsers.add(messageUser);
                                }
                            }
                        }

                        callback.onCallback(isDataFound, messageUsers);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
     * User detail who messaeg doctor
     * */
    public void loadMessageUserDetail(final String userId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.doctor)
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Doctor doctor = null;
                        boolean isDataFound = true;
                        if (dataSnapshot.exists()){
                            doctor = dataSnapshot.getValue(Doctor.class);

                            if (doctor != null) doctor.setId(userId);
                        } else {
                            isDataFound = false;
                        }

                        callback.onCallback(isDataFound, doctor);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
     * Update Not Viewed To Viewed
     * */
    public void updateMessageNotViewedToViewed() {
        mReference.child(AFModel.database)
                .child(AFModel.message_user)
                .child(getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.hasChild(AFModel.notification_status)) {
                                snapshot.child(AFModel.notification_status).getRef().setValue(AFModel.viewed);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Load All Home Service Doctors
    * */
    public void loadHomeServiceDoctors(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service_doctor)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<HomeServiceDoctor> homeServiceDoctors = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                HomeServiceDoctor homeServiceDoctor = snapshot.getValue(HomeServiceDoctor.class);
                                if (homeServiceDoctor != null) {
                                    homeServiceDoctor.setId(snapshot.getKey());
                                    homeServiceDoctors.add(homeServiceDoctor);
                                }
                            }

                            if (homeServiceDoctors.size() > 0)
                                callback.onCallback(true, homeServiceDoctors);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Send Request For Home Service
    * */
    public void sendHomeServiceRequest(Map<String, Object> value, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service)
                .updateChildren(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Check Patient Requested for Home Service
    * */
    public void checkHomeServiceRequest(final String doctorId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild(doctorId)) {
                                callback.onCallback(true, null);
                            } else {
                                callback.onCallback(false, null);
                            }
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Cancel Home Service Request
    * */
    public void cancelHomeServiceRequest(final String doctorId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service)
                .child(getCurrentUser().getUid())
                .child(doctorId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mReference.child(AFModel.database)
                                    .child(AFModel.home_service)
                                    .child(doctorId)
                                    .child(getCurrentUser().getUid()).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            callback.onCallback(task.isSuccessful(), null);
                                        }
                                    });
                        } else {
                            callback.onCallback(false, null);
                        }
                    }
                });
    }

    /*
    * Load Home Services
    * */
    public void loadHomeService(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<HomeService> homeServices = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                HomeService homeService = snapshot.getValue(HomeService.class);
                                if (homeService != null) {
                                    homeService.setDoctor_id(snapshot.getKey());
                                    homeServices.add(homeService);
                                }
                            }

                            if (homeServices.size() > 0) {
                                callback.onCallback(true, homeServices);
                            } else {
                                callback.onCallback(false, null);
                            }
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Load Appointment Doctors
    * */
    public void loadAppointmentDoctors(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment_doctor)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<AppointmentDoctor> appointmentDoctors = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                AppointmentDoctor appointmentDoctor = snapshot.getValue(AppointmentDoctor.class);
                                if (appointmentDoctor != null)  {
                                    Gson gson = new Gson();
                                    List<Appointment> appointments = gson.fromJson(
                                            appointmentDoctor.getAppointments(), new TypeToken<List<Appointment>>(){}.getType()
                                    );

                                    appointmentDoctor.setId(snapshot.getKey());
                                    appointmentDoctor.setAppointmentsList(appointments);
                                    appointmentDoctors.add(appointmentDoctor);
                                }
                            }

                            if (appointmentDoctors.size() > 0)
                                callback.onCallback(true, appointmentDoctors);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Save Patient Appointment Request
    * */
    public void saveAppointmentRequest(Map<String, Object> map, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });

    }

    /*
    * Check Appointment Sent
    * */
    public void checkAppointmentSent(final String doctorId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild(doctorId)) {
                                callback.onCallback(true, null);
                            } else {
                                callback.onCallback(false, null);
                            }
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void loadPatientAppointments(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<AppointmentRequest> appointmentRequests = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                AppointmentRequest appointmentRequest = snapshot.getValue(AppointmentRequest.class);
                                if (appointmentRequest != null) {
                                    appointmentRequest.setDoctor_id(snapshot.getKey());
                                    appointmentRequests.add(appointmentRequest);
                                }
                            }

                            if (appointmentRequests.size() > 0) {
                                callback.onCallback(true, appointmentRequests);
                            } else {
                                callback.onCallback(false, null);
                            }
                        } else
                            callback.onCallback(false, null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void deletePatientAppointment(final String doctorId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment)
                .child(getCurrentUser().getUid())
                .child(doctorId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mReference.child(AFModel.database)
                                .child(AFModel.appointment)
                                .child(doctorId)
                                .child(getCurrentUser().getUid())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        callback.onCallback(task.isSuccessful(), null);
                                    }
                                });
                    }
                });
    }

    /*
    * Upload Profile Pic
    * */
    public void uploadProfileImage(final Uri image, final ICallback callback) {
        final StorageReference uploadImage = mStorage.child(AFModel.profile_image).child(getCurrentUser().getUid());

        uploadImage.putFile(image)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            uploadImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    callback.onCallback(true, url);
                                }
                            });
                        } else {
                            callback.onCallback(false, null);
                        }
                    }
                });
    }

    /*
    * Update Profile Detail
    * */
    public void updatePatientProfile(Map<String, Object> map, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.patient)
                .child(getCurrentUser().getUid())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Load All Nurses
    * */
    public void loadNurses(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.nurse)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Nurse> nurses = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Nurse nurse = snapshot.getValue(Nurse.class);

                                if (nurse != null) {
                                    if (nurse.getStatus().equals(AFModel.free)) {
                                        nurse.setId(snapshot.getKey());
                                        nurses.add(nurse);
                                    }
                                }
                            }

                            if (nurses.size() > 0)
                                callback.onCallback(true, nurses);
                            else
                                callback.onCallback(false, null);
                        } else
                            callback.onCallback(false, null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Load All Audio Call Doctors
    * */
    public void loadAllAudioCallDoctors(final List<Doctor> doctors, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.audio_call_doctor)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final List<AudioCallDoctor> audioCallDoctors = new ArrayList<>();

                            for (Doctor doctor : doctors) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    final String status = snapshot.child(AFModel.audio_call_status).getValue(String.class);
                                    if (status != null) {
                                        if (status.equals(AFModel.online)) {
                                            if (snapshot.getKey() != null) {
                                                if (doctor.getId().equals(snapshot.getKey())) {
                                                    AudioCallDoctor audioCallDoctor = new AudioCallDoctor();
                                                    audioCallDoctor.setDoctor(doctor);
                                                    audioCallDoctor.setAudio_call_status(status);
                                                    audioCallDoctors.add(audioCallDoctor);
                                                }
                                            }
                                        }

                                    }
                                }
                            }

                            if (audioCallDoctors.size() > 0) {
                                callback.onCallback(true, audioCallDoctors);
                            }
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
     * Load Last Blog Content
     * */
    public void loadLastBlog(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blog)
                .orderByChild(AFModel.timestamp)
                .limitToLast(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        AppFirebaseStatic.updateLastBlog(dataSnapshot, callback);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        AppFirebaseStatic.updateLastBlog(dataSnapshot, callback);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        AppFirebaseStatic.updateLastBlog(dataSnapshot, callback);
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    /*
     * Load All Blog Content
     * */
    public void loadAllBlogs(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blog)
                .orderByChild(AFModel.timestamp)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Blog> blogs = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Blog blog = snapshot.getValue(Blog.class);
                                if (blog != null) {
                                    blog.setId(snapshot.getKey());
                                    blogs.add(blog);
                                }
                            }

                            if (blogs.size() > 0)
                                callback.onCallback(true, blogs);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Set Current Device Id
    * */
    @SuppressLint("HardwareIds")
    public void setAudioCallDeviceId(final ICallback callback) {
        String deviceId = Vars.localDB.retriveString(LDBModel.SAVE_DEVICE_ID);
        if (deviceId.equals("")) {
            deviceId = Settings.Secure.getString(RefActivity.refACActivity.get().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        Map<String, Object> map = new HashMap<>();
        map.put(AFModel.device_id, deviceId);
        map.put(AFModel.timestamp, String.valueOf(System.currentTimeMillis()));

        mReference.child(AFModel.database)
                .child(AFModel.audio_call_device)
                .child(getCurrentUser().getUid())
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Count New Message
    * */
    public void countNewMessage(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.message_user)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int count = 0;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                MessageUser messageUser = snapshot.getValue(MessageUser.class);
                                if (messageUser != null) {
                                    if (messageUser.getNotification_status() != null && messageUser.getNotification_status().equals(AFModel.not_viewed)) {
                                        count++;
                                    }
                                }
                            }

                            if (count > 0)
                                callback.onCallback(true, count);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
     * Count Home Services
     * */
    public void countHomeServices(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int count = 0;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                HomeService homeService = snapshot.getValue(HomeService.class);
                                if (homeService != null) {
                                    count++;
                                }
                            }

                            if (count > 0)
                                callback.onCallback(true, count);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
     * Count Appointments
     * */
    public void countAcceptedAppointments(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int count = 0;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                AppointmentRequest appointmentRequest = snapshot.getValue(AppointmentRequest.class);
                                if (appointmentRequest != null) {
                                    if (appointmentRequest.getStatus() != null && appointmentRequest.getStatus().equals(AFModel.accept)) {
                                        count++;
                                    }
                                }
                            }

                            if (count > 0)
                                callback.onCallback(true, count);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Send Request To Nurse
    * */
    public void sendRequestNurse(String nurseId, Patient patient, final ICallback callback) {
        Map<String, Object> map = new ArrayMap<>();
        map.put(AFModel.link, patient.getLink());
        map.put(AFModel.name, patient.getName());
        map.put(AFModel.phone, patient.getPhone());
        map.put(AFModel.email, patient.getEmail());
        map.put(AFModel.address, patient.getAddress());
        map.put(AFModel.notification_status, AFModel.not_viewed);

        mReference.child(AFModel.database)
                .child(AFModel.nurse_request)
                .child(nurseId)
                .child(getCurrentUser().getUid())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });

    }

    /*
     * Remove Request Nurse
     * */
    public void removeRequestNurse(String nurseId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.nurse_request)
                .child(nurseId)
                .child(getCurrentUser().getUid())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });

    }

    /*
     * Check Request Nurse
     * */
    public void checkRequestNurse(final String nurseId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.nurse_request)
                .child(nurseId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild(getCurrentUser().getUid())) {
                                callback.onCallback(true, null);
                            } else {
                                callback.onCallback(false, null);
                            }
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    /*
    * Load All Blood Donors
    * */
    public void loadBloodDonors(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blood_donors)
                .orderByChild(AFModel.group)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<BloodDonor> bloodDonors = new ArrayList<>();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot != null) {
                                    BloodDonor donor = snapshot.getValue(BloodDonor.class);
                                    if (donor != null) {
                                        if (donor.getAvailable().equals(AFModel.available) || donor.getAvailable().equals("")) {
                                            donor.setId(snapshot.getKey());
                                            bloodDonors.add(donor);
                                        }
                                    }
                                }
                            }
                        }

                        if (bloodDonors.size() <= 0) {
                            callback.onCallback(false, null);
                        } else {
                            callback.onCallback(true, bloodDonors);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Blood Taker Posts
    * */
    public void loadBloodPosts(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blood_posts)
                .orderByChild(AFModel.timestamp)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<BloodPost> bloodPosts = new ArrayList<>();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot != null) {
                                    BloodPost post = snapshot.getValue(BloodPost.class);
                                    if (post != null) {
                                        post.setId(snapshot.getKey());
                                        bloodPosts.add(post);
                                    }
                                }
                            }
                        }

                        if (bloodPosts.size() <= 0) {
                            callback.onCallback(false, null);
                        } else {
                            callback.onCallback(true, bloodPosts);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Create Blood Donor Account
    * */
    public void saveBloodDonor(BloodDonor bloodDonor, final ICallback callback) {
        Map<String, Object> map = new HashMap<>();
        map.put(AFModel.name, bloodDonor.getName());
        map.put(AFModel.phone, bloodDonor.getPhone());
        map.put(AFModel.age, bloodDonor.getAge());
        map.put(AFModel.city, bloodDonor.getCity());
        map.put(AFModel.gender, bloodDonor.getGender());
        map.put(AFModel.group, bloodDonor.getGroup());
        map.put(AFModel.weight, bloodDonor.getWeight());
        map.put(AFModel.available, bloodDonor.getAvailable());
        map.put(AFModel.last_donation_date, bloodDonor.getLast_donation_date());
        map.put(AFModel.timestamp, bloodDonor.getTimestamp());

        mReference.child(AFModel.database)
                .child(AFModel.blood_donors)
                .child(getCurrentUser().getUid())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });

    }

    /*
    * New Blood Post
    * */
    public void newBloodPost(BloodPost post, final ICallback callback) {
        Map<String, Object> map = new HashMap<>();
        map.put(AFModel.name, post.getName());
        map.put(AFModel.user_id, getCurrentUser().getUid());
        map.put(AFModel.city, post.getCity());
        map.put(AFModel.group, post.getGroup());
        map.put(AFModel.description, post.getDescription());
        map.put(AFModel.phone, post.getPhone());
        map.put(AFModel.timestamp, post.getTimestamp());

        mReference.child(AFModel.database)
                .child(AFModel.blood_posts)
                .push()
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Check Me As Blood Donor
    * */
    public void checkMeAsBloodDonor(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blood_donors)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        callback.onCallback(dataSnapshot.exists(), null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    /*
    * My Blood Donor Data
    * */
    public void myBloodDonorData(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blood_donors)
                .child(getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            BloodDonor donor = dataSnapshot.getValue(BloodDonor.class);
                            callback.onCallback(true, donor);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    /*
    * Load My Blood Posts
    * */
    public void loadMyBloodPosts(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blood_posts)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<BloodPost> bloodPosts = new ArrayList<>();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot != null) {
                                    BloodPost post = snapshot.getValue(BloodPost.class);
                                    if (post != null) {
                                        if (post.getUser_id().equals(getCurrentUser().getUid())) {
                                            post.setId(snapshot.getKey());
                                            bloodPosts.add(post);
                                        }
                                    }
                                }
                            }
                        }

                        if (bloodPosts.size() <= 0) {
                            callback.onCallback(false, null);
                        } else {
                            callback.onCallback(true, bloodPosts);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Check My Blood Posts
    * */
    public void checkMyBloodPosts(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blood_posts)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isPostFound = false;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot != null) {
                                    BloodPost post = snapshot.getValue(BloodPost.class);
                                    if (post != null) {
                                        if (post.getUser_id().equals(getCurrentUser().getUid())) {
                                            isPostFound = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if (!isPostFound) {
                            callback.onCallback(false, null);
                        } else {
                            callback.onCallback(true, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Find User By Blood Group
    * */
    public void findUserByBloodGroup(final String bloodGroup, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blood_donors)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<BloodDonor> bloodDonors = new ArrayList<>();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                BloodDonor donor = snapshot.getValue(BloodDonor.class);
                                if (donor != null) {
                                    if (donor.getGroup().equals(bloodGroup)) {
                                        donor.setId(snapshot.getKey());
                                        bloodDonors.add(donor);
                                    }
                                }
                            }
                        }

                        if (bloodDonors.size() > 0)
                            callback.onCallback(true, bloodDonors);
                        else
                            callback.onCallback(false, null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Delete Current User Donor Account
    * */
    public void deleteCurrentDonorData(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blood_donors)
                .child(getCurrentUser().getUid())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onCallback(task.isSuccessful(), null);
            }
        });
    }

    /*
    * Delete Current Post
    * */
    public void deleteCurrentPost(String postId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blood_posts)
                .child(postId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onCallback(task.isSuccessful(), null);
            }
        });
    }

    /*
     * Load All Doctors
     * */
    public void loadAllDoctors(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.doctor)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Doctor> doctors = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Doctor doctor = snapshot.getValue(Doctor.class);
                                if (doctor != null) {
                                    doctor.setId(snapshot.getKey());
                                    doctors.add(doctor);
                                }
                            }

                            if (doctors.size() > 0)
                                callback.onCallback(true, doctors);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Audio Call History
    * */
    public void audioCallHistories(final List<Doctor> doctors, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.audio_call_history)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<AudioCallHistory> histories = new ArrayList<>();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                AudioCallHistory history = snapshot.getValue(AudioCallHistory.class);
                                if (history != null) {
                                    for (Doctor doctor : doctors) {
                                        if (doctor != null) {
                                            if (history.getCaller_id().equals(doctor.getId())) {
                                                history.setId(snapshot.getKey());
                                                history.setUser(doctor);
                                                histories.add(history);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (histories.size() > 0)
                            callback.onCallback(true, histories);
                        else
                            callback.onCallback(false, null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Delete Current History
    * */
    public void deleteCurrentHistory(String historyId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.audio_call_history)
                .child(getCurrentUser().getUid())
                .child(historyId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Delete All Histories
    * */
    public void deleteHistories(ICallback callback) {
    }
}
