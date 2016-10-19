package com.dean.loadingview_overwatch;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

/**
 * Created by DeanGuo on 10/19/16.
 */

public class OverWatchViewItem {
    public static final int CORNER_PATH_EFFECT_SCALE = 8;
    private Paint mPaint;
    private Path mPath;
    private ValueAnimator scaleAnimator, alphaAnimator;
    private float scale;
    private OverWatchLoadingView overWatchLoadingView;
    private PointF mCenterPoint;

    public OverWatchViewItem(OverWatchLoadingView loadingView, PointF centerPoint, int length) {
        overWatchLoadingView = loadingView;
        mCenterPoint = centerPoint;
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(3);
        CornerPathEffect corEffect = new CornerPathEffect(length/CORNER_PATH_EFFECT_SCALE);
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

        scaleAnimator = ValueAnimator.ofFloat(0.5f, 1);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = (float) animation.getAnimatedValue();
                overWatchLoadingView.invalidate();
            }
        });
        scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnimator.setDuration(1000);
        scaleAnimator.start();

        alphaAnimator = ValueAnimator.ofInt(0, 255);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPaint.setAlpha((Integer) animation.getAnimatedValue());
                overWatchLoadingView.invalidate();
            }
        });
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimator.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimator.setDuration(1000);
        alphaAnimator.start();
    }

    public void drawViewItem(Canvas canvas) {
        canvas.save();
        canvas.scale(scale,scale,mCenterPoint.x, mCenterPoint.y);
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
}
