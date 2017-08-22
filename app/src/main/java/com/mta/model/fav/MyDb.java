package com.mta.model.fav;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.mta.model.pojo.Child;
import com.mta.util.Functify;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ref:
 * https://android.jlelse.eu/room-persistence-library-typeconverters-and-database-migration-3a7d68837d6c
 * <p>
 * https://github.com/googlesamples/android-architecture-components/blob/e49ba281a341c51fe0cda55e942b25f19bb14f22/PersistenceMigrationsSample/app/src/room3/java/com/example/android/persistence/migrations/UsersDatabase.java
 * <p>
 * I got this error:
 * Warning:(15, 17) Schema export directory is not provided to the annotation processor
 * so we cannot export the schema. You can either provide `room.schemaLocation`
 * annotation processor argument OR set exportSchema to false.
 * solved with: https://stackoverflow.com/a/44424908/1180898
 * <p>
 * Created by amir on 8/22/17.
 */
@Database(entities = {Favorite.class}, version = 1)
public abstract class MyDb extends RoomDatabase {
    private static final Object sLock = new Object();
    private static MyDb INSTANCE;
    private Set<String> allFavIds = null;

    public static MyDb getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        MyDb.class, "fav.db")
//                        .addMigrations(MIGRATION_1_2) // placeholder
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract FavDao getFavoritesDao();

    public boolean isFavofite(String id) {
        if (allFavIds == null) {
            return false;
        }
        return allFavIds.contains(id);
    }


    public boolean isCacheLoaded() {
        return allFavIds != null;
    }

    public void saveFavorite(final Child c) {
        Functify.justRunAsync(new Runnable() {
            @Override
            public void run() {
                getFavoritesDao().insertAll(ChildFavTypeConverter.toFavorite(c));
            }
        });
        if (allFavIds != null) {
            allFavIds.add(ChildFavTypeConverter.getId(c));
        }
    }

    public void deleteFavorite(final Child c) {
        Functify.justRunAsync(new Runnable() {
            @Override
            public void run() {
                getFavoritesDao().delete(ChildFavTypeConverter.getId(c));
            }
        });
        if (allFavIds != null) {
            allFavIds.remove(ChildFavTypeConverter.getId(c));
        }
    }

    /**
     * must be called from a user thread
     */
    public void loadCache() {
        List<String> favs = getFavoritesDao().getAllFavIds();
        allFavIds = new HashSet<>(favs);
    }
}
