package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ICallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Blog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

class AppFirebaseStatic {

    protected static void updateLastBlog(DataSnapshot dataSnapshot, ICallback callback) {
        Log.d(Vars.TAG, "updateLastBlog: " + dataSnapshot.toString());

        if (dataSnapshot.exists()) {
            Blog blog = dataSnapshot.getValue(Blog.class);
            if (blog != null) {
                blog.setId(dataSnapshot.getKey());
                callback.onCallback(true, blog);
            } else {
                callback.onCallback(false, null);
            }
        } else {
            callback.onCallback(false, null);
        }
    }

}
