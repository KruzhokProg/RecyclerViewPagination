package com.example.recyclerviewpagination.adapter;

import android.widget.ImageView;
import android.widget.TextView;

public interface ProjectsNewsCallback {

    void onProjectsNewsItemClick(int pos,
                                 ImageView imgLogo,
                                 TextView date,
                                 TextView title);
}
