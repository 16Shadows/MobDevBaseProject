package org.hse.android.baseproject;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TimeQuery {
    private static final String URL = "https://api.ipgeolocation.io/ipgeo?apiKey=b03018f75ed94023a005637878ec0977";
    private static final String TAG = "TimeQuery";

    public static class Time {
        @SerializedName("current_time")
        private String currentTime;

        public String getCurrentTime() { return currentTime; }
    }

    public static class TimeResponse {
        @SerializedName("time_zone")
        private Time timeZone;

        public Time getTimeZone() { return timeZone; }
    }

    public interface TimeQueryResponse {
        void onResponseReady(@Nullable Date response);
    }

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    public static void requestTime(TimeQueryResponse callback) {
        Request request = new Request.Builder().url(URL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onResponseReady(null);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                ResponseBody body = response.body();
                if (body == null) {
                    callback.onResponseReady(null);
                    return;
                }
                try {
                    String str = body.string();
                    TimeResponse rsp = gson.fromJson(str, TimeResponse.class);
                    callback.onResponseReady(dateFormatter.parse(rsp.getTimeZone().getCurrentTime()));

                } catch (Exception e) {
                    Log.e(TAG, "", e);
                    callback.onResponseReady(null);
                }
            }
        });
    }
}
