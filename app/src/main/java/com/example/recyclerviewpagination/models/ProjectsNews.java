package com.example.recyclerviewpagination.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ProjectsNews implements Parcelable{

    private int id;
    private String published_at;
    private String title;
    private String anounce;
    private String content;

    public ProjectsNews(){

    }

    public ProjectsNews(String published_at, String title, String anounce, String content) {
        this.published_at = published_at;
        this.title = title;
        this.anounce = anounce;
        this.content = content;
    }

    protected ProjectsNews(Parcel in) {
        id = in.readInt();
        published_at = in.readString();
        title = in.readString();
        anounce = in.readString();
        content = in.readString();
    }

    public static final Creator<ProjectsNews> CREATOR = new Creator<ProjectsNews>() {
        @Override
        public ProjectsNews createFromParcel(Parcel in) {
            return new ProjectsNews(in);
        }

        @Override
        public ProjectsNews[] newArray(int size) {
            return new ProjectsNews[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnounce() {
        return anounce;
    }

    public void setAnounce(String anounce) {
        this.anounce = anounce;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SchoolProjectNewsPost{" +
                "id=" + id +
                ", published_at='" + published_at + '\'' +
                ", title='" + title + '\'' +
                ", anounce='" + anounce + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(published_at);
        parcel.writeString(title);
        parcel.writeString(anounce);
        parcel.writeString(content);
    }
}
