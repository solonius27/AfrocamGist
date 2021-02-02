package com.solz.afrocamgist;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.solz.afrocamgist.MainActivity.TOKEN;
import static com.solz.afrocamgist.MainActivity.service;

public class AddActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 23;

    EditText editText;
    Button button, addImage, addVideo;
    ImageView prevImage;

    String postType = "text";
    boolean addImageclick = false;
    boolean addVideoClick = false;


    String fileuploadpathString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        Dexter.withContext(AddActivity.this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //Toast.makeText(AddActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(AddActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();







        editText = findViewById(R.id.post_editText);
        button = findViewById(R.id.post_button);
        addImage = findViewById(R.id.add_image_button);
        addVideo = findViewById(R.id.add_video_button);
        prevImage = findViewById(R.id.preview_image);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevImage.setVisibility(View.VISIBLE);


                new ImagePicker.Builder(AddActivity.this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();


                addImageclick = true;
                addVideo.setEnabled(false);
                postType = "image";
//                Snackbar.make(view, "Static image Added to post", BaseTransientBottomBar.LENGTH_SHORT).show();

            }
        });

        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVideoClick = true;
                addImage.setEnabled(false);
                postType = "video";
                Snackbar.make(view, "Static video Added to post", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });









        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.getText().toString().trim().isEmpty()){
                    Snackbar.make(view, "Field cannot be empty", BaseTransientBottomBar.LENGTH_SHORT ).show();
                    return;
                }

                String[] empty1 = {};
                String[] imagx = {fileuploadpathString};

               // "uploads/posts/2021/02/files-1612269025549.jpg"


                HashMap<String, Object> hm = new HashMap<>();
                hm.put("post_text", editText.getText().toString());
                hm.put("post_lat_long", "");
                hm.put("tagged_id", empty1);
                hm.put("post_type", postType);
                hm.put("bg_image_post", false);
                hm.put("bg_map_post", false);
                hm.put("bg_image", "");
                if (addImageclick){
                    hm.put("post_image", imagx);
                }else{
                    hm.put("post_image", empty1);
                }
                if (addVideoClick){
                    hm.put("post_video", "uploads/posts/2020/07/file-1596054935118.mp4");
                    hm.put("thumbnail", "uploads/posts/2020/07/thumbs/file-1596054935118-thumbnail-640x640-0010.png");
                }else{
                    hm.put("post_video", "");
                    hm.put("thumbnail", "");
                }
                hm.put("group_id", "");
                hm.put("share_post_id", "");
                hm.put("post_privacy", "friends");

                final Call<JsonObject> call = MainActivity.service.newPost("Bearer " + TOKEN, hm);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        editText.setText("");
                        final JsonObject body = response.body();
                        final JsonElement status = body.get("status");

                        if (status.getAsBoolean()){
                            Snackbar.make(view, "Post Added...Return to main Page", BaseTransientBottomBar.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Snackbar.make(view, "Something went wrong...Try Again", BaseTransientBottomBar.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            Log.d("hshhh", mPaths.get(0));
            File file = new File(mPaths.get(0));
            prevImage.setImageBitmap(BitmapFactory.decodeFile(mPaths.get(0)));


            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("files", file.getName(), requestFile);

            final Call<JsonObject> call3 = service.uploadImage("Bearer " + TOKEN, body, requestFile);
            call3.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    final JsonObject body = response.body();
                    if (body != null) {
                        final JsonArray data1 = body.getAsJsonArray("data");
                        final JsonObject jsonElement = (JsonObject) data1.get(0);
                        final JsonElement path = jsonElement.get("path");

                        fileuploadpathString = path.getAsString();

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });


//            HashMap<String, Object> hmv = new HashMap<>();
//            hmv.put("files", file);
//
//
//            final Call<JsonObject> call = service.uploadImages("Bearer " + TOKEN, hmv);
//            call.enqueue(new Callback<JsonObject>() {
//                @Override
//                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    response.body();
//                    String have = "jdjfjf";
//                }
//
//                @Override
//                public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                }
//            });


            //RequestBody body = RequestBody.create(MultipartBody.FORM, "files");
//            RequestBody filepart = RequestBody.create(MediaType.parse("image/*"), file);
//
//            MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), filepart);
//
//            RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), "files");




            //Your Code
        }
    }
}
