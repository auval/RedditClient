package com.mta.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * auto generated using jsonschema2pojo.org
 * but I've added fields for filtering at the bottom
 */
public class Child {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("data")
    @Expose
    private Data_ data;


    /**
     * filtering highlight helper
     * -au/
     */
    private int matchAt = -1;
    private int length;
    /**
     * for more efficient matching
     */
    private String lowercaseTitle = null;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Data_ getData() {
        return data;
    }

    public void setData(Data_ data) {
        this.data = data;
    }

    public int getMatchAt() {
        return matchAt;
    }

    public void setMatchAt(int matchAt, int length) {
        this.matchAt = matchAt;
        this.length = length;
    }

    public String getLowercaseTitle() {
        if (lowercaseTitle == null) {
            lowercaseTitle = data.getTitle().toLowerCase();
        }
        return lowercaseTitle;
    }

    public int getLength() {
        return length;
    }
}
