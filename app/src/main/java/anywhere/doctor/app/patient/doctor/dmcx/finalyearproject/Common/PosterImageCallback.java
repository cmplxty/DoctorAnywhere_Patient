package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common;

import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class PosterImageCallback implements Callback {

    private ImageView imageView;

    public static PosterImageCallback getInstance() {
        return new PosterImageCallback();
    }

    public PosterImageCallback setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    @Override
    public void onSuccess() {
        Log.d(Vars.TAG, "onSuccess, Poster Image: Center Crop");
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void onError() {
        Log.d(Vars.TAG, "onError, Poster Image: Fit Center");
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}
