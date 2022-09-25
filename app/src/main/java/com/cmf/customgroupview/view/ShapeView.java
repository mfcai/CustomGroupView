package com.cmf.customgroupview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.cmf.customgroupview.R;

public class ShapeView extends View {
    Paint mPaint;
    Path mPath;
    public Shape getCurSape() {
        return curSape;
    }

    public void setCurSape(Shape curSape) {
        this.curSape = curSape;
    }

    Shape curSape =Shape.SQUARE;
    public ShapeView(Context context) {
        this(context,null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        switch (curSape){
            case CIRCLE:
                drawCircle(canvas);
                break;
            case TRIAGLE:
                drawTriagle(canvas);
                break;
            case SQUARE:
                drawSquare(canvas);
                break;
        }
        invalidate();
    }

    public void changeShape(){
        switch (curSape){
            case CIRCLE:
                curSape = Shape.TRIAGLE;
                break;
            case TRIAGLE:
                curSape = Shape.SQUARE;
                break;
            case SQUARE:
                curSape = Shape.CIRCLE;
                break;
        }
    }

    private void drawCircle(Canvas canvas){
        int center = getWidth()/2;
        mPaint.setColor(Color.YELLOW);
        canvas.drawCircle(center,center,center,mPaint);
    }

    private void  drawSquare(Canvas canvas){
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

    private void drawTriagle(Canvas canvas){
        if(mPath == null){
            mPath = new Path();
            mPath.moveTo(getWidth()/2,0);
            mPath.lineTo(0,getHeight());
            mPath.lineTo(getWidth(),getHeight());
            mPath.close();
        }
        mPaint.setColor(getResources().getColor(R.color.violet));
        canvas.drawPath(mPath,mPaint);

    }




    enum Shape{
        CIRCLE,SQUARE,TRIAGLE
    }
}
