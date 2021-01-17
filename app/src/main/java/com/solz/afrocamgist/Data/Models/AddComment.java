package com.solz.afrocamgist.Data.Models;

import com.google.gson.annotations.SerializedName;

public class AddComment {

    @SerializedName("post_id")
    private String post_id;
    @SerializedName("cmment_text")
    private String cmment_text;

    public AddComment(String post_id, String cmment_text) {
        this.post_id = post_id;
        this.cmment_text = cmment_text;
    }
}
