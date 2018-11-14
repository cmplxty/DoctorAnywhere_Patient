package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Common.RefActivity;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Model.Blog;
import anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerViewAdapter extends RecyclerView.Adapter<BlogRecyclerViewAdapter.BlogRecyclerViewHolder> {

    private List<Blog> blogs = new ArrayList<>();

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @NonNull
    @Override
    public BlogRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_blog, parent, false);
        return new BlogRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerViewHolder holder, int position) {

        if (!blogs.get(position).getImage_link().equals("")) {
            Picasso.with(RefActivity.refACActivity.get())
                    .load(blogs.get(position).getImage_link())
                    .placeholder(R.drawable.noperson)
                    .into(holder.bloggerProfileImageBCIV);
        }

        if (!blogs.get(position).getPoster().equals("")) {
            final BlogRecyclerViewHolder bHolder = holder;
            Picasso.with(RefActivity.refACActivity.get())
                    .load(blogs.get(position).getPoster())
                    .placeholder(R.drawable.no_image_available)
                    .into(holder.blogPosterDBIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            bHolder.blogPosterDBIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

        holder.blogWriterNameBTV.setText(blogs.get(position).getName());
        holder.blogWriterDetailBTV.setText(blogs.get(position).getDetail());
        holder.dateBTV.setText(blogs.get(position).getDate());
        holder.blogTitleBTV.setText(blogs.get(position).getTitle());
        holder.blogContentBTV.setText(blogs.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    class BlogRecyclerViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView bloggerProfileImageBCIV;
        private TextView blogWriterNameBTV;
        private TextView blogWriterDetailBTV;
        private TextView dateBTV;
        private ImageView blogPosterDBIV;
        private TextView blogTitleBTV;
        private TextView blogContentBTV;

        BlogRecyclerViewHolder(View itemView) {
            super(itemView);

            bloggerProfileImageBCIV = itemView.findViewById(R.id.bloggerProfileImageBCIV);
            blogWriterNameBTV = itemView.findViewById(R.id.blogWriterNameBTV);
            blogWriterDetailBTV = itemView.findViewById(R.id.blogWriterDetailBTV);
            dateBTV = itemView.findViewById(R.id.dateBTV);
            blogPosterDBIV = itemView.findViewById(R.id.blogPosterDBIV);
            blogTitleBTV = itemView.findViewById(R.id.blogTitleBTV);
            blogContentBTV = itemView.findViewById(R.id.blogContentBTV);
        }
    }
}
