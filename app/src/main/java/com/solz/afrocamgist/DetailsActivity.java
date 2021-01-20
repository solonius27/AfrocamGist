package com.solz.afrocamgist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.solz.afrocamgist.Data.Models.Comments;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.LinearLayout.VERTICAL;
import static com.solz.afrocamgist.MainActivity.TOKEN;

public class DetailsActivity extends AppCompatActivity {

    ImageView image;
    TextView username, main_post, likes_count, comment_count, comments_section;
    LinearLayout likeLi, commentLi;
    EditText editText;
    Button button;
    RecyclerView recyclerView;
    CommentsAdapter adapter;
    List<Comments> commList;

    KAlertDialog a;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        a = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        a.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        a.setTitleText("Fetching Data");
        a.setCancelable(false);
        a.show();


        image = findViewById(R.id.detail_image);
        username = findViewById(R.id.detail_username);
        main_post = findViewById(R.id.detail_mainText);
        likes_count = findViewById(R.id.detail_likesCount);
        comment_count = findViewById(R.id.detail_commentCount);
        likeLi = findViewById(R.id.detail_likes);
        commentLi = findViewById(R.id.detail_comment);
        editText = findViewById(R.id.edit_comment);
        button = findViewById(R.id.button);
        comments_section = findViewById(R.id.comment_section);


        recyclerView = findViewById(R.id.recycler_comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentsAdapter(this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);

        commList = new ArrayList<>();

         intent = getIntent();
         String imgurl = intent.getStringExtra("imageUrl");
         String usernamex = intent.getStringExtra("username");
         String textx = intent.getStringExtra("maintext");
         String likC = intent.getStringExtra("likecount");
         String commC = intent.getStringExtra("commentcount");
         String post_id = intent.getStringExtra("postId");

        Picasso.get().load(String.format("https://cdn.staging.afrocamgist.com/%s", imgurl)).placeholder(R.drawable.profilepic).into(image);
        username.setText(String.format("@%s", usernamex));
        main_post.setText(textx);
        likes_count.setText(likC);
        comment_count.setText(commC);


        pageData(post_id, a);


        commentLi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.getText().toString().trim().isEmpty()){
                    return;
                }



                editText.setVisibility(View.GONE);
                button.setVisibility(View.GONE);

                HashMap<String, Object> hm = new HashMap<>();
                hm.put("post_id", Integer.parseInt(post_id) );
                hm.put("comment_text", editText.getText().toString());

                editText.setText("");

                Snackbar.make(view, "Adding Comment....", BaseTransientBottomBar.LENGTH_SHORT).show();

                final Call<JsonObject> call = MainActivity.service.postComment("Bearer " + TOKEN, hm );
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        final JsonObject body = response.body();

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });


                final Call<JsonObject> call2 = MainActivity.service.getPostIdSearch("Bearer " + TOKEN, post_id);
                call2.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        final JsonObject body = response.body();
                        if (body != null) {
                            final JsonObject data = body.getAsJsonObject("data");
                            final JsonElement count_element = data.get("comment_count");
                            comment_count.setText(count_element.getAsString());
                            final JsonArray comments = data.getAsJsonArray("comments");

                            if(comments != null){
                                recyclerView.setVisibility(View.VISIBLE);
                                adapter.clearTasks(commList);

                                for(int i = 0; i<comments.size(); i++){
                                    final JsonObject jsonElement = (JsonObject) comments.get(i);
                                    final JsonElement comment_text = jsonElement.get("comment_text");
                                    final JsonObject commented_by = jsonElement.getAsJsonObject("commented_by");
                                    final JsonElement first_name = commented_by.get("first_name");
                                    final JsonElement profile_image_url = commented_by.get("profile_image_url");




                                    commList.add(new Comments(comment_text.getAsString(), profile_image_url.getAsString(), first_name.getAsString()));

                                    adapter.setTasks(commList);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });










//                pageData(post_id, b);

            }
        });
    }

    private void pageData(String post_id, KAlertDialog alert) {
        final Call<JsonObject> call = MainActivity.service.getPostIdSearch("Bearer " + TOKEN, post_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                alert.dismissWithAnimation();
                final JsonObject body = response.body();
                if (body != null) {
                    final JsonObject data = body.getAsJsonObject("data");
                    final JsonArray comments = data.getAsJsonArray("comments");

                    if(comments != null){
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.clearTasks(commList);

                        for(int i = 0; i<comments.size(); i++){
                            final JsonObject jsonElement = (JsonObject) comments.get(i);
                            final JsonElement comment_text = jsonElement.get("comment_text");
                            final JsonObject commented_by = jsonElement.getAsJsonObject("commented_by");
                            final JsonElement first_name = commented_by.get("first_name");
                            final JsonElement profile_image_url = commented_by.get("profile_image_url");




                            commList.add(new Comments(comment_text.getAsString(), profile_image_url.getAsString(), first_name.getAsString()));

                            adapter.setTasks(commList);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
