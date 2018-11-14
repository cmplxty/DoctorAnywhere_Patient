package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.BlogRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.BlogController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Blog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;

public class BlogActivity extends AppCompatActivity {

    private TextView noBlogFoundBTV;
    private RecyclerView blogBRV;
    private BlogRecyclerViewAdapter blogRecyclerViewAdapter;

    private void init() {
        noBlogFoundBTV = findViewById(R.id.noBlogFoundBTV);
        blogBRV = findViewById(R.id.blogBRV);

        blogRecyclerViewAdapter = new BlogRecyclerViewAdapter();

        blogBRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        blogBRV.setHasFixedSize(true);
        blogBRV.setAdapter(blogRecyclerViewAdapter);
    }

    private void loadBlogContent() {
        BlogController.LoadAllBlogs(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                List<Blog> blogs = new ArrayList<>();

                if (object != null) {
                    for (Object obj : (List<?>) object) {
                        blogs.add((Blog) obj);
                    }
                }

                if (blogs.size() > 0) {
                    switchLayout(View.VISIBLE, View.GONE);

                    blogRecyclerViewAdapter.setBlogs(blogs);
                    blogRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    switchLayout(View.GONE, View.VISIBLE);
                }
            }
        });
    }

    private void switchLayout(int i1, int i2) {
        blogBRV.setVisibility(i1);
        noBlogFoundBTV.setVisibility(i2);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        RefActivity.updateACActivity(this);
        init();
        loadBlogContent();
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }
}
