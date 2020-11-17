package com.example.space_shooter;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class GameView extends SurfaceView implements Runnable {

  private final int screenX;
  private final int screenY;
  public static float screenRatioX, screenRatioY;
    private final GameActivity gameActivity;
    private final Background bg1;
    private final Background bg2;
    private final Paint paint;
    private final List<Bullet> bullets;
    private final List<EnemyBullet> enemyBullets;
    private final Flight flight;
    private final Enemy[] enemies;
    private final Random random;
   private boolean isRunning=true;
   private boolean isGameOver=false;
   private Thread thread;
   public static int score;
    private List<Bullet> trash;
    private List<EnemyBullet> enemyTrash;
   public int centerShip=0;
   public int updateCounter=0;
    int randomShot;
    static int life;
    private  LifeIcon lifeIcon;
    MediaPlayer  enemyGetShot, playerGetShot;
    Canvas canvas;
    int textx;
    public SoundPool soundPool;
    int shoot;



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
        random= new Random();
        bullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        enemies= new Enemy[5];
        for (int i=0; i<5; i++){
           Enemy  enemy =new Enemy(getResources(), screenX, screenY);
            enemies[i]=enemy;

        }

        randomShot= random.nextInt(60-30)+30;
        score=0;

        life=3;

        lifeIcon = new LifeIcon(getResources());
        lifeIcon.x = (int) (20*screenRatioX);
        lifeIcon.y = (int) (20*screenRatioY);

        paint.setColor(Color.WHITE);
        paint.setTextSize(108f);




        enemyGetShot = MediaPlayer.create(gameActivity.getApplicationContext(), R.raw.get_shot);
        enemyGetShot.setLooping(false);
        playerGetShot = MediaPlayer.create(gameActivity.getApplicationContext(), R.raw.player_get_shot);
        playerGetShot.setLooping(false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        }else{
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }



        shoot= soundPool.load(gameActivity.getApplicationContext(), R.raw.shoot, 1);


    }



    public void update(){

        updateCounter++;
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

        trash = new ArrayList<>();
        for (Bullet bullet: bullets){
            if(bullet.y<0){
                trash.add(bullet);
            }
                bullet.y = bullet.y-(int)( ( 70 * screenRatioY));


            for(Enemy enemy: enemies){
                if(Rect.intersects(enemy.getRectangle(), bullet.getRectangle())){
                    score++;
                    enemyGetShot.start();
                    randomShot = random.nextInt(60-30)+30;
                    System.out.println("Nowy random shot: "+randomShot);
                    bullet.y=-500;
                    enemy.y=-500;

                }
            }
        }
        for(Bullet bullet : trash){
            bullets.removeAll(trash);
        }



        //Enemy

        for (Enemy enemy : enemies) {

            enemy.y += enemy.speed;

            if ( enemy.y + enemy.heightEnemy < 0) {


                int bound = (int) (15 * screenRatioY);
                enemy.speed = random.nextInt(bound);

                if (enemy.speed < 10 * screenRatioY)
                    enemy.speed = (int) (10 * screenRatioY);

                enemy.x = random.nextInt(screenX -enemy.widthEnemy);
                enemy.y = -enemy.heightEnemy;




            }


            if(Rect.intersects(flight.getRectangle(), enemy.getRectangle())){
            life--;

            enemy.y=-500;
                if(life==0){
                isGameOver=true;}else{ playerGetShot.start();}

            }

            if(enemy.y>=screenY){
                enemy.x = random.nextInt(screenX -enemy.widthEnemy);
                enemy.y = -enemy.heightEnemy;

            }

        }


        //Enemy bullets
        enemyTrash = new ArrayList<>();


    for(EnemyBullet enemyBullet: enemyBullets){
        if(enemyBullet.y>screenY){
            enemyTrash.add(enemyBullet);
        }

        enemyBullet.y = enemyBullet.y+(int)( ( 20 * screenRatioY));

        if(Rect.intersects(flight.getRectangle(), enemyBullet.getRectangle())){
            life--;

            enemyBullet.y=screenY+50;
            if(life==0){
            isGameOver=true;}else{ playerGetShot.start();}
        }
    }

    for(EnemyBullet enemyBullet: enemyTrash){
        enemyBullets.remove(enemyTrash);

    }






    if (updateCounter >= randomShot) {
        createNewEnemyBullet();
        updateCounter = 0;
    }



    if(score<10){
        textx= (int) ((screenX/2)-40*screenRatioX);
    }else if(score>=10 && score<100){
       textx= (int) ((screenX/2)-60*screenRatioX);
    }else if(score>=100 & score<1000){
        textx= (int) ((screenX/2) -100*screenRatioX);
    }else{
        textx= (int) ((screenX/2) -120*screenRatioX);
    }

    }

    public void draw(){

        if(getHolder().getSurface().isValid()){
             canvas = getHolder().lockCanvas();

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


               try{ canvas.drawBitmap(enemy.getEnemy(), enemy.x, enemy.y, paint);}
               catch (ConcurrentModificationException e){
                   System.out.println("Blad z statkiem wroga");
                   e.printStackTrace();
               }


            }

            try{
                for (EnemyBullet enemyBullet : enemyBullets) {

                    canvas.drawBitmap(enemyBullet.bullet, enemyBullet.x, enemyBullet.y, paint);

                }
            } catch (ConcurrentModificationException e)
            {
                System.out.println("Błąd z  enemy bullet");
                e.printStackTrace();

            }


            canvas.drawBitmap(lifeIcon.getLifeBitmap(), lifeIcon.x, lifeIcon.y, paint);



            if(isGameOver){
                isRunning=false;
                waitBeforeExciting();
            }


            canvas.drawText(""+score, textx, 100, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    private void waitBeforeExciting() {
        try {
            Thread.sleep(1000);

            enemyGetShot.pause();
            playerGetShot.pause();
            gameActivity.startActivity(new Intent(gameActivity, ResultActivity.class));

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
                soundPool.play(shoot,0.5f,0.5f,0,0,1);


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


    public void createNewEnemyBullet(){


        for(Enemy enemy: enemies){
            EnemyBullet enemyBullet = new EnemyBullet(getResources());
            enemyBullet.x = (int) (((enemy.x+enemy.widthEnemy/2)-18)*screenRatioX);
            enemyBullet.y= enemy.y+enemy.heightEnemy;
            enemyBullets.add(enemyBullet);
        }

    }



}
