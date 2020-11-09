package com.example.space_shooter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

    GameView gv;
    int screenX,  screenY,screenRatioX, screenRatioY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gv= new GameView(this, screenX, screenY);

        Point point = new Point();

        getWindowManager().getDefaultDisplay().getSize(point);

        int screenX = point.x;
        int screenY= point.y;

        screenRatioX  = 1080/screenX;
        screenRatioY  = 1920/screenY;


        setContentView(gv);
    }
}