package org.nicknelson.roomwithaview;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;

@Database(entities = {WordEntity.class}, version = 1)
public abstract class RoomDatabase extends android.arch.persistence.room.RoomDatabase {

    public abstract WordDao wordDao();

    private static volatile RoomDatabase INSTANCE;

    static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
