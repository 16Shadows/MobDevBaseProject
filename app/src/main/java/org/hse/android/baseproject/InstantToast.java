package org.hse.android.baseproject;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.UiContext;

public class InstantToast {
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
