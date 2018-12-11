package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Fragments.Home;

import android.content.Intent;
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
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Map.MapActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.PosterImageCallback;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.BlogController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.DashboardController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
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
    private ConstraintLayout mapPageCL;
    private ConstraintLayout blogMainLayoutDBCL;
    private ConstraintLayout audioCallHistoryPageCL;

    private HorizontalScrollView optionsHorizontalScrollView;
    private ConstraintLayout serviceDBCL;
    private ConstraintLayout blogDBCL;

    private ImageView blogPosterDBIV;
    private TextView blogTitleDBTV;
    private TextView blogContentDBTV;
    private TextView noBlogFoundDBTV;

    private TextView newMessageCounterDBTV;
    private TextView homeServiceCounterDBTV;
    private TextView appointmentCounterDBTV;

    private Blog blog;
    private LoadUIElementHandler loadUIElementHandler = new LoadUIElementHandler();
    private LoadUIOptionLayoutRunnable loadUIOptionLayoutRunnable = new LoadUIOptionLayoutRunnable();
    private LoadUIServiceLayoutRunnable loadUIServiceLayoutRunnable = new LoadUIServiceLayoutRunnable();
    private LoadUIBlogLayoutRunnable loadUIBlogLayoutRunnable = new LoadUIBlogLayoutRunnable();
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

            loadUIElementHandler.removeCallbacks(loadUIOptionLayoutRunnable);
            loadUIElementHandler.removeCallbacks(loadUIServiceLayoutRunnable);
            loadUIElementHandler.removeCallbacks(loadUIBlogLayoutRunnable);
        }
    }


    // Class

    // Methods
    private void init(View view) {
        messagesPageCL = view.findViewById(R.id.messagesPageCL);
        prescriptionPageCL = view.findViewById(R.id.prescriptionPageCL);
        blogPageCL = view.findViewById(R.id.blogPageCL);
        bloodPageCL = view.findViewById(R.id.bloodPageCL);
        mapPageCL = view.findViewById(R.id.mapPageCL);
        nursePageCL = view.findViewById(R.id.nursePageCL);
        optionsHorizontalScrollView = view.findViewById(R.id.optionsHorizontalScrollView);
        homeServicePageCL = view.findViewById(R.id.homeServicePageCL);
        serviceDBCL = view.findViewById(R.id.serviceDBCL);
        blogDBCL = view.findViewById(R.id.blogDBCL);
        blogPosterDBIV = view.findViewById(R.id.blogPosterDBIV);
        blogTitleDBTV = view.findViewById(R.id.blogTitleDBTV);
        blogContentDBTV = view.findViewById(R.id.blogContentDBTV);
        noBlogFoundDBTV = view.findViewById(R.id.noBlogFoundDBTV);
        blogMainLayoutDBCL = view.findViewById(R.id.blogMainLayoutDBCL);
        newMessageCounterDBTV = view.findViewById(R.id.newMessageCounterDBTV);
        homeServiceCounterDBTV = view.findViewById(R.id.homeServiceCounterDBTV);
        appointmentCounterDBTV = view.findViewById(R.id.appointmentCounterDBTV);
        audioCallHistoryPageCL = view.findViewById(R.id.audioCallHistoryPageCL);
    }

    private void task() {
        if (!Vars.Flag.IsDashboardLoaded) {
            loadUIElementHandler.postDelayed(loadUIOptionLayoutRunnable, 400);
        } else {
            visibleOptionsLayout();
        }

        loadUIElementHandler.postDelayed(loadUIServiceLayoutRunnable, 550);
        loadUIElementHandler.postDelayed(loadUIBlogLayoutRunnable, 650);
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

        blogMainLayoutDBCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blog != null)
                    ActivityTrigger.BlogViewerActivity(Vars.ParentActivity.HOME_ACTIVITY, blog);
            }
        });

        bloodPageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.BloodDonorActivity();
            }
        });

        audioCallHistoryPageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.AudioCallHistoryActivity();
            }
        });

        mapPageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RefActivity.refACActivity.get(), MapActivity.class);
                RefActivity.refACActivity.get().startActivity(intent);
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

                    blog = (Blog) object;
                    if (!blog.getPoster().equals("")) {
                        Picasso.with(RefActivity.refACActivity.get())
                                .load(blog.getPoster())
                                .placeholder(R.drawable.no_image_available)
                                .into(blogPosterDBIV, PosterImageCallback.getInstance().setImageView(blogPosterDBIV));
                    }

                    blogTitleDBTV.setText(blog.getTitle());
                    blogContentDBTV.setText(blog.getContent());
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

    private void loadCounter() {
        DashboardController.CountNewMessages(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                counterTextUpdater(object, newMessageCounterDBTV);
            }
        });

        DashboardController.CountHomeServices(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                counterTextUpdater(object, homeServiceCounterDBTV);
            }
        });

        DashboardController.CountAcceptedAppointments(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                counterTextUpdater(object, appointmentCounterDBTV);
            }
        });
    }

    private void counterTextUpdater(Object object, TextView textView) {
        if (object instanceof Integer) {
            if ((Integer) object < 9) {
                textView.setText(String.valueOf(object));
            } else {
                textView.setText("*");
            }
        } else {
            textView.setText("0");
        }
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
        loadCounter();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
