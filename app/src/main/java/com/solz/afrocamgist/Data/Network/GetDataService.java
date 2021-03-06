package com.solz.afrocamgist.Data.Network;

import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @POST("/api/comments")
    @Headers("Content-Type: application/json")
    Call<JsonObject> postComment(@Header("Authorization") String token, @Body HashMap<String, Object> body);


    @POST("/api/posts")
    @Headers("Content-Type: application/json")
    Call<JsonObject> newPost(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    @POST("/api/likes")
    @Headers("Content-Type: application/json")
    Call<JsonObject> postLike(@Header("Authorization") String token, @Body HashMap<String, Object> body);

    @Multipart
    @POST("/api/posts/upload")
    Call<JsonObject> uploadImage(@Header("Authorization") String token, @Part MultipartBody.Part file,
                                 @Part("items") RequestBody items);

//    @Multipart
//    @POST("/api/posts/upload")
//    Call<ResponseBody> uploadImage(@Header("Authorization") String token, @Part("files")RequestBody filev, @Part MultipartBody.Part file);








}
