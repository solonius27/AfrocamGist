package com.solz.afrocamgist.Data.Network;

import com.google.gson.JsonObject;
import com.solz.afrocamgist.Data.Models.AddComment;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GetDataService {

    @GET("/api/posts/afroswagger")
    Call<JsonObject> getAfroSwaggerPost(@Header("Authorization") String token);

    @GET("/api/posts/afrotalent")
    Call<JsonObject> getAfroTalentPost(@Header("Authorization") String token);

    @GET("/api/posts/most-popular")
    Call<JsonObject> getMostPopular(@Header("Authorization") String token);

    @GET("/api/posts/hashtag/{hashtag}")
    Call<JsonObject> getHashtagSearch(@Header("Authorization") String token, @Path("hashtag") String hash);

    @GET("/api/posts/{postID}")
    Call<JsonObject> getPostIdSearch(@Header("Authorization") String token, @Path("postID") String hash);

    @DELETE("/api/posts/{postid}")
    Call<JsonObject> deletePost(@Header("Authorization") String token, @Path("postid") String post);


    @POST("/api/comments/")
    Call<JsonObject> postComment(@Header("Authorization") String token, @Body AddComment body);





}
