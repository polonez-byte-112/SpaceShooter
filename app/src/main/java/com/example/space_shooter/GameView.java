package com.example.space_shooter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

public class GameView extends SurfaceView implements Runnable {

    int screenX;
    int screenY;
    MainActivity ma;
    float screenRatioX, screenRatioY;
    GameActivity gameActivity;

   private boolean isRunning=true;
   private Thread thread;


    public GameView(GameActivity gameActivity, int screenX, int screenY) {
        super(gameActivity);

        this.gameActivity=gameActivity;
        this.gameActivity.ma = ma;
        this.screenX=screenX;
        this.screenY=screenY;

        screenRatioX=1080/ screenX;
        screenRatioY =1920/ screenY;


    }



    public void update(){



    }

    public void draw(){

        if(getHolder().getSurface().isValid()){



            Canvas canvas = getHolder().lockCanvas();



            getHolder().unlockCanvasAndPost(canvas);
        }

    }






    public void sleep(){
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void pause(){

        try {
            isRunning= false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void resume(){
      thread = new Thread(this);
      isRunning=true;
      thread.start();
    }

    @Override
    public void run() {
        while(isRunning){
            update();
            draw();
            sleep();
        }
    }

}
