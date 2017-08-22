package com.mta.model.fav;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * reference:
 * https://developer.android.com/topic/libraries/architecture/room.html
 *
 * Created by amir on 8/22/17.
 */
@Entity
public class Favorite {
    /**
     * Using same representation as reddit's: type_id
     */
    @PrimaryKey
    private String id;

    @ColumnInfo(name = "thumb")
    private String thumbnailImgUrl;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "url")
    private String url;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnailImgUrl() {
        return thumbnailImgUrl;
    }

    public void setThumbnailImgUrl(String thumbnailImgUrl) {
        this.thumbnailImgUrl = thumbnailImgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
