package com.amanpatel.veggiestoretest0.Adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CheckoutViewPager extends ViewPager {
    private Boolean disable = false;

    public CheckoutViewPager(Context context) {
        super(context);
    }

    public CheckoutViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !disable && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        return !disable && super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void disableScroll(Boolean disable) {
        this.disable = disable;
    }
}
