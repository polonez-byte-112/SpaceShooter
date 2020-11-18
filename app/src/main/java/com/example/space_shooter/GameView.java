package com.example.space_shooter;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;

import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private final int screenX;
    private final int screenY;
    public static float screenRatioX, screenRatioY;
    private final GameActivity gameActivity;
    private final Background bg1;
    private final Background bg2;
    private final Paint paint;
    private final List<Bullet> bullets;
    private final List<Bullet> newBullets;
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
    int amountOfBullets =1;
    static int life;
    private final LifeIcon lifeIcon;
    Canvas canvas;
    int textx;
    public SoundPool soundPool;
    int shoot,enemyGetShot, playerGetShot, bonusSound;
    int playerSpeed=10,enemyBulletSpeed=20, playerBulletSpeed=70;
    String currentEnemySpeed="normal";
    private final Bonus bonus;
    int bound;
    /**
     * Big thanks to @Beko and @Notescrew from Stack Overflow
     *They helped me with ConcurrentModificationException
     *Full story here
     * https://stackoverflow.com/questions/64876689/concurrentmodificationexception-at-displaying-bullets/
     * @Beko : https://stackoverflow.com/users/4303296/beko
     * @Notescrew https://stackoverflow.com/users/4762502/notescrew

     */

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
        newBullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        enemies= new Enemy[5];
        bonus = new Bonus(getResources());
        for (int i=0; i<5; i++){
            Enemy  enemy =new Enemy(getResources(), screenX);
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


        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 1);

        shoot= soundPool.load(gameActivity.getApplicationContext(), R.raw.shoot, 1);
        playerGetShot= soundPool.load(gameActivity.getApplicationContext(), R.raw.player_get_shot, 1);
        enemyGetShot= soundPool.load(gameActivity.getApplicationContext(), R.raw.get_shot, 1);
        bonusSound= soundPool.load(gameActivity.getApplicationContext(), R.raw.bonus, 1);


        bonus.x= random.nextInt(screenX-bonus.width);
        bonus.randomBonus = random.nextInt(Bonus.bound)+1;
        bonus.y =-bonus.height;
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
            flight.x -=playerSpeed;
        }
        if(flight.isGoingLeft==false && flight.isGoingRight==true){
            flight.x +=playerSpeed;
        }
        if(flight.x >=screenX-flight.widthFlight){
            flight.x=screenX-flight.widthFlight;
        }
        if(flight.x<0){
            flight.x =0;
        }


        // bullets
        bullets.addAll(newBullets);
        newBullets.clear();

        trash = new ArrayList<>();
        for (Bullet bullet: bullets){
            if(bullet.y<0){
                trash.add(bullet);
            }
            bullet.y = bullet.y-(int)( ( playerBulletSpeed * screenRatioY));


            for(Enemy enemy: enemies){
                if(Rect.intersects(enemy.getRectangle(), bullet.getRectangle())){
                    score++;
                   soundPool.play(enemyGetShot, 1,1,1,0,1);
                    randomShot = random.nextInt(60-30)+30;
                    System.out.println("Nowy random shot: "+randomShot);
                    bullet.y=-500;
                    enemy.y=-500;

                }
            }
        }
            bullets.removeAll(trash);
            trash.clear();



        //Enemy

        for (Enemy enemy : enemies) {

            enemy.y += enemy.speed;

            if ( enemy.y + enemy.heightEnemy < 0) {

                if(currentEnemySpeed.equals("normal")){
                 bound = (int) (15 * screenRatioY);

                    enemy.speed = random.nextInt(bound);

                    if (enemy.speed < 10 * screenRatioY)
                        enemy.speed = (int) (10 * screenRatioY);
                }

                if(currentEnemySpeed.equals("slow")){
                    bound = (int) (5 * screenRatioY);

                    enemy.speed = random.nextInt(bound);

                    if (enemy.speed < 4 * screenRatioY)
                        enemy.speed = (int) (4 * screenRatioY);
                }


                if(currentEnemySpeed.equals("fast")){
                    bound = (int) (25 * screenRatioY);

                    enemy.speed = random.nextInt(bound);

                    if (enemy.speed < 20 * screenRatioY)
                        enemy.speed = (int) (20 * screenRatioY);
                }



                enemy.x = random.nextInt(screenX -enemy.widthEnemy);
                enemy.y = -enemy.heightEnemy;




            }


            if(Rect.intersects(flight.getRectangle(), enemy.getRectangle())){
                life--;
               if(amountOfBullets==2){ amountOfBullets=1;}
               if(amountOfBullets==3){ amountOfBullets=2;}
                enemy.y=-500;
                if(life==0){
                    isGameOver=true;}else{ soundPool.play(playerGetShot,1,1,1,0,1);}

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

            enemyBullet.y = enemyBullet.y+(int)( ( enemyBulletSpeed * screenRatioY));

            if(Rect.intersects(flight.getRectangle(), enemyBullet.getRectangle())){
                life--;

                enemyBullet.y=screenY+50;
                if(life==0){
                    isGameOver=true;}else{ soundPool.play(playerGetShot,1,1,1,0,1);}
            }
        }
        enemyBullets.removeAll(enemyTrash);
        enemyTrash.clear();

        //BONUSES
        bonus.y += 20*screenRatioY;
        if(bonus.y+ bonus.height>screenY){
            bonus.y=-(bonus.height+5*screenY);
            bonus.randomBonus = random.nextInt(Bonus.bound)+1;
            bonus.x= random.nextInt(screenX-bonus.width);
        }
        if(bonus.y>0 && bonus.y<screenY){
        if(Rect.intersects(flight.getRectangle(), bonus.getRectangle())){
                bonus.y=-(bonus.height+5*screenY);
                doSpecificBonus();
                bonus.randomBonus = random.nextInt(Bonus.bound)+1;
                bonus.x= random.nextInt(screenX-bonus.width);

            }


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

            canvas.drawBitmap(bonus.getBonus(), bonus.x, bonus.y, paint);


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


        if(amountOfBullets==1) {
            Bullet bullet = new Bullet(getResources());
            bullet.x = (int) (((flight.x + flight.widthFlight / 2) - 18) * screenRatioX);
            bullet.y = flight.y - 20;
            // @Beko: this is my best guess:
             // @Beko: avoid adding new bullet to this list, because when update deletes elements while you add someting to it, you get ConcurrentModificationException
            newBullets.add(bullet); // @Beko: use a different list to add new bullets
        }

        if(amountOfBullets==2) {
            Bullet bullet = new Bullet(getResources());
            bullet.x = (int) (((flight.x + flight.widthFlight / 2)-10) * screenRatioX);
            bullet.y = flight.y - 20;


            Bullet bullet1 = new Bullet(getResources());
            bullet1.x = (int) (((flight.x + flight.widthFlight / 2) - 38) * screenRatioX);
            bullet1.y = flight.y - 20;
            newBullets.add(bullet1);
            newBullets.add(bullet);
        }

        if(amountOfBullets==3) {
            Bullet bullet = new Bullet(getResources());
            bullet.x = (int) (((flight.x + flight.widthFlight / 2)+28-20) * screenRatioX);
            bullet.y = flight.y - 20;


            Bullet bullet1 = new Bullet(getResources());
            bullet1.x = (int) (((flight.x + flight.widthFlight / 2)-20) * screenRatioX);
            bullet1.y = flight.y - 20;



            Bullet bullet2 = new Bullet(getResources());
            bullet2.x = (int) (((flight.x + flight.widthFlight / 2) - 48) * screenRatioX);
            bullet2.y = flight.y - 20;
            newBullets.add(bullet);
            newBullets.add(bullet1);
            newBullets.add(bullet2);
        }

    }


    public void createNewEnemyBullet(){


        for(Enemy enemy: enemies){
            EnemyBullet enemyBullet = new EnemyBullet(getResources());
            enemyBullet.x = (int) (((enemy.x+enemy.widthEnemy/2)-18)*screenRatioX);
            enemyBullet.y= enemy.y+enemy.heightEnemy;
            enemyBullets.add(enemyBullet);
        }

    }


    private void doSpecificBonus() {
        soundPool.play(bonusSound, 1,1,1,0,1);
        switch (bonus.randomBonus){
            case 1: healthBonus(); break;
            case 2: bullet1Bonus(); break;
            case 3: bullet2Bonus(); break;
            case 4: bullet3Bonus(); break;
            case 5: enemySlowBonus(); break;
            case 6: enemySpeedBonus(); break;
            case 7: playerSlowBonus(); break;
            case 8: playerSpeedBonus(); break;
        }

    }


    private void healthBonus(){

        if(life<5){
            life++;
        }
    }



    private void bullet1Bonus(){
        System.out.println("Sprawia ze z automatu 1 bullet");
        amountOfBullets =1;
    }

    private void bullet2Bonus(){
        amountOfBullets =2;
    }

    private void bullet3Bonus(){
        amountOfBullets =3;
    }


    private void enemySlowBonus(){
      if(currentEnemySpeed.equals("normal")){ currentEnemySpeed="slow"; enemyBulletSpeed=17;}
        if(currentEnemySpeed.equals("fast")){ currentEnemySpeed="normal"; enemyBulletSpeed=20;}
    }

    private void enemySpeedBonus(){

        if(currentEnemySpeed.equals("normal")){ currentEnemySpeed="fast"; enemyBulletSpeed=30;}
        if(currentEnemySpeed.equals("slow")){ currentEnemySpeed="normal"; enemyBulletSpeed=20;}
    }

    private void playerSlowBonus(){
        if(playerSpeed==10){playerSpeed=5; playerBulletSpeed=55;}
        if(playerSpeed==15){playerSpeed=10; playerBulletSpeed=70;}
    }

    private void playerSpeedBonus(){
        if(playerSpeed==10){playerSpeed=15; playerBulletSpeed=90;}
        if(playerSpeed==5){playerSpeed=10; playerBulletSpeed=70;}
    }

}