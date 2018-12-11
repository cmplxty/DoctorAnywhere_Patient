package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.Blog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Activities.HomeActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter.BlogRecyclerViewAdapter;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.BlogController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Controller.SearchController;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.IAction;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface.ISearch;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Blog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.HomeService;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Variables.Vars;

public class BlogActivity extends AppCompatActivity implements ISearch {

    public static WeakReference<BlogActivity> instance;

    private Toolbar toolbar;
    private MaterialSearchView materialSearchView;
    private TextView noBlogFoundBTV;
    private RecyclerView blogBRV;
    private BlogRecyclerViewAdapter blogRecyclerViewAdapter;

    private List<Blog> blogs;

    private void init() {
        blogBRV = findViewById(R.id.blogBRV);
        toolbar = findViewById(R.id.toolbar);
        noBlogFoundBTV = findViewById(R.id.noBlogFoundBTV);
        materialSearchView = findViewById(R.id.materialSearchView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(RefActivity.refACActivity.get());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        blogBRV.setLayoutManager(layoutManager);
        blogBRV.setHasFixedSize(true);

        blogRecyclerViewAdapter = new BlogRecyclerViewAdapter();
        blogBRV.setAdapter(blogRecyclerViewAdapter);

        blogs = new ArrayList<>();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupSearchView() {
        materialSearchView.setOnSearchViewListener(SearchController.getInstance());
        materialSearchView.setOnQueryTextListener(SearchController.getInstance());

        SearchController.setPage(Vars.Search.PAGE_BLOG);
        SearchController.setiSearch(this);
    }

    private void loadBlogContent() {
        BlogController.LoadAllBlogs(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                blogs = new ArrayList<>();

                if (object != null) {
                    for (Object obj : (List<?>) object) {
                        blogs.add((Blog) obj);
                    }
                }

                updateAdapter(blogs);
                updateLayout(blogs);
            }
        });
    }

    private void updateAdapter(List<Blog> blogs) {
        blogRecyclerViewAdapter.setBlogs(blogs);
        blogRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateLayout(List<Blog> blogs) {
        if (blogs.size() > 0) {
            noBlogFoundBTV.setVisibility(View.GONE);
            blogBRV.setVisibility(View.VISIBLE);
        } else {
            blogBRV.setVisibility(View.GONE);
            noBlogFoundBTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        RefActivity.updateACActivity(this);
        instance = new WeakReference<>(this);
        init();
        setupToolbar();
        setupSearchView();
        loadBlogContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.searchSMI);
        materialSearchView.setMenuItem(search);
        return true;
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
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();
            return;
        }

        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }

    @Override
    public void onSearch(List<?> objects) {
        if (objects != null) {
            List<Blog> searches = new ArrayList<>();
            for (Object object  : objects) {
                if (object  instanceof Blog)
                    searches.add((Blog) object);
            }

            updateLayout(searches);
            updateAdapter(searches);
        }
    }

    @Override
    public List<Blog> getList() {
        return blogs;
    }

    @Override
    public ISearch getiSearch() {
        return this;
    }
}
