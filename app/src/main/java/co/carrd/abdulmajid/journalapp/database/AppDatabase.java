package co.carrd.abdulmajid.journalapp.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import co.carrd.abdulmajid.journalapp.database.dao.IdeasDAO;
import co.carrd.abdulmajid.journalapp.model.Idea;

/**
 * Created by admin on 6/30/18.
 */

@Database(entities = {Idea.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "idea_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract IdeasDAO ideaModel();
}
