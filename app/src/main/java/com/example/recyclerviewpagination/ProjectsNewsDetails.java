package com.example.recyclerviewpagination;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recyclerviewpagination.models.ProjectsNews;
import com.example.recyclerviewpagination.utils.CircleTransform;

import static com.example.recyclerviewpagination.utils.Constants.IMAGE_CONF;
import static com.example.recyclerviewpagination.utils.Constants.IMAGE_PATH;

public class ProjectsNewsDetails extends AppCompatActivity {

    private static final String TAG = "ProjectsNewsDetailActivity";
    private ImageView img;
    private TextView tvTitleDetail, tvContentDetail, tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_news_details);

        img = findViewById(R.id.img_projects_news_detail);
        tvTitleDetail = findViewById(R.id.tv_title_detail);
        tvContentDetail = findViewById(R.id.tv_content_detail);
        tvDate = findViewById(R.id.tv_date_detail);

        tvContentDetail.setMovementMethod(new ScrollingMovementMethod());

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("projectsNews")){
            ProjectsNews news = getIntent().getParcelableExtra("projectsNews");

            tvTitleDetail.setText(news.getTitle());
            tvContentDetail.setText(news.getContent());
            String[] dateMas =  news.getPublished_at().replace(".", "T").split("T");
            tvDate.setText(dateMas[0] + "\n" + dateMas[1]);

            Uri imageUri = Uri.parse(IMAGE_PATH + news.getId() + IMAGE_CONF);
            Glide.with(getApplicationContext())
                    .load(imageUri)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .transform(new CircleTransform(3))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);

            Log.d(TAG, "getIncomingIntent: " + news.getTitle());
        }
    }
}
