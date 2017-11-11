package com.projects.melih.gistpub.network.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.projects.melih.gistpub.BuildConfig;
import com.projects.melih.gistpub.R;

import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by melihmg on 19/02/2017
 */

public class GitHubApi {
    private static final int TIMEOUT_SECOND = 60;
    public static final String API_BASE_URL = "https://api.github.com/";
    private static GitHubService gitHubService;
    private static GitHubService gitHubBasicAuthService;
    private static GitHubService gitHubOAuthService;

    private static OkHttpClient.Builder getOkkHttpClient() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS);
        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(httpLoggingInterceptor);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            httpClient.addInterceptor(httpLoggingInterceptor);
        }
        return httpClient;
    }

    public static GitHubService getGitHubService(@NonNull Context context) {
        if (gitHubService == null) {
            synchronized (GitHubApi.class) {
                if (gitHubService == null) {
                    SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_user_key), Context.MODE_PRIVATE);
                    String username = sharedPref.getString(context.getString(R.string.username_extra), null);
                    String password = sharedPref.getString(context.getString(R.string.password_extra), null);
                    String authToken = Credentials.basic(username, password);

                    AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

                    final OkHttpClient.Builder httpClient = getOkkHttpClient();
                    httpClient.addInterceptor(interceptor);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                    gitHubService = retrofit.create(GitHubService.class);
                }
            }
        }
        return gitHubService;
    }

    public static GitHubService getBasicAuthGitHubService(@NonNull String username, @NonNull String password) {
        if (gitHubBasicAuthService == null) {
            synchronized (GitHubApi.class) {
                if (gitHubBasicAuthService == null) {
                    String authToken = Credentials.basic(username, password);
                    AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

                    final OkHttpClient.Builder httpClient = getOkkHttpClient();
                    httpClient.addInterceptor(interceptor);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                    gitHubBasicAuthService = retrofit.create(GitHubService.class);
                }
            }
        }
        return gitHubBasicAuthService;
    }

    public static GitHubService getOAuthGitHubService(@NonNull Context context, @NonNull String username, @NonNull String password) {
        if (gitHubOAuthService == null) {
            synchronized (GitHubApi.class) {
                if (gitHubOAuthService == null) {
                    String authToken = Credentials.basic("", "");
                    AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

                    final OkHttpClient.Builder httpClient = getOkkHttpClient();
                    httpClient.addInterceptor(interceptor);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                    gitHubOAuthService = retrofit.create(GitHubService.class);
                }
            }
        }
        return gitHubOAuthService;
    }
}
