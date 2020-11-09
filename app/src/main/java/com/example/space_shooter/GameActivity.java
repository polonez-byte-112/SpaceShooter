package com.example.space_shooter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

    GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        gv= new GameView(this, point.x , point.y);
        setContentView(gv);
    }


    @Override
    protected void onPause() {
        super.onPause();
        gv.pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        gv.resume();
    }
}