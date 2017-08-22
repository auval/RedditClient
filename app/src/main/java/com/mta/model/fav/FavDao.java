package com.mta.model.fav;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by amir on 8/22/17.
 */
@Dao
public interface FavDao {
    @Query("SELECT * FROM favorite")
    List<Favorite> getAll();

    @Query("SELECT id FROM favorite")
    List<String> getAllFavIds();

    @Query("SELECT * FROM favorite WHERE id IN (:ids)")
    List<Favorite> loadAllByIds(int[] ids);


    @Query("SELECT * FROM favorite WHERE title LIKE :filter")
    List<Favorite> filterFavs(String filter);

    @Insert
    void insertAll(Favorite... favorites);

    @Delete
    void delete(Favorite favorite);

    @Query("DELETE from favorite WHERE id = (:id)")
    int delete(String id);

}
