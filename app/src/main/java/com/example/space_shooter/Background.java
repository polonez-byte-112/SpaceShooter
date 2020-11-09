package com.example.space_shooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x=0,y=0;

    Background(Resources res, int screenX, int screenY){


        Bitmap background  = BitmapFactory.decodeResource(res, R.drawable.background);

        background= Bitmap.createScaledBitmap(background, screenX, screenY,false);


    }


}
