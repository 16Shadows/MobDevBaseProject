package org.hse.android.baseproject;

import android.app.Application;
import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Transaction;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class DatabaseProvider {


    private final HseTimetableDatabase hseTimetableDatabase;

    private DatabaseProvider(Context context) {
        context.deleteDatabase(HseTimetableDatabase.DATABASE_NAME);
        hseTimetableDatabase = Room.databaseBuilder(context, HseTimetableDatabase.class, HseTimetableDatabase.DATABASE_NAME)
                                   .addCallback(new RoomDatabase.Callback() {
                                       @Override
                                       public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                           Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                               @Override
                                               public void run() {
                                                   new Thread(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           HseTimetableDatabase.initializeDummyData(getHseTimetableDatabase().hseTimetableDao());
                                                       }
                                                   }).start();
                                               }
                                           });
                                       }
                                   })
                                   .build();
    }

    public HseTimetableDatabase getHseTimetableDatabase() {
        return hseTimetableDatabase;
    }

    private static volatile DatabaseProvider instance;

    public static DatabaseProvider getInstance(Context ctx) {
        if (instance == null) {
            synchronized (DatabaseProvider.class) {
                if (instance == null) {
                    instance = new DatabaseProvider(ctx);
                }
            }
        }
        return instance;
    }
}
