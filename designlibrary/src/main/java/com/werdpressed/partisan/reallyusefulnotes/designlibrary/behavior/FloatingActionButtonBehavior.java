package com.werdpressed.partisan.reallyusefulnotes.designlibrary.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

@SuppressWarnings("unused")
public class FloatingActionButtonBehavior extends FloatingActionButton.Behavior {

    //private static final String TAG = "FAB-Behavior";

    @SuppressWarnings("unused")
    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if ((dyUnconsumed > 0 || dyConsumed > 0) && child.getVisibility() == View.VISIBLE) {
            child.hide();
        } else if ((dyUnconsumed < 0 || dyConsumed < 0 ) && child.getVisibility() != View.VISIBLE) {
            child.show();
        }
    }
}
