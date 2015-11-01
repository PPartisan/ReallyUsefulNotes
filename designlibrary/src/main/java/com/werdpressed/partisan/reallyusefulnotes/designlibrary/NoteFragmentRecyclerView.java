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

public class NoteFragmentRecyclerView extends RecyclerView {

    private static final int TOTAL_ANIMATION_TIME = 500;

    private int screenWidth;
    private int startX, finalX;

    public NoteFragmentRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public NoteFragmentRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoteFragmentRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        calculateScreenWidth(context);

        startX = 0;
        finalX = screenWidth;
    }

    private void calculateScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
    }

    public void updateListOrder() {
        createAnimatorSet();
    }

    private void createAnimatorSet() {

        final int duration = 100;

        AnimatorSet set = new AnimatorSet();
        ArrayList<Animator> animArrayList = new ArrayList<>();

        for (int i = 0; i < getChildCount(); i++) {
            ObjectAnimator anim = ObjectAnimator
                    .ofFloat(getChildAt(i), "translationX", finalX);

            anim.addListener(new RowAnimationOnListener(i, duration, startX));
            animArrayList.add(anim);
        }
        set.setInterpolator(new AccelerateInterpolator());
        set.setDuration(duration);
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

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof LinearLayoutManager)) {
            throw new IllegalLayoutManagerException();
        }
        super.setLayoutManager(layout);
    }

    private static class IllegalLayoutManagerException extends IllegalArgumentException {

        public IllegalLayoutManagerException() {
            super("LayoutManager must be of type " +
                    "LinearLayoutManager or GridLayoutManager");
        }

    }

    private class RowAnimationOnListener implements Animator.AnimatorListener {

        private int position, duration, targetX;

        public RowAnimationOnListener(int position, int duration, int targetX) {
            this.position = position;
            this.duration = duration;
            this.targetX = targetX;
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            getAdapter().notifyItemChanged(position);
            animateOn(position, duration, targetX);
        }

        @Override
        public void onAnimationCancel(Animator animation) { }

        @Override
        public void onAnimationRepeat(Animator animation) { }
    }
}
