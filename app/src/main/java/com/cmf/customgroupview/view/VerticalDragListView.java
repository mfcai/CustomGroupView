package com.cmf.customgroupview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ListViewCompat;
import androidx.customview.widget.ViewDragHelper;

public class VerticalDragListView extends FrameLayout {
    ViewDragHelper viewDragHelper;
    View foreView;
    View bgView;
    int moveDis;
    boolean initFlg=false;
    float lastY;
    boolean isMenuOpen = false;
    public VerticalDragListView(@NonNull Context context) {
        this(context,null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                if(child == foreView){
                    return true;
                }
                return false;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
//                Log.d("caimingfu","yvel:"+yvel);
                if (releasedChild == foreView) {

                    if(releasedChild.getTop() < moveDis/2){
                        viewDragHelper.settleCapturedViewAt(0,0);
                        isMenuOpen = false;
                    }else{
                        viewDragHelper.settleCapturedViewAt(0,moveDis);
                        isMenuOpen = true;
                    }
                    invalidate();
                }
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
//                Log.d("caimingfu","top:"+top+",dy:"+dy);
                if(top < 0){
                    top =0;
                }
                if(top > moveDis){
                    top = moveDis;
                }
                return top;
            }
        });
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!initFlg){
            initFlg = true;
            moveDis = bgView.getMeasuredHeight();
        }

    }

    private void initView()  {
        int child_count = getChildCount();
        if(child_count == 2){
            bgView = getChildAt(0);
            foreView = getChildAt(1);

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(isMenuOpen){
            return true;
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                viewDragHelper.processTouchEvent(ev);
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = ev.getY() - lastY;
                if(dy > 0  && !canChildScrollUp()){
                    //向下滑动
                    return  true;
                }

                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public boolean canChildScrollUp() {
        if (foreView instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) foreView, -1);
        }
        return foreView.canScrollVertically(-1);
    }


}
