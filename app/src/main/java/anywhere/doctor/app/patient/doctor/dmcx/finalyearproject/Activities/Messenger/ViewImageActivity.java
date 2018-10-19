package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ImageView showImageIV = findViewById(R.id.showImageIV);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(showImageIV);
        photoViewAttacher.update();

        String url = getIntent().getStringExtra(Vars.Connector.VIEW_IMAGE_DATA);
        Picasso.with(RefActivity.refACActivity.get()).load(url).placeholder(R.drawable.nophoto).into(showImageIV);
    }
}
