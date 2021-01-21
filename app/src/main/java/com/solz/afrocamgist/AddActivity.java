package com.solz.afrocamgist;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

import static com.solz.afrocamgist.MainActivity.TOKEN;

public class AddActivity extends AppCompatActivity {

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        editText = findViewById(R.id.post_editText);
        button = findViewById(R.id.post_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.getText().toString().trim().isEmpty()){
                    Snackbar.make(view, "Field cannot be empty", BaseTransientBottomBar.LENGTH_SHORT ).show();
                    return;
                }

                String[] empty1 = {};

                HashMap<String, Object> hm = new HashMap<>();
                hm.put("post_text", editText.getText().toString());
                hm.put("post_lat_long", "");
                hm.put("tagged_id", empty1);
                hm.put("post_type", "text");
                hm.put("bg_image_post", false);
                hm.put("bg_map_post", false);
                hm.put("bg_image", "");
                hm.put("post_image", empty1);
                hm.put("post_video", "");
                hm.put("thumbnail", "");
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
}
