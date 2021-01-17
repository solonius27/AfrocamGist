package com.solz.afrocamgist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.solz.afrocamgist.Data.Models.AddComment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.solz.afrocamgist.MainActivity.TOKEN;

public class DetailsActivity extends AppCompatActivity {

    ImageView image;
    TextView username, main_post, likes_count, comment_count, comments_section;
    LinearLayout likeLi, commentLi;
    EditText editText;
    Button button;
    RecyclerView recyclerView;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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

        commentLi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        HashMap<String, Object> hm = new HashMap<String, Object>();
//                        hm.put("post_id", post_id );
//                        hm.put("cmment_text", editText.getText().toString());

//                        final Call<JsonObject> call = MainActivity.service.postComment("Bearer " + TOKEN, new AddComment("post_id", "cmment_text"));
//                        call.enqueue(new Callback<JsonObject>() {
//                            @Override
//                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                                final JsonObject body = response.body();
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                            }
//                        });

                        Toast.makeText(DetailsActivity.this, "Functionality not complete. Try again later", Toast.LENGTH_SHORT).show();
                        editText.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);


                    }
                });
            }
        });
    }
}
