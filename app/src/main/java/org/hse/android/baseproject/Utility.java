package org.hse.android.baseproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.text.DateFormatSymbols;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

class InstantToast {
    private static Toast lastToast;

    public static void show(Context context, int resId, int duration) {
        if (lastToast != null) {
            lastToast.cancel();
        }
        lastToast = Toast.makeText(context, resId, duration);
        lastToast.show();
    }

    public static void show(Context context, CharSequence message, int duration) {
        if (lastToast != null) {
            lastToast.cancel();
        }
        lastToast = Toast.makeText(context, message, duration);
        lastToast.show();
    }

    public static Boolean isToastVisible() {
        return lastToast != null && lastToast.getView() != null;
    }

    public static Toast getLastToast() {
        return lastToast;
    }
}

class SimpleDateFormatEx {
    public static void capitalizeWeekdays(SimpleDateFormat formatter) {
        DateFormatSymbols symbols = formatter.getDateFormatSymbols();
        String[] weekdays = symbols.getWeekdays();
        for (int i = 0; i < weekdays.length; i++)
            weekdays[i] = weekdays[i].length() > 2 ?
                    Character.toUpperCase(weekdays[i].charAt(0)) + weekdays[i].substring(1) :
                    weekdays[i].toUpperCase();
        symbols.setWeekdays(weekdays);
        formatter.setDateFormatSymbols(symbols);
    }
}

class PermissionsManager {
    public enum ShouldRequestPermissionResult {
        PROCEED,
        REQUEST_PERMISSION,
        CANCEL
    }

    public static void shouldRequestPermission(@NonNull ComponentActivity activity, @NonNull String permission, ActivityResultCallback<ShouldRequestPermissionResult> callback, @Nullable Integer explanation) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED)
            callback.onActivityResult(ShouldRequestPermissionResult.PROCEED);
        else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) && explanation != null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.permission_request_title)
                    .setMessage(explanation)
                    .setPositiveButton(android.R.string.ok, (d, id) -> callback.onActivityResult(ShouldRequestPermissionResult.REQUEST_PERMISSION))
                    .setNegativeButton(android.R.string.cancel, (d, id) -> callback.onActivityResult(ShouldRequestPermissionResult.CANCEL));
            builder.create().show();
        }
        else
            callback.onActivityResult(ShouldRequestPermissionResult.REQUEST_PERMISSION);
    }
}

class Time {
    @SerializedName("current_time")
    private String currentTime;

    public String getCurrentTime() { return currentTime; }
}

class TimeResponse {
    @SerializedName("time_zone")
    private Time timeZone;

    public Time getTimeZone() { return timeZone; }
}

class TimeQuery {
    private static final String URL = "https://api.ipgeolocation.io/ipgeo?apiKey=b03018f75ed94023a005637878ec0977";
    private static final String TAG = "TimeQuery";

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    public static LiveData<Date> requestTime() {
        MutableLiveData<Date> ld = new MutableLiveData<>();
        Request request = new Request.Builder().url(URL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                ResponseBody body = response.body();
                if (body == null) {
                    return;
                }
                try {
                    String str = body.string();
                    TimeResponse rsp = gson.fromJson(str, TimeResponse.class);
                    ld.postValue(dateFormatter.parse(rsp.getTimeZone().getCurrentTime()));

                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        });
        return ld;
    }
}

