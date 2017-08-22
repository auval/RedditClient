package com.mta.model.fav;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.mta.model.pojo.Child;
import com.mta.redditclient.IListPresenter;
import com.mta.util.Functify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * There are 2 caches managed here:
 * 1. favorites ids (as Strings)
 * 2. favorites list
 * <p>
 * both are memory caches, and also persisted in the DB
 * they are loaded on app start, and when the loading is complete - the ui is told to update itself.
 * <p>
 * (see comment below on implementation considerations)
 * <p>
 * Room ref:
 * https://android.jlelse.eu/room-persistence-library-typeconverters-and-database-migration-3a7d68837d6c
 * <p>
 * https://github.com/googlesamples/android-architecture-components/blob/e49ba281a341c51fe0cda55e942b25f19bb14f22/PersistenceMigrationsSample/app/src/room3/java/com/example/android/persistence/migrations/UsersDatabase.java
 * <p>
 * Created by amir on 8/22/17.
 */
@Database(entities = {Favorite.class}, version = 1)
public abstract class MyDb extends RoomDatabase {
    private static final Object sLock = new Object();
    private static final String TAG = MyDb.class.getSimpleName();
    private static MyDb INSTANCE;
    boolean loadingCache = false;
    /**
     * a set is the most efficient data structure to check if an id is in favorites or not.
     * That's O(1)
     */
    private Set<String> cachedFavIds = null;
    /**
     * -au/
     * The DB stores List<Favorite> in order to keep it as small as possible.
     * But the list adapter uses Child objects, because there are much more of those
     * and I want to keep the type conversions to a minimum.
     * <p>
     * I considered creating an interface or a proxy class to use either Child or Favorite
     * object, and let the adapter run on the interface. But it would complicate the implementation
     * and also require excessive object creation and deletion (interface solution), or managing a pool
     * of the proxy classes, with added complexity and room for bugs.
     * <p>
     * Therefore the simplest solution is converting Favorites (database type) to Child (model type)
     * ana so cache is stored as a list of Child objects.
     */
    private List<Child> cachedFavs = null;

    public static MyDb getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        MyDb.class, "fav.db")
//                        .addMigrations(MIGRATION_1_2) // placeholder for future db versions
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract FavDao getFavoritesDao();

    /**
     * logic: if cache is not ready yet, return false in the mean time.
     *
     * @param id
     * @param callback relevant if cache is not loaded
     * @return
     */
    public boolean isFavorite(String id, IListPresenter callback) {

        if (isCacheLoaded()) {
            return cachedFavIds.contains(id);
        } else {

            loadCacheAsync(callback);
            // for now return false, wait until the fav list is ready, and a callback will be sent
            return false;
        }

    }

    /**
     * here I'm using a utility class I've written in the past.
     * It's allowing to setup a series of operations to be performed one after the other,
     * each on its designated thread, user or main.
     * <p>
     * (and released open source https://github.com/auval/Functify)
     */
    private void loadCacheAsync(final IListPresenter callback) {
        if (loadingCache) {
            // allow to load the cache only once,
            return;
        }
        loadingCache = true;

        Log.i(TAG, "loading cache...");
        Functify.FuncFlow fb = Functify.newFlow();
        fb.setExceptionHandler(new Functify.FExceptionHandler() {
            @Override
            public void onFException(Throwable e) {
                e.printStackTrace();
            }
        });
        fb.runAsync(new Functify.Func() {
            @Override
            public void onExecute(Bundle b) {
                loadCacheBlocking();
            }
        }).runOnMain(new Functify.Func() {
            @Override
            public void onExecute(Bundle b) {
                Log.i(TAG, "cache is loaded");
                callback.onDataChanged();

            }
        }).execute();

    }


    public boolean isCacheLoaded() {
        return cachedFavIds != null;
    }

    /**
     * update both memory cache and DB
     *
     * @param c
     */
    public void saveFavorite(final Child c) {
        Functify.justRunAsync(new Runnable() {
            @Override
            public void run() {
                getFavoritesDao().insertAll(TypeConverters.toFavorite(c));
            }
        });
        if (cachedFavIds != null) {
            cachedFavIds.add(TypeConverters.getId(c));
        }
        if (cachedFavs != null) {
            cachedFavs.add(c);
        }
    }

    /**
     * update both memory cache and DB
     *
     * @param c
     */
    public void deleteFavorite(final Child c) {
        Functify.justRunAsync(new Runnable() {
            @Override
            public void run() {
                getFavoritesDao().delete(TypeConverters.getId(c));
            }
        });
        if (cachedFavIds != null) {
            cachedFavIds.remove(TypeConverters.getId(c));
        }
        if (cachedFavs != null) {
            cachedFavs.remove(c);
        }
    }

    /**
     * must be called from a user thread
     */
    public void loadCacheBlocking() {
//        List<String> favs = getFavoritesDao().getAllFavIds();
//        cachedFavIds = new HashSet<>(favs);

        cachedFavIds = new HashSet<>();
        cachedFavs = new ArrayList<>();

        List<Favorite> all = getFavoritesDao().getAll();
        for (Favorite f : all) {
            cachedFavIds.add(f.getId());
            cachedFavs.add(TypeConverters.toChild(f));
        }
    }

    /**
     * get favorites with cache and db
     *
     * @return
     */
    public List<Child> getFavorites() {
        return cachedFavs;
    }
}
