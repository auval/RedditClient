package com.mta.model.fav;

import android.arch.persistence.room.TypeConverter;

import com.mta.model.pojo.Child;
import com.mta.model.pojo.Data_;

/**
 * converting entities Child <=> Favorite
 * <p>
 * <p>
 * Created by amir on 8/22/17.
 */

public class ChildFavTypeConverter {


    public static Child toChild(Favorite fav) {
        Child c = new Child();
        Data_ d = new Data_();
        c.setData(d);

        String[] split = getId(fav).split("_");
        c.setKind(split[0]);
        d.setId(split[1]);
        d.setUrl(fav.getUrl());
        d.setThumbnail(fav.getThumbnailImgUrl());

        return c;
    }

    @TypeConverter
    public static Favorite toFavorite(Child c) {
        Favorite f = new Favorite();

        f.setId(getId(c));
        f.setUrl(c.getData().getUrl());
        f.setThumbnailImgUrl(c.getData().getThumbnail());

        return f;
    }

    /**
     * must return the same id as getId(fav) of the same instance
     * @param c
     * @return
     */
    public static String getId(Child c) {
        return c.getKind() + "_" + c.getData().getId();
    }

    /**
     * must return the same id as getId(child) of the same instance
     * @param c
     * @return
     */
    public static String getId(Favorite f) {
        return f.getId();
    }

}
