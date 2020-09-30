package com.example.recyclerviewpagination.network;

import com.example.recyclerviewpagination.models.ProjectsNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProjectsNewsApi {

    @GET("api/projects_posts/v0/posts?order_by%5Bpublished_at%5D=desc")
    Call<ProjectsNewsListResponse> getProjectsNewsList(
            @Query("page") Integer page,
            @Query("page_limit") Integer page_limit);


    @GET("api/projects_posts/v0/posts/{id}")
    Call<ProjectsNews> getProjectsNews(@Path("id") Integer id);
}
