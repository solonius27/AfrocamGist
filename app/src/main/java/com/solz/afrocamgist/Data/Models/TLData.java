package com.solz.afrocamgist.Data.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TLData {

    private String id;
    private String postText;
    private int likeCount;
    private int commentCount;
    private String firstName;
    private int postId;
    private String profileImageUrl;



    public TLData(String id, String postText, int likeCount, int commentCount, String firstName, int postId, String profileImageUrl) {
        this.id = id;
        this.postText = postText;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.firstName = firstName;
        this.postId = postId;
        this.profileImageUrl = profileImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }




}
