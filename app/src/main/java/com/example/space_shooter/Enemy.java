package com.example.space_shooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

import static com.example.space_shooter.GameView.screenRatioX;
import static com.example.space_shooter.GameView.screenRatioY;

public class Enemy {

    int widthEnemy, heightEnemy;
    int wingEnemyCounter=0;
    int x,y;
    int speed=10;
    public boolean wasShot=true;
    Bitmap enemy1, enemy2, enemy3;

    Enemy(Resources res, int screenX, int screenY){
        enemy1= BitmapFactory.decodeResource(res, R.drawable.enemy1backup);
        enemy2= BitmapFactory.decodeResource(res, R.drawable.enemy2backup);
        enemy3= BitmapFactory.decodeResource(res, R.drawable.enemy3backup);

        widthEnemy = enemy1.getWidth();
        heightEnemy = enemy1.getHeight();

        widthEnemy = (int) (widthEnemy*0.5);
        heightEnemy= (int) (heightEnemy*0.7);

        widthEnemy =  (int)( widthEnemy *screenRatioX);
       heightEnemy =  (int)( heightEnemy *screenRatioY);

        enemy1= Bitmap.createScaledBitmap(enemy1, widthEnemy, heightEnemy, false);
        enemy2= Bitmap.createScaledBitmap(enemy2, widthEnemy, heightEnemy, false);
        enemy3= Bitmap.createScaledBitmap(enemy3, widthEnemy, heightEnemy, false);



        // x i y potem ustawimy
        Random random = new Random();
        x= random.nextInt(screenX- widthEnemy);
        y = 0;
    }


    Bitmap getEnemy(){

        if(wingEnemyCounter==0){
            wingEnemyCounter++;
            return enemy1;
        }

        if(wingEnemyCounter==1){
            wingEnemyCounter++;
            return enemy2;
        }

        wingEnemyCounter=0;
        return enemy3;
    }


    Rect getRectangle(){
        return new Rect(x,y, x+ widthEnemy, y+ heightEnemy);
    }
}
