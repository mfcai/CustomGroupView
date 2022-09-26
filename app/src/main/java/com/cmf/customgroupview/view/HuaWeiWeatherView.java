package com.cmf.customgroupview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class HuaWeiWeatherView extends View {
    int len;
    Paint linePaint,ovalPaint;
    Paint targetLinePatin;
    Paint textPaint,smallPaint;
    RectF ovalRect;
    float startAngle=120,endAngle = 300;
    int redius,smallRadius;
    int perAngle=0;
    int rotateAngle=30;
    float targetAngle=200;
    private  OnAngleColorListener listener;
    int score;
    public HuaWeiWeatherView(Context context) {
        this(context,null);
    }

    public HuaWeiWeatherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HuaWeiWeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(2);
        linePaint.setStyle(Paint.Style.STROKE);

        //绘制有色部分的画笔
        targetLinePatin=new Paint();
        targetLinePatin.setColor(Color.GREEN);
        targetLinePatin.setStrokeWidth(2);
        targetLinePatin.setAntiAlias(true);

        ovalPaint = new Paint();
        ovalPaint.setColor(Color.WHITE);
        ovalPaint.setAntiAlias(true);
        ovalPaint.setStyle(Paint.Style.STROKE);


        //绘制文本
        textPaint=new Paint();
        //设置文本居中对齐
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);

        smallPaint=new Paint();
        ovalPaint.setAntiAlias(true);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        len = Math.min(width,height);
        ovalRect = new RectF(0,0,len,len);
        redius = len/2;

        smallRadius=redius-60;
        perAngle = (int)(endAngle/99);
        setMeasuredDimension(len,len);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(ovalRect,startAngle,endAngle,false,ovalPaint);
        drawLine(canvas);
        drawScoreText(canvas);

    }
    int red,green;
    private void drawLine(Canvas canvas){
        canvas.save();
        canvas.translate(redius,redius);
        canvas.rotate(rotateAngle);
        //记录已经绘制过的有色部分范围
        float hasDraw=0;
        for(int i=0;i<=100;i++){
            if(hasDraw<=targetAngle&&targetAngle!=0){//需要绘制有色部分的时候
                //画一条刻度线
                float percent=hasDraw/endAngle;
                red= 255-(int) (255*percent);
                green= (int) (255*percent);
                targetLinePatin.setARGB(255,red,green,0);
                listener.colorListener(red,green);
                canvas.drawLine(0,redius,0,redius-40,targetLinePatin);
            }else {//不需要绘制有色部分
                //画一条刻度线
                canvas.drawLine(0,redius,0,redius-40,linePaint);
            }
            //累计绘制过的部分
            hasDraw+=perAngle;
            canvas.rotate(perAngle);
        }

        canvas.restore();
    }

    //判断是否在动
    private boolean isRunning;
    //判断是回退的状态还是前进状态
    private int state = 1;
    public void changeAngle(final int trueAngle) {
        if (isRunning){//如果在动直接返回
            return;
        }
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                switch (state) {
                    case 1://后退状态
                        isRunning=true;
                        targetAngle -= 3;
                        if (targetAngle <= 0) {//如果回退到0
                            targetAngle = 0;
                            //改为前进状态
                            state = 2;
                        }
                        break;
                    case 2://前进状态
                        targetAngle += 3;
                        if (targetAngle >= trueAngle) {//如果增加到指定角度
                            targetAngle = trueAngle;
                            //改为后退状态
                            state = 1;
                            isRunning=false;
                            //结束本次运动
                            timer.cancel();
                        }
                        break;
                    default:
                        break;
                }
                //重新绘制（子线程中使用的方法）
                score = (int) ((targetAngle/endAngle)*100);
                postInvalidate();
            }
        }, 500, 30);
    }

    /**
     * 绘制小圆和文本的方法，小圆颜色同样渐变
     * @param canvas
     */
    private void drawScoreText(Canvas canvas) {
        smallPaint.setARGB(100,red,green,0);
        // 画小圆指定圆心坐标，半径，画笔即可
        int smallRadius=redius-60;
        canvas.drawCircle(redius, redius, redius - 60, smallPaint);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(smallRadius/2);
        //score需要通过计算得到
        canvas.drawText(""+score,redius,redius,textPaint);
        //绘制分，在分数的右上方
        textPaint.setTextSize(smallRadius/6);
        canvas.drawText("分",redius+smallRadius/2,redius-smallRadius/4,textPaint);
        //绘制点击优化在分数的下方
        textPaint.setTextSize(smallRadius/6);
        canvas.drawText("点击优化",redius,redius+smallRadius/2,textPaint);
    }

    public OnAngleColorListener getListener() {
        return listener;
    }

    public void setListener(OnAngleColorListener listener) {
        this.listener = listener;
    }

    public interface OnAngleColorListener {
        public void colorListener(int red, int green);
    }
}
