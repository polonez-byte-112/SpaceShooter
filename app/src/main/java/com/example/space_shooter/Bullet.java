package com.example.space_shooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.space_shooter.GameView.screenRatioX;
import static com.example.space_shooter.GameView.screenRatioY;

public class Bullet {
    int x,y,width,height;


    Bitmap bullet;

    Bullet(Resources res){

        bullet= BitmapFactory.decodeResource(res, R.drawable.bullet4);
        width= (int) ((bullet.getWidth()*2)*screenRatioX);
        height= (int) ((bullet.getHeight()*2)* screenRatioY);

        bullet= Bitmap.createScaledBitmap(bullet, width,height, false);


    }

    Rect getRectangle(){
        return new Rect(x,y, x+width, y+height);
    }
}
