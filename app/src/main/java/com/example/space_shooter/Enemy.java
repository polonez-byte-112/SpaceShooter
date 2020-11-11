package com.example.space_shooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.space_shooter.GameView.screenRatioX;
import static com.example.space_shooter.GameView.screenRatioY;

public class Enemy {

    int widthEnemy, heightEnemy;
    int wingEnemyCounter=0;
    int x,y;
    int speed=20;
    public boolean wasShot=true;
    Bitmap enemy1, enemy2, enemy3;

    Enemy(Resources res){
        enemy1= BitmapFactory.decodeResource(res, R.drawable.enemy1);
        enemy2= BitmapFactory.decodeResource(res, R.drawable.enemy2);
        enemy3= BitmapFactory.decodeResource(res, R.drawable.enemy3);

        widthEnemy = enemy1.getWidth();
        heightEnemy = enemy1.getHeight();

        widthEnemy /=4;
        heightEnemy /=4;

        widthEnemy =  (int)( widthEnemy *screenRatioX);
       heightEnemy =  (int)( heightEnemy *screenRatioY);

        enemy1= Bitmap.createScaledBitmap(enemy1, widthEnemy, heightEnemy, false);
        enemy2= Bitmap.createScaledBitmap(enemy2, widthEnemy, heightEnemy, false);
        enemy3= Bitmap.createScaledBitmap(enemy3, widthEnemy, heightEnemy, false);



        // x i y potem ustawimy

        y = -heightEnemy;
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
