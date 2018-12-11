package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.PosterImageCallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Blog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class BlogViewerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView blogPosterBVIV;
    private TextView blogContentBVTV;
    private TextView blogWriterBVTV;

    private Blog blog;
    private String parentActivity;

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        blogPosterBVIV = findViewById(R.id.blogPosterBVIV);
        blogContentBVTV = findViewById(R.id.blogContentBVTV);
        blogWriterBVTV = findViewById(R.id.blogWriterBVTV);

        blog = getIntent().getParcelableExtra(Vars.Connector.BLOG_VIEWER_ACTIVITY_DATA);
        parentActivity = getIntent().getStringExtra(Vars.ParentActivity.TRIG_BLOG_VIEWER_ACTIVITY);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void load() {
        if (blog != null) {
            if (!blog.getPoster().equals(""))
                Picasso.with(RefActivity.refACActivity.get())
                        .load(blog.getPoster())
                        .placeholder(R.drawable.no_image_available)
                        .into(blogPosterBVIV, PosterImageCallback.getInstance().setImageView(blogPosterBVIV));

            toolbar.setTitle(blog.getTitle());
            blogContentBVTV.setText(blog.getContent());
            blogWriterBVTV.setText(new StringBuilder(blog.getDate()).append(" - Written by ").append(blog.getName()));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_viewer);
        init();
        setupToolbar();
        load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (parentActivity.equals(Vars.ParentActivity.HOME_ACTIVITY))
            RefActivity.updateACActivity(HomeActivity.instance.get());
        else if (parentActivity.equals(Vars.ParentActivity.BLOG_ACTIVITY)) {
            RefActivity.updateACActivity(BlogActivity.instance.get());
        }

        super.onBackPressed();
    }
}
