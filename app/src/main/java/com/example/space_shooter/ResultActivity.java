package com.example.space_shooter;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    ImageView bg1, bg2;
    private ValueAnimator resultAnimatior;
    TextView displayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        bg1 = findViewById(R.id.result_background_one);
        bg2 = findViewById(R.id.result_background_two);


        resultAnimatior = ValueAnimator.ofFloat(1.0f, 0.0f);
        resultAnimatior.setRepeatCount(ValueAnimator.INFINITE);
        resultAnimatior.setInterpolator(new LinearInterpolator());
        resultAnimatior.setDuration(10000L);
        resultAnimatior.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float height = bg1.getHeight();
                final float translationY = height * progress;
                bg1.setTranslationY(translationY);
                bg2.setTranslationY(translationY - height);
            }
        });

        resultAnimatior.start();


        displayText= (TextView) findViewById(R.id.displayScore);

        displayText.setText(""+ GameView.score+" ");



        findViewById(R.id.playAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultAnimatior.cancel();
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            resultAnimatior.pause();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            resultAnimatior.resume();
        }
    }
}