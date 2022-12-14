package com.cmf.customgroupview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.cmf.customgroupview.R;

import java.text.DecimalFormat;
import java.util.Calendar;

public class ClockView extends View {
    //刻度线长度
    private final static int MARK_LENGTH = 20;

    //使用wrap_content时默认的尺寸
    private final static int DEFAULT_SIZE = 400;

    //刻度线宽度
    private final static int MARK_WIDTH = 8;
    //刻度线与圆的间隙
    private final static int MARK_GAP = 12;
    //时针宽度
    private final static int HOUR_LINE_WIDTH = 10;

    //分针宽度
    private final static int MINUTE_LINE_WIDTH = 6;

    //秒针宽度
    private final static int SECOND_LINE_WIDTH = 4;

    //圆半径
    private int radius;

    //圆的画笔
    private Paint circlePaint;

    //刻度线画笔
    private Paint markPaint;

    //时针画笔
    private Paint hourPaint;

    //分针画笔
    private Paint minutePaint;

    //秒针画笔
    private Paint secondPaint;

    //时针长度
    private int hourLineLength;

    //分针长度
    private int minuteLineLength;

    //秒针长度
    private int secondLineLength;

    //圆的颜色
    private int mCircleColor = Color.WHITE;
    //时针的颜色
    private int mHourColor = Color.BLACK;
    //分针的颜色
    private int mMinuteColor = Color.BLACK;
    //秒针的颜色
    private int mSecondColor = Color.RED;
    //一刻钟刻度线的颜色
    private int mQuarterMarkColor = Color.parseColor("#B5B5B5");
    //分钟刻度线的颜色
    private int mMinuteMarkColor = Color.parseColor("#EBEBEB");
    //是否绘制3个指针的圆心
    private boolean isDrawCenterCircle = true;

    //圆心坐标
    private int centerX;
    private int centerY;
    private Bitmap hourBitmap;
    private Bitmap minuteBitmap;
    private Bitmap secondBitmap;
    private Canvas hourCanvas;
    private Canvas minuteCanvas;
    private Canvas secondCanvas;
    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        mCircleColor = a.getColor(R.styleable.ClockView_circle_color, Color.WHITE);
        mHourColor = a.getColor(R.styleable.ClockView_hour_color, Color.BLACK);
        mMinuteColor = a.getColor(R.styleable.ClockView_minute_color, Color.BLACK);
        mSecondColor = a.getColor(R.styleable.ClockView_second_color, Color.RED);
        mQuarterMarkColor = a.getColor(R.styleable.ClockView_quarter_mark_color, Color.parseColor("#B5B5B5"));
        mMinuteMarkColor = a.getColor(R.styleable.ClockView_minute_mark_color, Color.parseColor("#EBEBEB"));
        isDrawCenterCircle = a.getBoolean(R.styleable.ClockView_draw_center_circle, false);
        a.recycle();
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(mCircleColor);

        markPaint = new Paint();
        circlePaint.setAntiAlias(true);
        markPaint.setStyle(Paint.Style.FILL);
        markPaint.setStrokeCap(Paint.Cap.ROUND);
        markPaint.setStrokeWidth(MARK_WIDTH);

        hourPaint = new Paint();
        hourPaint.setAntiAlias(true);
        hourPaint.setColor(mHourColor);
        hourPaint.setStyle(Paint.Style.FILL);
        hourPaint.setStrokeCap(Paint.Cap.ROUND);
        hourPaint.setStrokeWidth(HOUR_LINE_WIDTH);

        minutePaint = new Paint();
        minutePaint.setAntiAlias(true);
        minutePaint.setColor(mMinuteColor);
        minutePaint.setStyle(Paint.Style.FILL);
        minutePaint.setStrokeCap(Paint.Cap.ROUND);
        minutePaint.setStrokeWidth(MINUTE_LINE_WIDTH);

        secondPaint = new Paint();
        secondPaint.setAntiAlias(true);
        secondPaint.setColor(mSecondColor);
        secondPaint.setStyle(Paint.Style.FILL);
        secondPaint.setStrokeCap(Paint.Cap.ROUND);
        secondPaint.setStrokeWidth(SECOND_LINE_WIDTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int len = Math.min(width,height);

        radius = len/2;
        centerX = len/2;
        centerY = len/2;
        hourLineLength = radius / 2;
        minuteLineLength = radius * 3 / 4;
        secondLineLength = radius * 3 / 4;
        //时针
        hourBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        hourCanvas = new Canvas(hourBitmap);

        //分针
        minuteBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        minuteCanvas = new Canvas(minuteBitmap);

        //秒针
        secondBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        secondCanvas = new Canvas(secondBitmap);

        setMeasuredDimension(len,len);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX,centerY,radius,circlePaint);
        canvas.save();
        canvas.translate(centerX,centerY);
        for(int i=0;i<12;i++){
            if (i % 3 == 0) {//一刻钟
                markPaint.setColor(mQuarterMarkColor);
            } else {
                markPaint.setColor(mMinuteMarkColor);
            }
            canvas.drawLine(0,radius - MARK_GAP,0,radius - MARK_GAP- MARK_LENGTH,markPaint);
            canvas.rotate(30);
        }
        canvas.restore();

        canvas.save();
        Calendar calendar = Calendar.getInstance();
        int hour12 = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        //(方案一)每过一小时(3600秒)时针添加30度，所以每秒时针添加（1/120）度
        //(方案二)每过一小时(60分钟)时针添加30度，所以每分钟时针添加（1/2）度
        hourCanvas.save();
        //清空画布
        hourCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        hourCanvas.rotate(hour12 * 30 + minute * 0.5f, centerX, centerY);
        hourCanvas.drawLine(centerX, centerY,
                centerX,  centerY-hourLineLength, hourPaint);
        if (isDrawCenterCircle)//根据指针的颜色绘制圆心
            hourCanvas.drawCircle(centerX, centerY, 2 * HOUR_LINE_WIDTH, hourPaint);
        hourCanvas.restore();

        minuteCanvas.save();
        //清空画布
        minuteCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        minuteCanvas.rotate(minute * 6 + second * 0.1f, centerX, centerY);
        minuteCanvas.drawLine(centerX, centerY,
                centerX,  centerY-minuteLineLength, hourPaint);
        if (isDrawCenterCircle)//根据指针的颜色绘制圆心
            minuteCanvas.drawCircle(centerX, centerY, 2 * MINUTE_LINE_WIDTH, minutePaint);
        minuteCanvas.restore();

        secondCanvas.save();
        //清空画布
        secondCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        secondCanvas.rotate(second * 6 , centerX, centerY);
        secondCanvas.drawLine(centerX, centerY,
                centerX,  centerY-secondLineLength, secondPaint);
        if (isDrawCenterCircle)//根据指针的颜色绘制圆心
            secondCanvas.drawCircle(centerX, centerY, 2 * MINUTE_LINE_WIDTH, secondPaint);
        secondCanvas.restore();
        canvas.drawBitmap(hourBitmap, 0, 0, null);
        canvas.drawBitmap(minuteBitmap, 0, 0, null);
        canvas.drawBitmap(secondBitmap,0,0,null);

        canvas.restore();
        postInvalidateDelayed(1000);


    }

    /**
     * int小于10的添加0
     *
     * @param i
     * @return
     */
    private String intAdd0(int i) {
        DecimalFormat df = new DecimalFormat("00");
        if (i < 10) {
            return df.format(i);
        } else {
            return i + "";
        }
    }
}
