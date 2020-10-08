package com.example.recyclerviewpagination.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recyclerviewpagination.R;
import com.example.recyclerviewpagination.models.ProjectsNews;
import com.example.recyclerviewpagination.utils.CircleTransform;

import java.util.List;

import static com.example.recyclerviewpagination.utils.Constants.IMAGE_CONF;
import static com.example.recyclerviewpagination.utils.Constants.IMAGE_PATH;

public class ProjectsNewsAdapter extends RecyclerView.Adapter<ProjectsNewsViewHolder> {

    List<ProjectsNews> data;
    Context context;
    OnProjectsNewsListener onProjectsNewsListener;
    ProjectsNewsCallback callback;

    public ProjectsNewsAdapter(Context context, OnProjectsNewsListener onProjectsNewsListener, ProjectsNewsCallback callback){
        this.context = context;
        this.onProjectsNewsListener = onProjectsNewsListener;
        this.callback = callback;
    }

//    public ProjectsNewsAdapter(List<ProjectsNews> data, Context context, OnProjectsNewsListener onProjectsNewsListener ) {
//        this.data = data;
//        this.context = context;
//        this.onProjectsNewsListener = onProjectsNewsListener;
//    }

    public void setData(List<ProjectsNews> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ProjectsNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_projects_news_list_item, parent, false);
        return new ProjectsNewsViewHolder(view, onProjectsNewsListener, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsNewsViewHolder holder, int position) {

        ProjectsNewsViewHolder h = holder;
        h.title.setText(data.get(position).getTitle());
        h.anounce.setText(data.get(position).getAnounce());
        String[] dateMas = data.get(position).getPublished_at().split("T");
        h.publishedDate.setText(dateMas[0]);

        h.img.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        h.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        Uri imageUri = Uri.parse(IMAGE_PATH + data.get(position).getId() + IMAGE_CONF);

        Glide.with(holder.itemView.getContext())
                .load(imageUri)
                .placeholder(R.drawable.ic_launcher_foreground)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new CircleTransform(3.0f/5))
                .into(h.img);
    }

    @Override
    public int getItemCount() {
        if(data!=null) {
            return data.size();
        }
        return 0;
    }

}
