package me.narlove.enhancedlearningapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static Retrofit retrofit;
    private static ApiService service;

    // singleton pattern
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService()
    {
        if (retrofit == null) getInstance();

        if (service == null)
        {
            service = retrofit.create(ApiService.class);
        }

        return service;
    }
}
