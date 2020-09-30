package com.example.recyclerviewpagination.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://mcrkpo.ru/wp-json/wp/v2/";
    public static final String BASE_URL_SCHOOL_MOSCOW = "https://school.moscow/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_SCHOOL_MOSCOW)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
