package edu.calpoly.android.rxexample;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GitHub {

    private static final String BASE_URL = "https://api.github.com/";

    private static GitHubService sService;

    public static GitHubService get() {
        if (sService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            sService = retrofit.create(GitHubService.class);
        }

        return sService;
    }
}
