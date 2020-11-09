package com.example.space_shooter;

import android.content.Context;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    public GameView(GameActivity gameActivity, int screenX, int screenY) {
        super(gameActivity);
    }

    @Override
    public void run() {

    }
}
