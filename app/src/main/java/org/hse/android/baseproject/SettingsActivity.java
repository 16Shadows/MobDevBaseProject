package org.hse.android.baseproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {
    protected final String TAG = "SettingsActivity";
    protected final String SETTINGS_FOLDER = "/settings/";
    protected final String AVATAR_FILE = SETTINGS_FOLDER + "avatar";
    protected final String AVATAR_TEMP_FILE = SETTINGS_FOLDER + "avatar_temporary";
    protected final String AVATAR_TEMP_STORED_FILE = SETTINGS_FOLDER + "avatar_temporary_stored";
    protected final String PREFS_KEY_USERNAME = "username";

    protected ActivityResultLauncher<Uri> takeAvatarPictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), this::avatarPictureTaken);

    protected ImageView avatarView;
    protected TextView nameView;
    protected TextView lightLevel;
    protected TextView sensorsList;

    protected SharedPreferences preferences;
    protected SensorManager sensorManager;
    protected Sensor lightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(this), MODE_PRIVATE);
        sensorManager = (SensorManager)getSystemService(SensorManager.class);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        Toolbar toolbar = findViewById(R.id.activity_settings_Toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        avatarView = findViewById(R.id.activity_settings_Avatar);
        nameView = findViewById(R.id.activity_settings_username);
        lightLevel = findViewById(R.id.activity_settings_lightlevel);
        sensorsList = findViewById(R.id.activity_settings_sensorsList);

        findViewById(R.id.activity_settings_takePictureButton).setOnClickListener(this::dispatchTakePicture);
        findViewById(R.id.activity_settings_SaveButton).setOnClickListener(this::saveSettings);

        File avatar = getAvatarFile();
        if (avatar.exists())
            avatarView.setImageURI(Uri.fromFile(avatar));

        nameView.setText(preferences.getString(PREFS_KEY_USERNAME, ""));

        StringBuilder sList = new StringBuilder();
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor s:
             sensors) {
            sList.append(s.getName());
            sList.append('\n');
        }

        sensorsList.setText(sList.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == lightSensor) {
            setLuxLevel(event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        File tempAvatar = getTempAvatarFile();
        File tempAvatarStored = getTempAvatarStoredFile();

        if (tempAvatar.exists() && !tempAvatar.delete())
            Log.w(TAG, "Failed to delete tempAvatar in onDestroy");

        if (tempAvatarStored.exists() && !tempAvatarStored.delete())
            Log.w(TAG, "Failed to delete tempAvatarStored in onDestroy");
    }

    protected void setLuxLevel(float level) {
        lightLevel.setText(String.format(Locale.getDefault(), getResources().getString(R.string.current_light_level_d), level));
    }

    protected boolean createSettingsFolder()
    {
        File f = new File(getFilesDir() + SETTINGS_FOLDER);
        return f.exists() || f.mkdirs();
    }

    protected File getAvatarFile() {
        if (!createSettingsFolder())
            return null;

        return new File(getFilesDir() + AVATAR_FILE);
    }

    protected File getTempAvatarFile() {
        if (!createSettingsFolder())
            return null;

        return new File(getFilesDir() + AVATAR_TEMP_FILE);
    }

    protected File getTempAvatarStoredFile() {
        if (!createSettingsFolder())
            return null;

        return new File(getFilesDir() + AVATAR_TEMP_STORED_FILE);
    }

    protected void saveSettings(View v) {
        File avatar = getAvatarFile();
        File tempAvatar = getTempAvatarFile();
        File tempAvatarStored = getTempAvatarStoredFile();

        if (tempAvatarStored.exists() && !tempAvatarStored.delete())
            Log.w(TAG, "saveSettings: failed to delete stored temp avatar.");

        if (tempAvatar.exists()) {
            if (avatar.exists() && !avatar.delete()) {
                Log.e(TAG, "saveSettings: failed to delete avatar.");
            } else if (!tempAvatar.renameTo(avatar)) {
                Log.e(TAG, "saveSettings: failed to update avatar.");
                if (!tempAvatar.delete())
                    Log.e(TAG, "saveSettings: failed to delete temp avatar.");
            }
            Log.d(TAG, "saveSettings: Updated avatar.");
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_KEY_USERNAME, nameView.getText().toString());
        editor.apply();
    }

    protected void avatarPictureTaken(Boolean result) {
        File tempAvatar = getTempAvatarFile();
        File tempAvatarStored = getTempAvatarStoredFile();

        if (result || tempAvatarStored.exists() && tempAvatar.delete() && tempAvatarStored.renameTo(tempAvatar)) {
            avatarView.setImageURI(Uri.fromFile(tempAvatar));
        }
        else  {
            tempAvatar.delete();
            Log.e(TAG, "avatarPictureTaken: failed to restore avatar");
            avatarView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.default_avatar, getTheme()));
        }
    }

    protected void dispatchTakePicture(View v)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) == null)
        {
            InstantToast.show(this, R.string.missing_intent_handler, Toast.LENGTH_SHORT);
            return;
        }

        File tempAvatar = getTempAvatarFile();
        File tempAvatarStored = getTempAvatarStoredFile();

        boolean tempAvatarExisted = tempAvatar.exists();

        try {
            if (tempAvatarStored.exists() && !tempAvatarStored.delete() || tempAvatar.exists() && !tempAvatar.renameTo(tempAvatarStored) || !tempAvatar.exists() && !tempAvatar.createNewFile()) {
                InstantToast.show(this, R.string.file_error, Toast.LENGTH_SHORT);
                return;
            }
        } catch (IOException e) {
            Log.e(TAG, "dispatchTakePicture", e);
        }

        if (tempAvatarExisted)
            avatarView.setImageURI(Uri.fromFile(tempAvatarStored));

        Uri tempAvatarUri = FileProvider.getUriForFile(this, AppFilesProvider.getAuthority(this), tempAvatar);
        takeAvatarPictureLauncher.launch(tempAvatarUri);
    }
}