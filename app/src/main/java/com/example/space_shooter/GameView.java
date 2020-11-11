package com.example.space_shooter;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

  private   int screenX;
  private   int screenY;
  public static    float screenRatioX, screenRatioY;
    private   GameActivity gameActivity;
    private  Background bg1, bg2;
    private    Paint paint;
    private List<Bullet> bullets;
    private    Flight flight;
    private Enemy[] enemies;
    private Random random;
   private boolean isRunning=true;
   private Thread thread;
    private List<Bullet> trash;
   public int centerShip=0;


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
        centerShip = flight.x;

        bg2.y=screenY;


        paint= new Paint();

        bullets = new ArrayList<>();
        enemies= new Enemy[4];

        for (int i = 0;i < 4;i++) {
            Enemy enemy =new Enemy(getResources());
            enemies[i]=enemy;
        }

        random= new Random();

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


        if(flight.isGoingLeft==true && flight.isGoingRight==false){
            flight.x -=10;
        }



        if(flight.isGoingLeft==false && flight.isGoingRight==true){
            flight.x +=10;
        }



        if(flight.x >=screenX-flight.widthFlight){
            flight.x=screenX-flight.widthFlight;
        }

        if(flight.x<0){
            flight.x =0;
        }



        // bullets

        trash = new ArrayList<>(9999);

        for (Bullet bullet: bullets){
            if(bullet.y>screenY){
                trash.add(bullet);
            }
            if(bullet.y<= screenY) {
                bullet.y = bullet.y-(int)( ( 70 * screenRatioY));
            }
        }


        for(Bullet bullet : trash){
            bullets.remove(bullet);
        }












    }

    public void draw(){

        if(getHolder().getSurface().isValid()){



            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(bg1.background, bg1.x, bg1.y,paint);
            canvas.drawBitmap(bg2.background, bg2.x, bg2.y,paint);

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            try {
                for (Bullet bullet : bullets) {

                    canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

                }
            } catch (ConcurrentModificationException e){
                System.out.println("Błąd z bullet");
                e.printStackTrace();
            }


            for (Enemy enemy: enemies){
                canvas.drawBitmap(enemy.getEnemy(), enemy.x, enemy.y, paint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {



        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() <= screenX / 2 && event.getY() >= flight.y-150) {
                flight.isGoingRight = false;
                flight.isGoingLeft = true;
            }
            if (event.getX() > screenX / 2 && event.getY() >= flight.y-150) {
                flight.isGoingRight = true;
                flight.isGoingLeft = false;
            }


            if (event.getY() < flight.y-150) {
                createNewBullet();
            }
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

    public void createNewBullet(){
        Bullet bullet = new Bullet(getResources());
        bullet.x= (int) ((( flight.x + flight.widthFlight/2)-18)*screenRatioX);
        bullet.y = flight.y-20;
        bullets.add(bullet);



    }

}
