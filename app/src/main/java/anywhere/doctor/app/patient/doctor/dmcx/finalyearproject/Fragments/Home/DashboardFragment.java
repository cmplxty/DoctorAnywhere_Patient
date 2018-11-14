package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.ActivityTrigger;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.BlogController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.FragmentNames;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Blog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class DashboardFragment extends Fragment {

    // Variables
    private ConstraintLayout prescriptionPageCL;
    private ConstraintLayout messagesPageCL;
    private ConstraintLayout homeServicePageCL;
    private ConstraintLayout blogPageCL;
    private ConstraintLayout bloodPageCL;
    private ConstraintLayout nursePageCL;
    private ConstraintLayout helpPageCL;

    private HorizontalScrollView optionsHorizontalScrollView;
    private ConstraintLayout serviceDBCL;
    private ConstraintLayout blogDBCL;

    private ImageView blogPosterDBIV;
    private TextView blogTitleBTV;
    private TextView blogDetailDBTV;
    private TextView noBlogFoundDBTV;
    private ConstraintLayout blogMainLayoutDBCL;

    private LoadUIElementHandler loadUIElementHandler = new LoadUIElementHandler();
    // Variables

    // Class
    private static class LoadUIElementHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class LoadUIOptionLayoutRunnable implements Runnable {

        @Override
        public void run() {
            visibleOptionsLayout();
        }
    }

    private class LoadUIServiceLayoutRunnable implements Runnable {

        @Override
        public void run() {
            visibleServiceLayout();
        }
    }

    private class LoadUIBlogLayoutRunnable implements Runnable {

        @Override
        public void run() {
            visibleBlogLayout();
        }
    }
    // Class

    // Methods
    private void init(View view) {
        messagesPageCL = view.findViewById(R.id.messagesPageCL);
        prescriptionPageCL = view.findViewById(R.id.prescriptionPageCL);
        blogPageCL = view.findViewById(R.id.blogPageCL);
        bloodPageCL = view.findViewById(R.id.bloodPageCL);
        helpPageCL = view.findViewById(R.id.helpPageCL);
        nursePageCL = view.findViewById(R.id.nursePageCL);
        optionsHorizontalScrollView = view.findViewById(R.id.optionsHorizontalScrollView);
        homeServicePageCL = view.findViewById(R.id.homeServicePageCL);
        serviceDBCL = view.findViewById(R.id.serviceDBCL);
        blogDBCL = view.findViewById(R.id.blogDBCL);
        blogPosterDBIV = view.findViewById(R.id.blogPosterDBIV);
        blogTitleBTV = view.findViewById(R.id.blogTitleBTV);
        blogDetailDBTV = view.findViewById(R.id.blogDetailDBTV);
        noBlogFoundDBTV = view.findViewById(R.id.noBlogFoundDBTV);
        blogMainLayoutDBCL = view.findViewById(R.id.blogMainLayoutDBCL);
    }

    private void task() {
        if (!Vars.Flag.IsDashboardLoaded) {
            loadUIElementHandler.postDelayed(new LoadUIOptionLayoutRunnable(), 300);
        } else {
            visibleOptionsLayout();
        }

        loadUIElementHandler.postDelayed(new LoadUIServiceLayoutRunnable(), 550);
        loadUIElementHandler.postDelayed(new LoadUIBlogLayoutRunnable(), 650);
    }

    private void event() {
        messagesPageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.MessageListActivity();
            }
        });

        prescriptionPageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.PrescriptionListActivity();
            }
        });

        nursePageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.NurseActivity();
            }
        });

        homeServicePageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.HomeServiceListActivity();
            }
        });

        blogPageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.BlogActivity();
            }
        });
    }

    private void visibleOptionsLayout() {
        optionsHorizontalScrollView.setVisibility(View.VISIBLE);
    }

    private void visibleServiceLayout() {
        serviceDBCL.setVisibility(View.VISIBLE);
    }

    private void visibleBlogLayout() {
        blogDBCL.setVisibility(View.VISIBLE);
    }

    private void goneLayout() {
        optionsHorizontalScrollView.setVisibility(View.GONE);
        serviceDBCL.setVisibility(View.GONE);
        blogDBCL.setVisibility(View.GONE);
    }

    private void loadLastBlog() {
        BlogController.LoadLastBlog(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object != null) {
                    switchBlogLayout(View.VISIBLE, View.GONE);

                    Blog blog = new Blog();

                    Picasso.with(RefActivity.refACActivity.get())
                            .load(blog.getImage_link())
                            .placeholder(R.drawable.noperson)
                            .into(blogPosterDBIV);

                    blogTitleBTV.setText(blog.getTitle());
                    blogDetailDBTV.setText(blog.getContent());
                } else {
                    switchBlogLayout(View.GONE, View.VISIBLE);
                }
            }
        });
    }

    private void switchBlogLayout(int i1, int i2) {
        blogMainLayoutDBCL.setVisibility(i1);
        noBlogFoundDBTV.setVisibility(i2);
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_dashboard, container, false);
        init(view);
        goneLayout();
        task();
        event();
        loadLastBlog();
        return view;
    }

}
