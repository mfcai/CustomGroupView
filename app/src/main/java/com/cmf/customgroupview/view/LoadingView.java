package com.cmf.customgroupview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.cmf.customgroupview.R;
/*
 * 1.整个布局分3部分：形状View + 阴影View + 文字提示
 * 2.形状View实现上抛下落和旋转动画，阴影View实现缩放动画
 * 3.形状View上抛时，阴影配合缩小；形状View下落时阴影配合放大
 * 4.形状View下落时，形状切换（正方形，圆形，三角形）
 * 5.形状View旋转角度（正方形，圆形：180；三角形：60）
 */

public class LoadingView extends LinearLayout {
    ShapeView shapeView;
    View shaderView;
    float transY;
    final static long duration = 350;
    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ui_loading_view,this);
        shapeView= findViewById(R.id.shape_view);
        shaderView = findViewById(R.id.shader_view);
        transY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getContext().getResources().getDisplayMetrics());
        downAnimator();
    }

    private void downAnimator() {
        ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(shapeView,"translationY",0f,transY);
        translateAnimator.setDuration(duration);
        translateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(shaderView,"scaleX",1f,0.3f);
        scaleAnimator.setDuration(duration);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateAnimator,scaleAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                shapeView.changeShape();
                upAnimator();
            }


        });
        animatorSet.start();
    }

    private void transformAnimator() {
        int a=0;
        switch (shapeView.getCurSape()){
            case CIRCLE:
                a = 180;
                break;
            case TRIAGLE:
                a= 60;
                break;
            case SQUARE:
                a=180;
                break;
        }
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(shapeView,"rotation",0f,a);
        rotationAnimator.setDuration(duration);
        rotationAnimator.start();
    }

    private void upAnimator(){
        ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(shapeView,"translationY",transY,0f);
        translateAnimator.setDuration(duration);
        translateAnimator.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(shaderView,"scaleX",0.3f,1f);
        scaleAnimator.setDuration(duration);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateAnimator,scaleAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                shapeView.changeShape();
                downAnimator();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                transformAnimator();
            }
        });
        animatorSet.start();
    }

    public void destroyView(){
        shapeView.clearAnimation();
        shaderView.clearAnimation();
        ViewGroup parent = (ViewGroup)getParent();
        if(parent != null){
            parent.removeAllViews();
            removeAllViews();
        }


    }
}
