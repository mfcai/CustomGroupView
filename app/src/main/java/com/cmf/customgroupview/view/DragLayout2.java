package com.cmf.customgroupview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.nineoldandroids.view.ViewHelper;

public class DragLayout2 extends FrameLayout {
    private ViewDragHelper mDragHelper;
    View mMainContent,mTopContent;
    int mWidth,mHeight,mMainTop,mDragRange; //mDragRange为拖拽范围，mMainTop为底部面板离父布局顶部的长度
    int title;
    Status mStatus = Status.Open;
    OnLayoutDragingListener mListener;
    public DragLayout2(@NonNull Context context) {
        this(context,null);
    }

    public DragLayout2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragLayout2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this,0.5f, mCallBack);
    }

    ViewDragHelper.Callback mCallBack=new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (mMainContent.getTop() == title){
                mStatus = Status.Close;
            }else {
                mStatus = Status.Open;
            }
            boolean directionCheck=mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL,pointerId);
            return (child==mMainContent||child==mTopContent)&&directionCheck;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (mMainTop+dy<title){
                return title;
            }else if(mMainTop+dy>mDragRange+title){
                return mDragRange+title;
            }
            return top;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if(changedView==mMainContent){
                mMainTop=top;
            }else{
                mMainTop+=dy;
            }
            if (mMainTop < title) {
                mMainTop = title;
            } else if (mMainTop > mDragRange+title) {
                mMainTop = mDragRange+title;
            }
            if (changedView == mTopContent) {
                layoutContent();   //如果拖动的是顶部面板则进行强制布局移动
            }
            dispatchDragEvent(mMainTop);

        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (yvel > 0) {
                open();
            } else if (yvel == 0 && mMainTop > mDragRange * 0.5f) {
                open();
            } else {
                close();
            }
        }
    };

    private void layoutContent() {
        mMainContent.layout(0, mMainTop,  mWidth, mHeight+mMainTop);
        mTopContent.layout(0, -mHeight/6, mWidth, mHeight);


    }

    protected void dispatchDragEvent(int mainTop) {
        float percent = mMainTop / (float)mDragRange;
        mTopContent.setTranslationY(mHeight / 6 - mHeight / 6 * percent);
//        ViewHelper.setTranslationY(mTopContent, mHeight / 8 - mHeight / 8 * percent);  //实现顶部下滑
        if (mListener != null) {
            mListener.onDraging(percent);
        }

        Status lastStatus = mStatus;
        if (updateStatus(mainTop) != lastStatus) {
            if (mListener == null) {
                return;
            }
            if (lastStatus == Status.Draging) {
                if (mStatus == Status.Close) {
                    mListener.onClose();
                } else if (mStatus == Status.Open) {
                    mListener.onOpen();
                }

            }
        }
    }

    private Status updateStatus(int mainTop) {
        if (mainTop == title) {
            mStatus = Status.Close;
        } else if (mainTop == mDragRange+title) {
            mStatus = Status.Open;
        } else {
            mStatus = Status.Draging;
        }
        return mStatus;
    }

    public enum Status {
        Open, Close, Draging
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        mMainContent.layout(0,mMainTop,  mWidth, mMainTop+mHeight);
        mTopContent.layout(0, -mHeight/6, mWidth, mHeight);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //拿到宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        //设置拖动范围
        mDragRange = (int)(mHeight*0.4);
        mMainTop=(int)(mHeight*0.5);
        title=mMainTop-mDragRange;
    }
    /**
     * 填充结束时获得两个子布局的引用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        // 必要的检验
        if (childCount < 2) {
            throw new IllegalStateException(
                    "You need two childrens in your content");
        }

        if (!(getChildAt(0) instanceof ViewGroup)
                || !(getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalArgumentException(
                    "Your childrens must be an instance of ViewGroup");
        }

        mTopContent = getChildAt(0);
        mMainContent = getChildAt(1);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    public void close() {
        mMainTop = title;
        // 执行动画，返回true代表有未完成的动画, 需要继续执行
        if (mDragHelper.smoothSlideViewTo(mMainContent, 0, mMainTop)) {
            // 注意：参数传递根ViewGroup
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    public void open() {
        mMainTop = mDragRange+title;
        if (mDragHelper.smoothSlideViewTo(mMainContent, 0, mMainTop)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    public void setOnLayoutDragingListener(OnLayoutDragingListener listener){
        this.mListener = listener;
    }

    public interface OnLayoutDragingListener {
        void onOpen();

        void onClose();

        void onDraging(float percent);
    }
}
