package dxp141030.utdallas.edu.mazeapp.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import dxp141030.utdallas.edu.mazeapp.R;


/**
 * @author Durga Sai Preetham Palagummi
 * @NetID dxp141030
 * @Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 24th April 2015
 */


/**
 * Ball class used to draw the ball for the game
 */
public class Ball {

    // Paint object to define the brush size, color, etc...
    Paint mPaint;

    // Constructor
    public Ball(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(context.getResources().getColor(R.color.ball_color));
    }

    // DrawCircle draws a circle that will be considered as a ball for this game
    public void draw(float x, float y, float ballRadius, Canvas c) {
        c.drawCircle(x, y, ballRadius, mPaint);
    }
}
