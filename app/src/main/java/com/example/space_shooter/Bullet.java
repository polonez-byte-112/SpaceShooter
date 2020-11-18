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
                width= bullet.getWidth();
                height= bullet.getHeight();
                width = (int) (width*1.7);
                height = (int) (height*1.7);

                width= (int) (width*screenRatioX);
                height= (int) (height* screenRatioY);

                bullet= Bitmap.createScaledBitmap(bullet, width,height, true);


            }

            Rect getRectangle(){
                return new Rect(x,y, x+width, y+height);
            }
        }
