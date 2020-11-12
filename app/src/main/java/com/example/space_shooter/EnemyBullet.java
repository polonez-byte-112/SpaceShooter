package com.example.space_shooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.space_shooter.GameView.screenRatioX;
import static com.example.space_shooter.GameView.screenRatioY;

public class EnemyBullet {

    int x,y,width,height;

    Bitmap enemyBullet;

    EnemyBullet(Resources res){

        enemyBullet= BitmapFactory.decodeResource(res, R.drawable.bullet2);
        width= (int) ((enemyBullet.getWidth()*1.5)*screenRatioX);
        height= (int) ((enemyBullet.getHeight()*1.5)* screenRatioY);

        enemyBullet= Bitmap.createScaledBitmap(enemyBullet, width,height, false);


    }

    Rect getRectangle(){
        return new Rect(x,y, x+width, y+height);
    }
}
