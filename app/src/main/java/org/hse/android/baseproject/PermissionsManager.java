package org.hse.android.baseproject;

import android.app.AlertDialog;
import android.content.pm.PackageManager;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsManager {
    public static void shouldRequestPermission(@NonNull ComponentActivity activity, @NonNull String permission, ActivityResultCallback<Boolean> callback, @Nullable Integer explanation) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED)
            callback.onActivityResult(false);
        else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) && explanation != null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.permission_request_title)
                    .setMessage(explanation)
                    .setPositiveButton(android.R.string.ok, (d, id) -> callback.onActivityResult(true))
                    .setNegativeButton(android.R.string.cancel, (d, id) -> callback.onActivityResult(false));
            builder.create().show();
        }
        else
            callback.onActivityResult(true);
    }
}
