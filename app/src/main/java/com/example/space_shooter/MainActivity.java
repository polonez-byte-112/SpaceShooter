package com.example.space_shooter;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    int screenX,screenY;
    int screenRatioX, screenRatioY;
     ImageView backgroundOne, backgroundTwo;

     public static int theme = 1;
   private ValueAnimator animator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

         backgroundOne = (ImageView) findViewById(R.id.background_one);
         backgroundTwo = (ImageView) findViewById(R.id.background_two);


          animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float height = backgroundOne.getHeight();
                final float translationY = height * progress;
                backgroundOne.setTranslationY(translationY);
                backgroundTwo.setTranslationY(translationY - height);
            }
        });

        animator.start();


        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    animator.cancel();
                    startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });


        findViewById(R.id.photob1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme=1;

                Toast.makeText(getBaseContext(),"Ship 1 Selected", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.photob2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme=2;
                Toast.makeText(getBaseContext(),"Ship 2 Selected", Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.photob3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme=3;
                Toast.makeText(getBaseContext(),"Ship 3 Selected", Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.photob4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme=4;
                Toast.makeText(getBaseContext(),"Ship 4 Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onResume() {
        animator.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animator.pause();
        }
    }


}