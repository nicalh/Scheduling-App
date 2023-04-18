package com.main.Caller;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {StaffInfo.class, ShiftInfo.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class LocalDB extends RoomDatabase {

    public abstract StaffDao getStaffDao();
    public abstract ShiftDao getShiftDao();

    //This builds the database at the start of the app
    public static LocalDB INSTANCE;

    public static LocalDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDB.class) {
                if (INSTANCE == null) {
                    String DATABASE = "Schedule-Database";
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            LocalDB.class,
                            DATABASE)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}


