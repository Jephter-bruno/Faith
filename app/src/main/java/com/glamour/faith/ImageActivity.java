package com.glamour.faith;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageActivity extends AppCompatActivity {
    private ImageView love;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        love = findViewById(R.id.love);

        String images = getIntent().getExtras().getString("postimage");
        Glide.with(getApplicationContext()).load(images).into(love);
    }
}