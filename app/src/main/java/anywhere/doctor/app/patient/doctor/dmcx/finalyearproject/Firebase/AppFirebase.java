package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Doctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HSDoctor;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HomeService;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Message;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.MessageUser;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Patient;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Prescription;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ErrorText;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Utility.ValidationText;

public class AppFirebase {

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private StorageReference mStorage;

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
                    callback.onCallback(task.isSuccessful(),
                            task.getException() == null ? null : Objects.requireNonNull(task.getException()).getMessage());
                } catch (Exception ex) {
                    // Exception Callback
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
                        boolean isDoctorExists = false;
                        List<Doctor> doctors = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Doctor doctor = snapshot.getValue(Doctor.class);
                            if (doctor != null) {
                                doctor.setId(snapshot.getKey());
                                doctors.add(doctor);
                                isDoctorExists = true;
                            }
                        }

                        callback.onCallback(isDoctorExists, doctors);
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
                                    userMessageMap.put(pFromUserId + "/" + dToUserId, userMessage);
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
    * Load All Home Service Doctors
    * */
    public void loadHomeServiceDoctors(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service_doctor)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<HSDoctor> hsDoctors = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                HSDoctor hsDoctor = snapshot.getValue(HSDoctor.class);
                                if (hsDoctor != null) {
                                    hsDoctor.setId(snapshot.getKey());
                                    hsDoctors.add(hsDoctor);
                                }
                            }

                            if (hsDoctors.size() > 0)
                                callback.onCallback(true, hsDoctors);
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
}
