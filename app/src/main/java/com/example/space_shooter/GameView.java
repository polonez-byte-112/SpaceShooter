package com.example.space_shooter;

import android.content.Context;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private int screenX;
   private int screenY;
   private float screenRatioX, screenRatioY;
   private GameActivity gameActivity;

   private boolean isRunning=true;
   private Thread thread;


    public GameView(GameActivity gameActivity, int screenX, int screenY) {
        super(gameActivity);

        this.gameActivity=gameActivity;
        this.screenX=screenX;
        this.screenY=screenY;

    }



    public void update(){}

    public void draw(){}






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
