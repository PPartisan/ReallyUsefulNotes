package com.werdpressed.partisan.reallyusefulnotes.designlibrary.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.utils.MathUtils;

import java.lang.ref.WeakReference;

public class CircularRevealLinearLayout extends LinearLayout implements Animator.AnimatorListener,
        ValueAnimator.AnimatorUpdateListener {

    private Path mRevealPath;
    private final Rect mTargetBounds;
    private RevealInfo mRevealInfo;
    private boolean mRunning;
    private float mRadius;

    private ValueAnimator mValueAnimator;

    public CircularRevealLinearLayout(Context context) {
        this(context, null);
    }

    public CircularRevealLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularRevealLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTargetBounds = new Rect();
        mRevealPath = new Path();
        mValueAnimator = ValueAnimator.ofFloat(0F, 1F);
        mValueAnimator.addListener(this);
        mValueAnimator.addUpdateListener(this);
    }

    private void setRevealRadius(float radius) {
        mRadius = radius;
        mRevealInfo.getTarget().getHitRect(mTargetBounds);
        invalidate(mTargetBounds);
    }

    private float getRevealRadius() {
        return mRadius;
    }

    private void attachRevealInfo(RevealInfo revealInfo) {
        mRevealInfo = revealInfo;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int state = canvas.save();

        mRevealPath.reset();
        mRevealPath.addCircle(mRevealInfo.centerX, mRevealInfo.centerY, mRadius, Path.Direction.CW);

        canvas.clipPath(mRevealPath);
        canvas.restoreToCount(state);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mRunning = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mRunning = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        mRunning = false;
        invalidate(mTargetBounds);
    }

    @Override
    public void onAnimationRepeat(Animator animation) { }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setRevealRadius(mRevealInfo.endRadius*animation.getAnimatedFraction());
    }

    public void start() {
        mRevealInfo = new RevealInfo(
                (getWidth()/2),
                (getHeight()/2),
                0,
                (float)MathUtils.findHypoteneuse(getWidth(), getHeight()),
                new WeakReference<View>(this));

        mValueAnimator.start();
    }

    private static class RevealInfo {

        public final int centerX;
        public final int centerY;
        public final float startRadius;
        public final float endRadius;
        public final WeakReference<View> target;

        public RevealInfo(int centerX, int centerY, float startRadius, float endRadius,
                          WeakReference<View> target) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.startRadius = startRadius;
            this.endRadius = endRadius;
            this.target = target;
        }

        public View getTarget(){
            return target.get();
        }

        public boolean hasTarget(){
            return getTarget() != null;
        }
    }
}
