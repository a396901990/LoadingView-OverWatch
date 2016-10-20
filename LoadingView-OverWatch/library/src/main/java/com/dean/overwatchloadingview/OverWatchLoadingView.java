package com.dean.overwatchloadingview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by DeanGuo on 10/19/16.
 */

public class OverWatchLoadingView extends View {

    public static final int OFFSET_SCALE = 20;

    private int mCenterX, mCenterY, mViewLength;

    private AnimatorSet mPlayAnimator = new AnimatorSet();

    private ArrayList<OverWatchViewItem> items = new ArrayList<>();

    private boolean isStart = false;

    protected int color;

    public OverWatchLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OverWatchLoadingView, 0, 0);
        color = attributes.getColor(R.styleable.OverWatchLoadingView_view_color, Color.GRAY);
        attributes.recycle();
    }

    public void start() {
        if (mPlayAnimator == null || mPlayAnimator.isRunning() || mPlayAnimator.isStarted()) {
            return;
        }

        mPlayAnimator.removeAllListeners();
        mPlayAnimator.addListener(new OWRepeatListener());
        mPlayAnimator.start();
        isStart = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pause() {
        if (mPlayAnimator == null) {
            return;
        }
        mPlayAnimator.pause();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resume() {
        if (mPlayAnimator == null) {
            return;
        }
        mPlayAnimator.resume();
    }

    public void end() {
        if (mPlayAnimator == null) {
            return;
        }
        resetAllItem();
        mPlayAnimator.removeAllListeners();
        mPlayAnimator.end();
        isStart = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isStart) {
            for (OverWatchViewItem item : items) {
                item.drawViewItem(canvas);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        mViewLength = (w > h ? h : w) / 3;
        initViewItems();
        initAnimation();
    }

    private void initAnimation() {
        ArrayList<Animator> animators = new ArrayList<>();
        // add show animation
        for (OverWatchViewItem item : items) {
            animators.add(item.getShowAnimation());
        }

        // add hide animation
        for (OverWatchViewItem item : items) {
            animators.add(item.getHideAnimation());
        }

        // start animation and repeat
        mPlayAnimator.playSequentially(animators);
    }

    public void resetAllItem() {
        for (OverWatchViewItem item : items) {
            item.reset();
        }
        invalidate();
    }

    public void setColor(int color) {
        this.color = color;
        for (OverWatchViewItem item : items) {
            item.setColor(color);
        }
        invalidate();
    }

    public class OWRepeatListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            animator.start();
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }

    private void initViewItems() {
        PointF[] points = getHexagonPoints(new PointF(mCenterX, mCenterY), mViewLength);
        int itemLength = mViewLength / 2;
        int offsetLength = itemLength / OFFSET_SCALE;

        for (PointF point : points) {
            items.add(new OverWatchViewItem(this, point, itemLength - offsetLength));
        }
    }

    /**
     * @param centerPoint
     * @param length
     * @return seven points for Hexagon. 6(point)+1(center point)
     */
    public static PointF[] getHexagonPoints(PointF centerPoint, int length) {
        PointF[] points = new PointF[7];

        float height = (float) (Math.sin(Math.PI / 3) * length);

        points[0] = new PointF(centerPoint.x - length / 2, centerPoint.y - height);
        points[1] = new PointF(centerPoint.x + length / 2, centerPoint.y - height);
        points[2] = new PointF(centerPoint.x + length, centerPoint.y);
        points[3] = new PointF(centerPoint.x + length / 2, centerPoint.y + height);
        points[4] = new PointF(centerPoint.x - length / 2, centerPoint.y + height);
        points[5] = new PointF(centerPoint.x - length, centerPoint.y);
        points[6] = centerPoint;

        return points;
    }
}
