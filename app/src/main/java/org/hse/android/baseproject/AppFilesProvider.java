package org.hse.android.baseproject;

import android.content.Context;

import androidx.core.content.FileProvider;

public class AppFilesProvider extends FileProvider {
    public AppFilesProvider() {
        super(R.xml.provided_file_paths);
    }

    public static String getAuthority(Context ctx) {
        return ctx.getPackageName() + ".FileProvider";
    }
}
