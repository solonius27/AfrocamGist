package com.solz.afrocamgist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.developer.kalert.KAlertDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.solz.afrocamgist.Data.Models.Comments;
import com.solz.afrocamgist.Data.Models.TLData;
import com.solz.afrocamgist.Data.Network.GetDataService;
import com.solz.afrocamgist.Data.Network.RetrofitClientInstance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    Spinner spinner;
    CustomAdapter adapter;
    List<TLData> pacList;
    //List<Comments> commentList;

    private KAlertDialog prog;

    static GetDataService service;
    public static final String TOKEN = "1349c73031d66e55d10d3fb98bc38fb8039140ca8401845c7b5cb159e06b5c0f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        prog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        prog.setTitleText("Fetching Data");
        prog.setCancelable(false);
        //prog.show();

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource(this,
                R.array.TLspinner, android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinneradapter);

        spinner.setOnItemSelectedListener(this);

        CustomAdapter.RecyclerViewClickListener listener = new CustomAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position, List<TLData> dataEntries) {

                TLData data;
                List<TLData> mEntries = dataEntries;

                data = mEntries.get(position);

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("imageUrl", data.getProfileImageUrl());
                intent.putExtra("username", data.getFirstName());
                intent.putExtra("maintext", data.getPostText());
                intent.putExtra("likecount",  String.valueOf(data.getLikeCount()));
                intent.putExtra("commentcount", String.valueOf(data.getCommentCount()));
                intent.putExtra("postId", data.getPostId());

                startActivity(intent);


            }
        };

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(this, listener);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);

        pacList = new ArrayList<>();
        //commentList = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        final Call<JsonObject> call = service.getAfroSwaggerPost("Bearer " + TOKEN);
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                prog.dismissWithAnimation();
//                final JsonObject body = response.body();
//                String postText;
//                if (body != null){
//                    final JsonArray data = body.getAsJsonArray("data");
//                    for (int i = 0; i<data.size(); i++){
//                        final JsonObject jsonElement =  (JsonObject) data.get(i);
//                        final JsonElement id = jsonElement.get("_id");
//                        final JsonElement post_text = jsonElement.get("post_text");
//                        final JsonElement like_count = jsonElement.get("like_count");
//                        final JsonElement comment_count = jsonElement.get("comment_count");
//                        final JsonElement first_name = jsonElement.get("first_name");
//                        final JsonElement user_id = jsonElement.get("user_id");
//                        final JsonElement profile_image_url = jsonElement.get("profile_image_url");
//
//                        if (post_text.getAsString().isEmpty()){
//                            postText = "Test string used to replace empty post";
//                        }else{
//                            postText = post_text.getAsString();
//                        }
//
//                        pacList.add(new TLData(id.toString(),
//                                postText,
//                                Integer.parseInt(like_count.getAsString()),
//                                Integer.parseInt(comment_count.getAsString()),
//                                first_name.getAsString(),
//                                Integer.parseInt(user_id.getAsString()),
//                                profile_image_url.getAsString()));
//
//                        adapter.setTasks(pacList);
//
//                        Log.d("dsdsd", pacList.toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        KAlertDialog f = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        f.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        f.setTitleText("Fetching Data");
        f.setCancelable(false);

        if (adapterView.getId() == R.id.spinner){
            final Object itemAtPosition = adapterView.getItemAtPosition(i);
            if(itemAtPosition.equals("Afro Talent")){

                f.show();
                adapter.clearTasks(pacList);

                final Call<JsonObject> call = service.getAfroTalentPost("Bearer " + TOKEN);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        f.dismissWithAnimation();
                        final JsonObject body = response.body();
                        String postText;
                        if (body != null){
                            final JsonArray data = body.getAsJsonArray("data");
                            for (int i = 0; i<data.size(); i++){
                                final JsonObject jsonElement =  (JsonObject) data.get(i);
                                final JsonElement id = jsonElement.get("_id");
                                final JsonElement post_text = jsonElement.get("post_text");
                                final JsonElement like_count = jsonElement.get("like_count");
                                final JsonElement comment_count = jsonElement.get("comment_count");
                                final JsonElement first_name = jsonElement.get("first_name");
                                final JsonElement post_id = jsonElement.get("post_id");
                                final JsonElement profile_image_url = jsonElement.get("profile_image_url");

//                                final JsonArray comments = jsonElement.getAsJsonArray("comments");
//                                if (comments != null){
//                                    for(int j =0; j<comments.size(); j++){
//                                        final JsonObject commentObj = (JsonObject) comments.get(j);
//                                        final JsonElement comment_text = commentObj.get("comment_text");
//                                        final JsonObject commented_by = commentObj.getAsJsonObject("commented_by");
//                                        final JsonElement first_name1 = commented_by.get("first_name");
//                                        final JsonElement profile_image_url1 = commented_by.get("profile_image_url");
//
//                                        commentList.add(new Comments(comment_text.getAsString(), profile_image_url1.getAsString(), first_name1.getAsString()));
//
//                                    }
//
//
//                                }

                                if (post_text.getAsString().isEmpty()){
                                    postText = "Test string used to replace empty post";
                                }else{
                                    postText = post_text.getAsString();
                                }

                                pacList.add(new TLData(id.toString(),
                                        postText,
                                        Integer.parseInt(like_count.getAsString()),
                                        Integer.parseInt(comment_count.getAsString()),
                                        first_name.getAsString(),
                                        Integer.parseInt(post_id.getAsString()),
                                        profile_image_url.getAsString()));



                                adapter.setTasks(pacList);

                                Log.d("dsdsd", pacList.toString());
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

            }else if (itemAtPosition.equals("Afro Swagger")){

                f.show();
                adapter.clearTasks(pacList);

                final Call<JsonObject> call = service.getAfroSwaggerPost("Bearer " + TOKEN);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        f.dismissWithAnimation();
                        final JsonObject body = response.body();
                        String postText;
                        if (body != null){
                            final JsonArray data = body.getAsJsonArray("data");
                            for (int i = 0; i<data.size(); i++){
                                final JsonObject jsonElement =  (JsonObject) data.get(i);
                                final JsonElement id = jsonElement.get("_id");
                                final JsonElement post_text = jsonElement.get("post_text");
                                final JsonElement like_count = jsonElement.get("like_count");
                                final JsonElement comment_count = jsonElement.get("comment_count");
                                final JsonElement first_name = jsonElement.get("first_name");
                                final JsonElement post_id = jsonElement.get("post_id");
                                final JsonElement profile_image_url = jsonElement.get("profile_image_url");


//                                final JsonArray comments = jsonElement.getAsJsonArray("comments");
//                                if (comments != null){
//                                    for(int j =0; j<comments.size(); j++){
//                                        final JsonObject commentObj = (JsonObject) comments.get(j);
//                                        final JsonElement comment_text = commentObj.get("comment_text");
//                                        final JsonObject commented_by = commentObj.getAsJsonObject("commented_by");
//                                        final JsonElement first_name1 = commented_by.get("first_name");
//                                        final JsonElement profile_image_url1 = commented_by.get("profile_image_url");
//
//                                        commentList.add(new Comments(comment_text.getAsString(), profile_image_url1.getAsString(), first_name1.getAsString()));
//
//                                    }
//
//
//                                }

                                if (post_text.getAsString().isEmpty()){
                                    postText = "Test string used to replace empty post";
                                }else{
                                    postText = post_text.getAsString();
                                }

                                pacList.add(new TLData(id.toString(),
                                        postText,
                                        Integer.parseInt(like_count.getAsString()),
                                        Integer.parseInt(comment_count.getAsString()),
                                        first_name.getAsString(),
                                        Integer.parseInt(post_id.getAsString()),
                                        profile_image_url.getAsString()));

                                adapter.setTasks(pacList);

                                Log.d("dsdsd", pacList.toString());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

            }else if(itemAtPosition.equals("Most Popular")){

                f.show();
                adapter.clearTasks(pacList);

                final Call<JsonObject> call = service.getMostPopular("Bearer " + TOKEN);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        f.dismissWithAnimation();
                        final JsonObject body = response.body();
                        String postText;
                        if (body != null){
                            final JsonArray data = body.getAsJsonArray("data");
                            for (int i = 0; i<data.size(); i++){
                                final JsonObject jsonElement =  (JsonObject) data.get(i);
                                final JsonElement id = jsonElement.get("_id");
                                final JsonElement post_text = jsonElement.get("post_text");
                                final JsonElement like_count = jsonElement.get("like_count");
                                final JsonElement comment_count = jsonElement.get("comment_count");
                                final JsonElement first_name = jsonElement.get("first_name");
                                final JsonElement post_id = jsonElement.get("post_id");
                                final JsonElement profile_image_url = jsonElement.get("profile_image_url");

//                                final JsonArray comments = jsonElement.getAsJsonArray("comments");
//                                if (comments != null){
//                                    for(int j =0; j<comments.size(); j++){
//                                        final JsonObject commentObj = (JsonObject) comments.get(j);
//                                        final JsonElement comment_text = commentObj.get("comment_text");
//                                        final JsonObject commented_by = commentObj.getAsJsonObject("commented_by");
//                                        final JsonElement first_name1 = commented_by.get("first_name");
//                                        final JsonElement profile_image_url1 = commented_by.get("profile_image_url");
//
//                                        commentList.add(new Comments(comment_text.getAsString(), profile_image_url1.getAsString(), first_name1.getAsString()));
//
//                                    }
//
//
//                                }

                                if (post_text.getAsString().isEmpty()){
                                    postText = "Test string used to replace empty post";
                                }else{
                                    postText = post_text.getAsString();
                                }

                                pacList.add(new TLData(id.toString(),
                                        postText,
                                        Integer.parseInt(like_count.getAsString()),
                                        Integer.parseInt(comment_count.getAsString()),
                                        first_name.getAsString(),
                                        Integer.parseInt(post_id.getAsString()),
                                        profile_image_url.getAsString()));

                                adapter.setTasks(pacList);

                                Log.d("dsdsd", pacList.toString());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
