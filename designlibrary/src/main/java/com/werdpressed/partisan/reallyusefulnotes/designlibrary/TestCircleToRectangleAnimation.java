package com.werdpressed.partisan.reallyusefulnotes.designlibrary;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.werdpressed.partisan.reallyusefulnotes.designlibrary.utils.ConversionUtils;

public class TestCircleToRectangleAnimation extends View {

    private float animatedFraction;

    private float startCenterX, endCenterX, startCenterY, endCenterY;

    private Paint mPaint;

    private RectF mFinalBounds, mBounds;

    public TestCircleToRectangleAnimation(Context context) {
        super(context);
        init(context);
    }

    public TestCircleToRectangleAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestCircleToRectangleAnimation(Context context, RectF finalBounds) {
        super(context, null);
        Log.e(getClass().getSimpleName(), getClass().getSimpleName() + " constructor called");
        mFinalBounds = finalBounds;
        init(context);
    }

    public static TestCircleToRectangleAnimation newInstance(Context context, RectF finalBounds){
        return new TestCircleToRectangleAnimation(context, finalBounds);
    }

    private void init(Context context) {

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(findPaintColor(context.getResources()));

        float left, top, right, bottom, fabSize;

        fabSize = ConversionUtils.convertDpToPixels(context.getResources(),
                ConversionUtils.FAB_SIZE_DEFAULT_DP);
        right = getWidth();
        bottom = getHeight();
        left = right - fabSize;
        top = bottom - fabSize;

        mBounds = new RectF(left, top, right, bottom);

        mFinalBounds = new RectF(0, 0, 500, 500);

        startCenterX = mBounds.centerX();
        startCenterY = mBounds.centerY();
        endCenterX = mFinalBounds.centerX();
        endCenterY = mFinalBounds.centerY();
    }



    @Override
    protected void onDraw(Canvas canvas) {

        Log.e(getClass().getSimpleName(), "onDraw called. Width:Height: " + getWidth() + " " + getHeight());

        if (animatedFraction < 0.2F) {
            //Animate circle to centre
        } else if (animatedFraction > 0.2F && animatedFraction < 0.6F){
            //Expand circle to bounds
        } else {
            //Adjust corner radius
        }
        animateCircle(canvas);
    }

    public void setAnimatedFraction(float animatedFraction) {
        this.animatedFraction = animatedFraction;
        invalidate();
    }

    private void animateCircle(Canvas canvas) {
        //ToDo: startX is known. TargetX is centre of mFinalBounds
        //float percentage = animatedFraction * 5; //convert 0 -> 0.2 val to 0 -> 1.0
        float percentage = animatedFraction;
        float x = ((endCenterX - startCenterX) * percentage) + startCenterX;
        float y = ((endCenterY - startCenterY) * percentage) + startCenterY;
        canvas.drawCircle(x, y, mBounds.width()/2, mPaint);
        Log.e(getClass().getSimpleName(), "percentage: " + percentage + "\nx,y: " + x + " | " + y);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static int findPaintColor(Resources res) {
        int id;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            id = res.getColor(R.color.note_fragment_background, null);
        } else {
            id = res.getColor(R.color.note_fragment_background);
        }
        return id;
    }

}
