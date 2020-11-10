package com.example.space_shooter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

public class GameView extends SurfaceView implements Runnable {

  private   int screenX;
  private   int screenY;
  public static    float screenRatioX, screenRatioY;
    private   GameActivity gameActivity;
    private  Background bg1, bg2;
    private    Paint paint;
    private    Flight flight;

   private boolean isRunning=true;
   private Thread thread;


    public GameView(GameActivity gameActivity, int screenX, int screenY) {
        super(gameActivity);
        this.gameActivity=gameActivity;

        this.screenX=screenX;
        this.screenY=screenY;

        screenRatioX=1080f / screenX;
        screenRatioY =1920f / screenY;

        bg1= new Background(getResources(), screenX, screenY);
        bg2= new Background(getResources(), screenX, screenY);

        flight = new Flight(this, screenX,screenY, getResources());


        bg2.y=screenY;


        paint= new Paint();




    }



    public void update(){

        bg1.y -= 10 * screenRatioX;
        bg2.y -= 10 * screenRatioX;


        if (bg1.y + bg1.background.getHeight() < 0) {
            bg1.y = screenY;
        }

        if (bg2.y + bg2.background.getHeight() < 0) {
            bg2.y = screenY;
        }


        if(flight.isGoingLeft==true){

        }



    }

    public void draw(){

        if(getHolder().getSurface().isValid()){



            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(bg1.background, bg1.x, bg1.y,paint);
            canvas.drawBitmap(bg2.background, bg2.x, bg2.y,paint);

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (event.getX() <= screenX / 2 &&  event.getY()>=flight.y ) {
                    flight.isGoingRight=false;
                    flight.isGoingLeft=true;
                    System.out.println("Ekran przesuwany w lewo");
                }
                if (event.getX() > screenX / 2  &&  event.getY()>=flight.y ) {

                    flight.isGoingRight=true;
                    flight.isGoingLeft=false;
                    System.out.println("Ekran przesuwany w prawo");
                }
                break;



            case MotionEvent.ACTION_UP:
                if ( event.getY()<flight.y ) {
                    System.out.println("STRZAL!!");
                }

                break;
        }


        return true;
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
