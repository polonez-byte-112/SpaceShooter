package com.example.space_shooter;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.Rect;
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
    private List<EnemyBullet> enemyBullets;
    private    Flight flight;
    private Enemy[] enemies;
    private Random random;
   private boolean isRunning=true;
   private boolean isGameOver=false;
   private Thread thread;
   private int score;
    private List<Bullet> trash;
    private List<EnemyBullet> enemyTrash;
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
        enemyBullets= new ArrayList<>();
        enemies= new Enemy[5];

        for (int i=0; i<5; i++){
           Enemy  enemy =new Enemy(getResources(), screenX, screenY);
            enemies[i]=enemy;

        }

        random= new Random();
        score=0;




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

            for(Enemy enemy: enemies){
                if(Rect.intersects(enemy.getRectangle(), bullet.getRectangle())){
                    score++;
                    bullet.y=-500;
                    enemy.y=-500;
                    enemy.wasShot=true;
                }
            }
        }


        for(Bullet bullet : trash){
            bullets.remove(bullet);
        }

        //Enemy bullet
        enemyTrash = new ArrayList<>(9999);
        for (Bullet bullet: bullets){
            if(bullet.y<screenY){
                trash.add(bullet);
            }

            if(bullet.y>= screenY) {
                bullet.y = bullet.y+(int)( ( 70 * screenRatioY));
            }


                if(Rect.intersects(flight.getRectangle(), bullet.getRectangle())){
                    bullet.y=-500;
                    flight.y=-500;
                    isGameOver=true;
                }

        }


        for(EnemyBullet enemyBullet : enemyTrash){
            enemyBullets.remove(enemyBullet);
        }






        for (Enemy enemy : enemies) {

            enemy.y += enemy.speed;

            if ( enemy.y + enemy.heightEnemy < 0) {


                int bound = (int) (30 * screenRatioY);
                enemy.speed = random.nextInt(bound);

                if (enemy.speed < 10 * screenRatioY)
                    enemy.speed = (int) (10 * screenRatioY);

                enemy.x = random.nextInt(screenX -enemy.widthEnemy);

                enemy.y = -enemy.heightEnemy;

              for(EnemyBullet enemyBullet: enemyBullets){


                      int boundOfBullet = (int)(40 * screenRatioY);
                      enemyBullet.speed= random.nextInt(boundOfBullet);

                      if(enemyBullet.speed<10 * screenRatioY){
                          enemyBullet.speed= (int) (10*screenRatioY);
                      }


                      enemyBullet.x= enemy.x+enemy.widthEnemy/2;
                      enemyBullet.y = enemy.y;


              }

            }




            if(Rect.intersects(flight.getRectangle(), enemy.getRectangle())){
                enemy.y=-500;
                for(EnemyBullet enemyBullet : enemyBullets){
                    enemyBullet.y=-500;
                }
                isGameOver=true;
            }

            if(enemy.y>=screenY){
                enemy.x = random.nextInt(screenX -enemy.widthEnemy);
                enemy.y = -enemy.heightEnemy;
            }


        }










    }

    public void draw(){

        if(getHolder().getSurface().isValid()){



            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(bg1.background, bg1.x, bg1.y,paint);
            canvas.drawBitmap(bg2.background, bg2.x, bg2.y,paint);



            for (Enemy enemy: enemies){
                canvas.drawBitmap(enemy.getEnemy(), enemy.x, enemy.y, paint);

                //nie wiem czy dobrze zagniezdzam
                for(EnemyBullet enemyBullet: enemyBullets){
                    canvas.drawBitmap(enemyBullet.enemyBullet, enemyBullet.x, enemyBullet.y, paint);
                }
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            try {
                for (Bullet bullet : bullets) {

                    canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

                }
            } catch (ConcurrentModificationException e){
                System.out.println("Błąd z bullet");
                e.printStackTrace();
            }


            if(isGameOver){
                isRunning=false;
                getHolder().unlockCanvasAndPost(canvas);
                waitBeforeExciting();
            }

            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    private void waitBeforeExciting() {
        try {
            Thread.sleep(1000);
            gameActivity.startActivity(new Intent(gameActivity, MainActivity.class));

            // zmienic na strone z wynikiem, i dodac takie samo tło

            gameActivity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
