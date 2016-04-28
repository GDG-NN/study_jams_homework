package com.gdgnn.filatov.kriya.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static final String BASE_URL = "http://m.dynasticlineage.info/";

    private Retrofit service;

    private Api() {
        this.service = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(this.createClient())
                .addConverterFactory(this.createConverter())
                .build();
    }

    public <T> T create(final Class<T> service) {
        return this.service.create(service);
    }

    private OkHttpClient createClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(15000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    private GsonConverterFactory createConverter() {
        return GsonConverterFactory.create();
    }

    private static Api instance = null;

    public static Api getInstance() {
        if (instance == null)
            instance = new Api();

        return instance;
    }
}
