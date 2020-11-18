package com.example.space_shooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.space_shooter.GameView.life;
import static com.example.space_shooter.GameView.screenRatioX;
import static com.example.space_shooter.GameView.screenRatioY;

public class LifeIcon {

    int x,y, width1,height , width2, width3, width4, width5;
    Bitmap lifeBitmap1, lifeBitmap2, lifeBitmap3,lifeBitmap4, lifeBitmap5;

    LifeIcon(Resources res){


            lifeBitmap1 = BitmapFactory.decodeResource(res, R.drawable.life1);



            lifeBitmap2 = BitmapFactory.decodeResource(res, R.drawable.life2);



            lifeBitmap3 = BitmapFactory.decodeResource(res, R.drawable.life3);



            lifeBitmap4 = BitmapFactory.decodeResource(res, R.drawable.life4);



            lifeBitmap5 = BitmapFactory.decodeResource(res, R.drawable.life5);




        width1= lifeBitmap1.getWidth();
        width2= lifeBitmap2.getWidth();
        width3= lifeBitmap3.getWidth();
        width4= lifeBitmap4.getWidth();
        width5= lifeBitmap5.getWidth();
        height= lifeBitmap1.getHeight();

        width1 /=4;
        width2 /=4;
        width3 /=4;
        width4 /=4;
        width5 /=4;
        height /=4;

        width1 = (int) (width1* screenRatioX);
        width2 = (int) (width2* screenRatioX);
        width3 = (int) (width3* screenRatioX);
        width4 = (int) (width4* screenRatioX);
        width5 = (int) (width5* screenRatioX);
        height = (int) (height*screenRatioY);

        lifeBitmap1 = Bitmap.createScaledBitmap(lifeBitmap1, width1,height,false);
        lifeBitmap2 = Bitmap.createScaledBitmap(lifeBitmap2, width2,height,false);
        lifeBitmap3 = Bitmap.createScaledBitmap(lifeBitmap3, width3,height,false);
        lifeBitmap4 = Bitmap.createScaledBitmap(lifeBitmap4, width4,height,false);
        lifeBitmap5 = Bitmap.createScaledBitmap(lifeBitmap5, width5,height,false);


    }

    Bitmap getLifeBitmap(){

        if(life==5){
            return  lifeBitmap5;
        }

        if(life==4){
            return  lifeBitmap4;
        }

        if(life==3){
            return  lifeBitmap3;
        }
        if(life==2){
            return lifeBitmap2;
        }



        return lifeBitmap1;
    }


}
