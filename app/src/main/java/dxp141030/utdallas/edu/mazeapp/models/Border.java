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
 * Modified on 22nd April 2015
 */


/**
 *  Class that helps draw the border for the entire maze
 * The intertwining layout will be handled in other class
 */
public class Border {

    // Paint object to define the brush size, color, etc...
    private Paint mPaint;
    private float startX, startY, stopX, stopY;

    // Constructor
    public Border(Context context, float startX, float startY, float stopX, float stopY) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
        mPaint = new Paint();
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(context.getResources().getColor(R.color.border_color));
        mPaint.setStrokeWidth(7);
    }

    // DrawLine used to draw the border with given start, stop for X and Y
    public void draw(Canvas c) {
        c.drawLine(startX, startY, stopX, stopY, mPaint);
    }

    // Getters and Setters
    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getStopX() {
        return stopX;
    }

    public void setStopX(float stopX) {
        this.stopX = stopX;
    }

    public float getStopY() {
        return stopY;
    }

    public void setStopY(float stopY) {
        this.stopY = stopY;
    }
}
