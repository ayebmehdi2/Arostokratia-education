package com.mehdi.blankactivity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class ShowImage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image);

        try {
            String url = getIntent().getStringExtra("url");

            if (url == null) return;

            ImageView view = findViewById(R.id.img_show);

            try {
                Glide.with(this).load(url).into(view);
            }catch (Exception e){
                e.printStackTrace();
            }

            findViewById(R.id.back_show_img).setOnClickListener(view1 -> finish());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
