package com.dean.overwatchloadingview;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by DeanGuo on 10/19/16.
 */

public class OverWatchViewItem {

    public static final int CORNER_PATH_EFFECT_SCALE = 8;
    public static final int ANIMATION_DURATION = 200;
    private Paint mPaint;
    private Path mPath;
    private float mScale = 0;
    private OverWatchLoadingView mLoadingView;
    private PointF mCenterPoint;

    private ValueAnimator mShowAnimation, mHideAnimation;

    public OverWatchViewItem(OverWatchLoadingView loadingView, PointF centerPoint, int length) {
        this.mLoadingView = loadingView;
        mCenterPoint = centerPoint;
        mPaint = new Paint();
        mPaint.setColor(mLoadingView.color);
        mPaint.setStrokeWidth(3);
        mPaint.setAlpha(0);
        CornerPathEffect corEffect = new CornerPathEffect(length / CORNER_PATH_EFFECT_SCALE);
        mPaint.setPathEffect(corEffect);

        PointF[] points = getHexagonPoints(centerPoint, length);
        mPath = new Path();
        mPath.moveTo(points[1].x, points[1].y);
        mPath.lineTo(points[2].x, points[2].y);
        mPath.lineTo(points[3].x, points[3].y);
        mPath.lineTo(points[4].x, points[4].y);
        mPath.lineTo(points[5].x, points[5].y);
        mPath.lineTo(points[6].x, points[6].y);
        mPath.close();

        initInitAnimation();
    }

    private void initInitAnimation() {
        mShowAnimation = ValueAnimator.ofFloat(0, 1);
        mShowAnimation.setDuration(ANIMATION_DURATION);
        mShowAnimation.setInterpolator(new DecelerateInterpolator());
        mShowAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animValue = (float) animation.getAnimatedValue();
                mScale = 0.5f + animValue / 2;
                mPaint.setAlpha((int) (animValue * 255));
                mLoadingView.invalidate();
            }
        });

        mHideAnimation = ValueAnimator.ofFloat(1, 0);
        mHideAnimation.setDuration(ANIMATION_DURATION);
        mHideAnimation.setInterpolator(new DecelerateInterpolator());
        mHideAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animValue = (float) animation.getAnimatedValue();
                mScale = 0.5f + animValue / 2;
                mPaint.setAlpha((int) (animValue * 255));
                mLoadingView.invalidate();
            }
        });
    }

    public void drawViewItem(Canvas canvas) {
        canvas.save();
        canvas.scale(mScale, mScale, mCenterPoint.x, mCenterPoint.y);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    /**
     * @param centerPoint
     * @param height
     * @return seven points for Hexagon. 6(point)+1(center point)
     */
    public PointF[] getHexagonPoints(PointF centerPoint, int height) {
        PointF[] points = new PointF[7];

        float length = (float) (height / Math.tan(Math.PI / 3) * 2);

        points[0] = centerPoint;
        points[1] = new PointF(centerPoint.x, centerPoint.y - length);
        points[2] = new PointF(centerPoint.x + height, centerPoint.y - length / 2);
        points[3] = new PointF(centerPoint.x + height, centerPoint.y + length / 2);
        points[4] = new PointF(centerPoint.x, centerPoint.y + length);
        points[5] = new PointF(centerPoint.x - height, centerPoint.y + length / 2);
        points[6] = new PointF(centerPoint.x - height, centerPoint.y - length / 2);

        return points;
    }

    public ValueAnimator getShowAnimation() {
        return mShowAnimation;
    }

    public void reset () {
        mScale= 0;
        mPaint.setAlpha(0);
        mLoadingView.invalidate();
    }

    public void setColor (int color) {
        mPaint.setColor(color);
        mLoadingView.invalidate();
    }

    public ValueAnimator getHideAnimation() {
        return mHideAnimation;
    }
}
