package com.example.space_shooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.space_shooter.GameView.screenRatioX;
import static com.example.space_shooter.GameView.screenRatioY;

public class EnemyBullet {

    int x,y,width,height;


    Bitmap bullet;

    EnemyBullet(Resources res){

        bullet= BitmapFactory.decodeResource(res, R.drawable.bullet2);
        width= bullet.getWidth();
        height= bullet.getHeight();
        width = (int) (width*1.5);
        height = (int) (height*1.5);

        width= (int) (width*screenRatioX);
        height= (int) (height* screenRatioY);

        bullet= Bitmap.createScaledBitmap(bullet, width,height, false);


    }

    Rect getRectangle(){
        return new Rect(x,y, x+width, y+height);
    }
}
