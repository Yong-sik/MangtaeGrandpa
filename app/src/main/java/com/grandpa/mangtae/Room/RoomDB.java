package com.grandpa.mangtae.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.grandpa.mangtae.Room.CallingDao;
import com.grandpa.mangtae.Room.CallingData;

@Database(entities = {CallingData.class}, version = 1)
public abstract class RoomDB extends RoomDatabase
{
    private static RoomDB database;

    private static final String DATABASE_NAME = "MangTae";

    public synchronized static RoomDB getInstance(Context context)
    {
//        Converters converters = new Converters();
        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
//                    .addTypeConverter(converters)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract CallingDao callingDao();
}