package com.werdpressed.partisan.reallyusefulnotes.designlibrary;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;

public class AllNotesFragmentRecyclerView extends RecyclerView {

    private static final int BASE_ANIMATION_TIME = 50;
    private static final int MAX_ANIMATION_TIME_INCREMENT = 100;

    private int screenWidth;
    private int startX, finalX;

    private int[] interpolatedAnimationTimes;

    public AllNotesFragmentRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public AllNotesFragmentRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AllNotesFragmentRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        calculateScreenWidth(context);

        startX = 0;
        finalX = -(screenWidth);
    }

    private void calculateScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
    }

    private int calculateInterpolatedAnimationTime(int currentIndex, int maxIndex) {
        float percentage = ((float)currentIndex/(float)maxIndex);
        float increment = (float) MAX_ANIMATION_TIME_INCREMENT * percentage;
        return (int) (BASE_ANIMATION_TIME + increment);
    }

    public void updateListOrder() {
        createAnimatorSet();
    }

    private void createAnimatorSet() {

        AnimatorSet set = new AnimatorSet();
        ArrayList<Animator> animArrayList = new ArrayList<>();

        for (int i = 0; i < getChildCount(); i++) {
            ObjectAnimator anim = ObjectAnimator
                    .ofFloat(getChildAt(i), "translationX", finalX);

            int duration = calculateInterpolatedAnimationTime(i, getChildCount());

            anim.setDuration(duration);
            anim.addListener(new RowAnimationListener(i, duration, startX));
            animArrayList.add(anim);
        }
        set.setInterpolator(new AccelerateInterpolator());
        set.playSequentially(animArrayList);
        set.start();
    }

    private void animateOn(int childPosition, int duration, int targetValue) {
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(getChildAt(childPosition), "translationX", targetValue);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(duration);
        animator.start();
    }

    /*
    Not entirely happy with this method. Should be existing method to grab all existing rows out-
    side of bind view?
     */
    private void notifyRowsPeripheralToVisibleItemsDataChanged(int currentItem) {
        if ((currentItem == 0)){
            if ((currentItem - 1) > 0) {
                getAdapter().notifyItemChanged(currentItem - 1);
            }
        } else if (currentItem == getChildCount() - 1) {
            if ((currentItem + 1) < getAdapter().getItemCount()) {
                getAdapter().notifyItemChanged(currentItem + 1);
            }
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof LinearLayoutManager)) {
            throw new IllegalLayoutManagerException();
        }
        super.setLayoutManager(layout);
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return (LinearLayoutManager) getLayoutManager();
    }

    private static class IllegalLayoutManagerException extends IllegalArgumentException {

        public IllegalLayoutManagerException() {
            super("LayoutManager must be of type " +
                    "LinearLayoutManager or GridLayoutManager");
        }

    }

    private class RowAnimationListener implements Animator.AnimatorListener {

        private int position, duration, targetX;

        public RowAnimationListener(int position, int duration, int targetX) {
            this.position = position;
            this.duration = duration;
            this.targetX = targetX;
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            int currentItem = getLinearLayoutManager().findFirstVisibleItemPosition() + position;
            getAdapter().notifyItemChanged(currentItem);
            notifyRowsPeripheralToVisibleItemsDataChanged(position);
            animateOn(position, duration, targetX);
        }

        @Override
        public void onAnimationCancel(Animator animation) { }

        @Override
        public void onAnimationRepeat(Animator animation) { }
    }
}
