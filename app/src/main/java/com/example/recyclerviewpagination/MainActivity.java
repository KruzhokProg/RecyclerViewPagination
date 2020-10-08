package com.example.recyclerviewpagination;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static com.example.recyclerviewpagination.utils.Constants.PAGE_LIMIT;
import androidx.core.util.Pair;

public class MainActivity extends AppCompatActivity implements OnProjectsNewsListener, ProjectsNewsCallback {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1234;
    EditText etSearch;
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<ProjectsNews> data;
    List<ProjectsNews> filtered_data;
    List<ProjectsNews> searchList;
    ProjectsNewsAdapter adapter;
    int page = 1;
    public static final String date1_pref = "date1";
    public static final String date2_pref = "date2";
    String date1s="", date2s="";
    SharedPreferences mSettings;
    String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = this.getSharedPreferences(date1_pref, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        SharedPreferences settings2 = this.getSharedPreferences(date2_pref, Context.MODE_PRIVATE);
        settings2.edit().clear().commit();

        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.rv_news);
        progressBar = findViewById(R.id.progress_bar);
        etSearch = findViewById(R.id.et_search);
        data = new ArrayList<>();
        filtered_data = new ArrayList<>();
        searchList = new ArrayList<>();
        mSettings = getSharedPreferences(date1_pref, Context.MODE_PRIVATE);

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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchInput = charSequence.toString();
                data.clear();
                filtered_data.clear();
                searchList.clear();
                getData(1);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void getData(final int page){
        if(mSettings.contains(date1_pref)) {
            date1s = mSettings.getString(date1_pref, "");
            date2s = mSettings.getString(date2_pref, "");
        }
        ProjectsNewsApi apiService = ApiClient.getClient().create(ProjectsNewsApi.class);
        Call<ProjectsNewsListResponse> call = apiService.getProjectsNewsList(page, PAGE_LIMIT);
        call.enqueue(new Callback<ProjectsNewsListResponse>() {
            @Override
            public void onResponse(Call<ProjectsNewsListResponse> call, Response<ProjectsNewsListResponse> response) {

                ProjectsNewsListResponse body = response.body();

                if(body.getPages() >= page && response.isSuccessful()) {
                    if(date1s.equals("") && date2s.equals("")) {
                        filtered_data.clear();
                        data.addAll(body.getPosts());

                        if(searchInput != null && !searchInput.equals("")){
                            List<ProjectsNews> output = GetDataFilterBySearchInput(data);
                            if(!output.isEmpty()) {
                                searchList.addAll(output);
                                adapter.setData(searchList);
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Список закончился!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        else {
                            adapter.setData(data);
                        }

                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                    else{
                        List<ProjectsNews> output = GetDataFilterByDate(body.getPosts());
                        if(output.size() != 0) {
                            data.clear();
                            filtered_data.addAll(output);
                            date1s = "";
                            date2s = "";
                            adapter.setData(filtered_data);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Список закончился!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

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

    public void chooseDate_OnClick(View view) {
        SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback(){

            @Override
            public void onCancelled() {
                date1s = "";
                date2s = "";
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(date1_pref, "");
                editor.putString(date2_pref, "");
                editor.apply();
                filtered_data.clear();
                data.clear();
                getData(1);
            }

            @Override
            public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
                String firstMonth = String.valueOf(firstDate.get(Calendar.MONTH) + 1);
                String secondMonth = String.valueOf(secondDate.get(Calendar.MONTH) + 1);
                date1s = firstDate.get(Calendar.YEAR) + "-" + firstMonth + "-" + firstDate.get(Calendar.DAY_OF_MONTH);
                date2s = secondDate.get(Calendar.YEAR) + "-" + secondMonth + "-" + secondDate.get(Calendar.DAY_OF_MONTH);

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(date1_pref, date1s);
                editor.putString(date2_pref, date2s);
                editor.apply();
                filtered_data.clear();
                data.clear();
                getData(1);
            }
        };
        new SlyCalendarDialog()
                .setSingle(false)
                .setCallback(callback)
                .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
    }

    public List<ProjectsNews> GetDataFilterBySearchInput(List<ProjectsNews> list){
        List<ProjectsNews> filtered_search_list = new ArrayList<>();

        for (ProjectsNews item : list) {
            if(item.getTitle().contains(searchInput)){
                filtered_search_list.add(item);
            }
        }

        return filtered_search_list;

    }

    public List<ProjectsNews> GetDataFilterByDate(List<ProjectsNews> list){
        List<ProjectsNews> filtered_list = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateItem = null, date1 = null, date2 = null;

        try {
            date1 = sdf.parse(date1s);
            date2 = sdf.parse(date2s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (ProjectsNews item : list) {
            String date = item.getPublished_at().split("T")[0];
            try {
                dateItem = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(dateItem.compareTo(date1) > 0 && dateItem.compareTo(date2) < 0){
                filtered_list.add(item);
            }
        }
        return filtered_list;
    }

    public void mic_speach_click(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_SPEECH_INPUT:
                if(resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etSearch.setText(result.get(0));
                }
                break;
        }
    }
}
