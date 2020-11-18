package com.example.space_shooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

import static com.example.space_shooter.GameView.screenRatioX;
import static com.example.space_shooter.GameView.screenRatioY;

public class Bonus {


    int x,y,width,height, randomBonus;
    String currentBonus="";
    Bitmap extraHealth, bullet1, bullet2, bullet3, enemySlow,enemyFast,playerSlow, playerFast;
    // 8 jest obrazków i losujemy od 1-8
    static int bound = 8;


    public Bonus(Resources res){
        extraHealth= BitmapFactory.decodeResource(res, R.drawable.health_extra_bonus);

        bullet1= BitmapFactory.decodeResource(res, R.drawable.bullet_bonus);
        bullet2 = BitmapFactory.decodeResource(res, R.drawable.bullet2_bonus);
        bullet3= BitmapFactory.decodeResource(res, R.drawable.bullet3_bonus);

        enemySlow= BitmapFactory.decodeResource(res, R.drawable.enemy_slowdown_bonus);
        enemyFast = BitmapFactory.decodeResource(res, R.drawable.enemy_speedup_bonus);

        playerSlow = BitmapFactory.decodeResource(res, R.drawable.player_slowdown_bonus);
        playerFast = BitmapFactory.decodeResource(res, R.drawable.player_speedup_bonus);

        width= extraHealth.getWidth();
        height = extraHealth.getHeight();

        width /= 4;
        height /=4;

        width = (int) (width*screenRatioX);
        height = (int) (height* screenRatioY);


        extraHealth = Bitmap.createScaledBitmap(extraHealth,width,height,false);

        bullet1 = Bitmap.createScaledBitmap(bullet1,width,height,false);
        bullet2 = Bitmap.createScaledBitmap(bullet2,width,height,false);
        bullet3 = Bitmap.createScaledBitmap(bullet3,width,height,false);

        enemySlow = Bitmap.createScaledBitmap(enemySlow,width,height,false);
        enemyFast = Bitmap.createScaledBitmap(enemyFast,width,height,false);

        playerSlow = Bitmap.createScaledBitmap(playerSlow,width,height,false);
        playerFast = Bitmap.createScaledBitmap(playerFast,width,height,false);


    }

    Bitmap getBonus(){

        if(randomBonus==1){
            return extraHealth;
        }

        if(randomBonus==2){
            return bullet1;
        }

        if(randomBonus==3){
            return bullet2;
        }

        if(randomBonus==4){
            return bullet3;
        }

        if(randomBonus==5){
            return enemySlow;
        }

        if(randomBonus==6){
            return enemyFast;
        }

        if(randomBonus==7){
            return playerSlow;
        }

        return playerFast;



    }

    Rect getRectangle(){
        return new Rect(x,y, x+width, y+height);
    }
}
