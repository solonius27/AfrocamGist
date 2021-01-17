package com.solz.afrocamgist.Data.Models;

public class Comments {

    private String commentText;
    private String profilepic;
    private String name;

    public Comments(String commentText, String profilepic, String name) {
        this.commentText = commentText;
        this.profilepic = profilepic;
        this.name = name;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
