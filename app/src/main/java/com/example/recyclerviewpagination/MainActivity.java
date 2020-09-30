package com.example.recyclerviewpagination;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclerviewpagination.adapter.OnProjectsNewsListener;
import com.example.recyclerviewpagination.adapter.ProjectsNewsAdapter;
import com.example.recyclerviewpagination.adapter.ProjectsNewsCallback;
import com.example.recyclerviewpagination.models.ProjectsNews;
import com.example.recyclerviewpagination.network.ApiClient;
import com.example.recyclerviewpagination.network.ProjectsNewsApi;
import com.example.recyclerviewpagination.network.ProjectsNewsListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.recyclerviewpagination.utils.Constants.PAGE_LIMIT;
import androidx.core.util.Pair;

public class MainActivity extends AppCompatActivity implements OnProjectsNewsListener, ProjectsNewsCallback {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<ProjectsNews> data;
    ProjectsNewsAdapter adapter;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.rv_news);
        progressBar = findViewById(R.id.progress_bar);
        data = new ArrayList<>();

        adapter = new ProjectsNewsAdapter(this,this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        getData(page);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    getData(page);
                }
            }
        });
    }

    public void getData(final int page){
        ProjectsNewsApi apiService = ApiClient.getClient().create(ProjectsNewsApi.class);
        Call<ProjectsNewsListResponse> call = apiService.getProjectsNewsList(page, PAGE_LIMIT);
        call.enqueue(new Callback<ProjectsNewsListResponse>() {
            @Override
            public void onResponse(Call<ProjectsNewsListResponse> call, Response<ProjectsNewsListResponse> response) {

                ProjectsNewsListResponse body = response.body();
                if( body.getPages()>=page && response.isSuccessful() && body !=null) {
                    data.addAll(response.body().getPosts());
                    adapter.setData(data);
                    adapter.notifyDataSetChanged();
                    //ViewCompat.setNestedScrollingEnabled(recyclerView, false);
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(MainActivity.this, "Список закончился!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProjectsNewsListResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private List<ProjectsNews> getData(int page){
//
//        ProjectsNewsApi apiService = ApiClient.getClient().create(ProjectsNewsApi.class);
//        Call<ProjectsNewsListResponse> call = apiService.getProjectsNewsList(page, PAGE_LIMIT);
//        call.enqueue(new Callback<ProjectsNewsListResponse>() {
//            @Override
//            public void onResponse(Call<ProjectsNewsListResponse> call, Response<ProjectsNewsListResponse> response) {
//                if(response.isSuccessful() && response.body() != null) {
//                    data = response.body().getPosts();
//                    progressBar.setVisibility(View.GONE);
//                }
//                else{
//                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProjectsNewsListResponse> call, Throwable t) {
//                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    @Override
    public void onProjectsNewsClick(int position) {
        //Toast.makeText(this, data.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProjectsNewsItemClick(int pos, ImageView imgLogo, TextView date, TextView title) {
        Intent intent = new Intent(this, ProjectsNewsDetails.class);
        intent.putExtra("projectsNews", data.get(pos));

        Pair<View, String> p1 = Pair.create((View)imgLogo, "logoTN");
        Pair<View, String> p2 = Pair.create((View)date, "dateTN");
        Pair<View, String> p3 = Pair.create((View)title, "titleTN");

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1,p2,p3);

        startActivity(intent, optionsCompat.toBundle());
    }
}
