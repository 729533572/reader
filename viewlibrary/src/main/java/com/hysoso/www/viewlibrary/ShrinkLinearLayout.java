package com.hysoso.www.viewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

/**
 * Created by luhe on 2018/4/9.
 */

public class ShrinkLinearLayout extends LinearLayout {
    private boolean isShrinking = false;
    //    private boolean isPressed = false;
    private boolean alreadTouch = false;

    private int shrinkDuration = 100;
    private int expandDuration = 100;
    private float shrinkRate = 0.9f;

    public ShrinkLinearLayout(Context context) {
        super(context);
    }
    public ShrinkLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }
    public ShrinkLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RelativeTimeTextView, 0, 0);
        try {
            shrinkDuration = a.getInt(R.styleable.ShrinkLinearLayout_shrinkDuration,100);
            expandDuration = a.getInt(R.styleable.ShrinkLinearLayout_expandDuration,100);
            shrinkRate = a.getFloat(R.styleable.ShrinkLinearLayout_shrinkRate,0.9f);
        } catch (Exception ex){
            Log.e(getClass().getSimpleName(),ex.getMessage()+"/"+ex.getCause());
        }
        finally {
            a.recycle();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(getClass().getSimpleName(),ev.getAction()+"");
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                ScaleAnimation shrinkAnimation  = new ScaleAnimation(1.0f,shrinkRate,1.0f,shrinkRate, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f  );
                shrinkAnimation.setDuration(shrinkDuration);
                shrinkAnimation.setFillAfter(true);
                shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        isShrinking = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isShrinking = false;
                        synchronized (this){
                            if (!isPressed()){
                                ScaleAnimation expandAnimation  = new ScaleAnimation(shrinkRate,1.0f,shrinkRate,1.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f  );
                                expandAnimation.setFillAfter(true);
                                expandAnimation.setDuration(expandDuration);
                                startAnimation(expandAnimation);
                            }
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                startAnimation(shrinkAnimation);
            }
            break;
            case MotionEvent.ACTION_CANCEL:
                expandAnimation();
                break;
            case MotionEvent.ACTION_UP:
                expandAnimation();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    private void expandAnimation(){
        synchronized (this){
            if (!isShrinking){
                ScaleAnimation expandAnimation  = new ScaleAnimation(shrinkRate,1.0f,shrinkRate,1.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f  );
                expandAnimation.setFillAfter(true);
                expandAnimation.setDuration(expandDuration);
                startAnimation(expandAnimation);
            }
        }
    }
    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return true;
    }
    public int getLeastDuration(){
        return shrinkDuration;
    }
}
