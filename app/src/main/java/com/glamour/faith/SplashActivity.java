package com.glamour.faith;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {
    private TextView name, slogan;
    private ImageView logo;
    private View topView1, topView2, topView3, bottomView1, bottomView2, bottomView3;
    private int count =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorview = getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View. SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_splash);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        name = findViewById(R.id.name);
        slogan = findViewById(R.id.slogan);
        logo = findViewById(R.id.logo);

        topView1 = findViewById(R.id.topView1);
        topView2 = findViewById(R.id.topView2);
        topView3 = findViewById(R.id.topView3);

        bottomView1 = findViewById(R.id.bottomView1);
        bottomView2 = findViewById(R.id.bottomView2);
        bottomView3 = findViewById(R.id.bottomView3);

        Animation logoanimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoom_animation);
        Animation nameanimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoom_animation);
        Animation topview1Animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoom_animation);
        Animation topview2Animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.top_views_animations);
        Animation topview3Animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.top_views_animations);

        Animation bottom1Animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.bottom_views_animations);
        Animation bottom2Animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.bottom_views_animations);
        Animation bottom3Animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.bottom_views_animations);


        topView1.startAnimation(topview1Animation);
        bottomView1.startAnimation(bottom1Animation);

        topview1Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                topView2.setVisibility(View.VISIBLE);
                bottomView2.setVisibility(View.VISIBLE);

                topView2.startAnimation(topview2Animation);
                bottomView2.startAnimation(bottom2Animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        topview2Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                topView3.setVisibility(View.VISIBLE);
                bottomView3.setVisibility(View.VISIBLE);

                topView3.startAnimation(topview3Animation);
                bottomView3.startAnimation(bottom3Animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        topview3Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.setVisibility(View.VISIBLE);
                logo.startAnimation(logoanimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logoanimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                name.setVisibility(View.VISIBLE);
                name.startAnimation(nameanimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        nameanimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                slogan.setVisibility(View.VISIBLE);
                final String animatestring =slogan.getText().toString();
                slogan.setText("");
                count = 0;

                new CountDownTimer(animatestring.length()*100, 100) {
                    @Override
                    public void onTick(long l) {
                        slogan.setText(slogan.getText().toString()+ animatestring.charAt(count));
                        count++;
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
startActivity(new Intent(SplashActivity.this, LoginActivity.class));
finish();

            }
        },10000);
    }
}