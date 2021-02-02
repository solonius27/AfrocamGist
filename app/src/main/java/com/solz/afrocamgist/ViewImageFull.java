package com.solz.afrocamgist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewImageFull extends AppCompatActivity {

    Intent intent;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_full);

        image = findViewById(R.id.full_image);


        intent = getIntent();
        String imgurl = intent.getStringExtra("imageUrl");

        Picasso.get().load(String.format("https://cdn.staging.afrocamgist.com/%s", imgurl)).placeholder(R.drawable.profilepic).into(image);
    }
}
