
package com.mta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * I created the initial pojo's using http://www.jsonschema2pojo.org/
 *
 * api ref: https://github.com/reddit/reddit/wiki/JSON
 *
 * -au
 */
public class RedditPojo {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
