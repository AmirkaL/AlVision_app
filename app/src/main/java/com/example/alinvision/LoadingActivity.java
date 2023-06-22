package com.example.alinvision;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        ImageView loadingIcon = findViewById(R.id.loading_icon);
        DrawableImageViewTarget drawableImageViewTarget = new DrawableImageViewTarget(loadingIcon);
        Glide.with(this)
            .load(R.drawable.loading_animation)
            .into(drawableImageViewTarget);
    }
}