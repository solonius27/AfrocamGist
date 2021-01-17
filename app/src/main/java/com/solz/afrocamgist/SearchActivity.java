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
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.solz.afrocamgist.Data.Models.TLData;
import com.solz.afrocamgist.Data.Network.GetDataService;
import com.solz.afrocamgist.Data.Network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText searchView;
    Button button;
    RecyclerView recyclerView;
    CustomAdapter adapter;
    List<TLData> pacList;
    GetDataService service;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        spinner = findViewById(R.id.search_spinner);
        ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource(this,
                R.array.searchSpinner, android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinneradapter);

        spinner.setOnItemSelectedListener(this);

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        pacList = new ArrayList<>();

        CustomAdapter.RecyclerViewClickListener listener = new CustomAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position, List<TLData> dataEntries) {

                TLData data;
                List<TLData> mEntries = dataEntries;

                data = mEntries.get(position);

                Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
                intent.putExtra("imageUrl", data.getProfileImageUrl());
                intent.putExtra("username", data.getFirstName());
                intent.putExtra("maintext", data.getPostText());
                intent.putExtra("likecount",  String.valueOf(data.getLikeCount()));
                intent.putExtra("commentcount", String.valueOf(data.getCommentCount()));

                startActivity(intent);


            }
        };

        recyclerView = findViewById(R.id.search_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(this, listener);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);




        searchView = findViewById(R.id.search);
        button = findViewById(R.id.search_button);






    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == R.id.search_spinner){
            final Object itemAtPosition = adapterView.getItemAtPosition(i);
            if(itemAtPosition.equals("HashTag")){
                searchView.setText("");
                searchView.setHint("Search trending Hashtags");
                searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                String text = searchView.getText().toString();

                adapter.clearTasks(pacList);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        KAlertDialog f = new KAlertDialog(SearchActivity.this, KAlertDialog.PROGRESS_TYPE);
                        f.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        f.setTitleText("Fetching Data");
                        f.setCancelable(false);
                        f.show();

                        adapter.clearTasks(pacList);
                        String text = searchView.getText().toString();

                        final Call<JsonObject> call = service.getHashtagSearch("Bearer " + MainActivity.TOKEN, text);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                f.dismissWithAnimation();
                                final JsonObject body = response.body();
                                String postText;
                                final JsonArray data;
                                if (body != null) {
                                    data = body.getAsJsonArray("data");
                                    for (int i = 0; i<data.size(); i++){
                                        final JsonObject jsonElement =  (JsonObject) data.get(i);
                                        final JsonElement id = jsonElement.get("_id");
                                        final JsonElement post_text = jsonElement.get("post_text");
                                        final JsonElement like_count = jsonElement.get("like_count");
                                        final JsonElement comment_count = jsonElement.get("comment_count");
                                        final JsonElement first_name = jsonElement.get("first_name");
                                        final JsonElement post_id = jsonElement.get("post_id");
                                        final JsonElement profile_image_url = jsonElement.get("profile_image_url");

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
                                    }
                                }else{
                                    Toast.makeText(SearchActivity.this, "Could not find hashtag. try another", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                    }
                });


            }else if(itemAtPosition.equals("PostID")){
                searchView.setText("");
                searchView.setHint("Search with PostID");
                searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                String text = searchView.getText().toString();

                adapter.clearTasks(pacList);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        KAlertDialog f = new KAlertDialog(SearchActivity.this, KAlertDialog.PROGRESS_TYPE);
                        f.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        f.setTitleText("Fetching Data");
                        f.setCancelable(false);
                        f.show();

                        adapter.clearTasks(pacList);
                        String text = searchView.getText().toString();

                        final Call<JsonObject> call = service.getPostIdSearch("Bearer " + MainActivity.TOKEN, text);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                f.dismissWithAnimation();
                                final JsonObject body = response.body();
                                String postText;
                                final JsonObject data;
                                if (body != null) {
                                    data = body.getAsJsonObject("data");

                                    final JsonElement id = data.get("_id");
                                    final JsonElement post_text = data.get("post_text");
                                    final JsonElement like_count = data.get("like_count");
                                    final JsonElement comment_count = data.get("comment_count");
                                    final JsonElement first_name = data.get("first_name");
                                    final JsonElement post_id = data.get("post_id");
                                    final JsonElement profile_image_url = data.get("profile_image_url");

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
                                }else{
                                    KAlertDialog k = new KAlertDialog(SearchActivity.this, KAlertDialog.ERROR_TYPE);
                                    k.setContentText("Could not find post");
                                    k.show();
                                }



                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                    }
                });

//                final Call<JsonObject> call = service.getPostIdSearch("Bearer " + MainActivity.TOKEN, text);
//                call.enqueue(new Callback<JsonObject>() {
//                    @Override
//                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                        final JsonObject body = response.body();
//                        String postText;
//                        final JsonArray data = body.getAsJsonArray("data");
//
//                        for (int i = 0; i<data.size(); i++){
//                            final JsonObject jsonElement =  (JsonObject) data.get(i);
//                            final JsonElement id = jsonElement.get("_id");
//                            final JsonElement post_text = jsonElement.get("post_text");
//                            final JsonElement like_count = jsonElement.get("like_count");
//                            final JsonElement comment_count = jsonElement.get("comment_count");
//                            final JsonElement first_name = jsonElement.get("first_name");
//                            final JsonElement post_id = jsonElement.get("post_id");
//                            final JsonElement profile_image_url = jsonElement.get("profile_image_url");
//
//                            if (post_text.getAsString().isEmpty()){
//                                postText = "Test string used to replace empty post";
//                            }else{
//                                postText = post_text.getAsString();
//                            }
//
//                            pacList.add(new TLData(id.toString(),
//                                    postText,
//                                    Integer.parseInt(like_count.getAsString()),
//                                    Integer.parseInt(comment_count.getAsString()),
//                                    first_name.getAsString(),
//                                    Integer.parseInt(post_id.getAsString()),
//                                    profile_image_url.getAsString()));
//
//                            adapter.setTasks(pacList);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                    }
//                });
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
