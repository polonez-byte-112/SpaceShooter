package com.example.space_shooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.space_shooter.R;

import static com.example.space_shooter.GameView.screenRatioX;
import static com.example.space_shooter.GameView.screenRatioY;

public class Flight {

    public  boolean isGoingLeft=false, isGoingRight=false;
    int  widthFlight, heightFlight;
    int wingCounter=0;
    int x,y;
     GameView gameView;
    Bitmap flight1, flight2, flight3;

    Flight( GameView gameView, int screenX,int screenY, Resources res){

        if(MainActivity.theme==1) {
            flight1 = BitmapFactory.decodeResource(res, R.drawable.theme11);
            flight2 = BitmapFactory.decodeResource(res, R.drawable.theme12);
            flight3 = BitmapFactory.decodeResource(res, R.drawable.theme13);
        }


        if(MainActivity.theme==2) {
            flight1 = BitmapFactory.decodeResource(res, R.drawable.theme21);
            flight2 = BitmapFactory.decodeResource(res, R.drawable.theme22);
            flight3 = BitmapFactory.decodeResource(res, R.drawable.theme23);
        }


        if(MainActivity.theme==3) {
            flight1 = BitmapFactory.decodeResource(res, R.drawable.theme31);
            flight2 = BitmapFactory.decodeResource(res, R.drawable.theme32);
            flight3 = BitmapFactory.decodeResource(res, R.drawable.theme33);
        }


        if(MainActivity.theme==4) {
            flight1 = BitmapFactory.decodeResource(res, R.drawable.theme41);
            flight2 = BitmapFactory.decodeResource(res, R.drawable.theme42);
            flight3 = BitmapFactory.decodeResource(res, R.drawable.theme43);
        }


            widthFlight = flight1.getWidth();
            heightFlight = flight1.getHeight();


            widthFlight /= 4;
            heightFlight /= 4;

            widthFlight = (int) (widthFlight * screenRatioX);
            heightFlight = (int) (heightFlight * screenRatioY);



            flight1= Bitmap.createScaledBitmap(flight1, widthFlight, heightFlight, false);
            flight2= Bitmap.createScaledBitmap(flight2, widthFlight, heightFlight, false);
            flight3= Bitmap.createScaledBitmap(flight3, widthFlight, heightFlight, false);

             this.gameView=gameView;


            y = (int) (screenY - heightFlight*screenRatioY)-40;
            x = (int) ((screenX/2-widthFlight/2)*screenRatioX);

    }


    Bitmap getFlight(){

        if(wingCounter==0){
            wingCounter++;
            return flight1;
        }

        if(wingCounter==1){
            wingCounter++;
            return flight2;
        }
        wingCounter=0;
        return flight3;
    }


    Rect getRectangle(){
        return new Rect(x+20,y+15, x+widthFlight-35, y+heightFlight-10);
    }
}
