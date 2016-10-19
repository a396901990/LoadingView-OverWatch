package com.dean.loadingview_overwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by DeanGuo on 10/19/16.
 */

public class OverWatchLoadingView extends View {

    public static final int OFFSET_SCALE = 20;

    private int mCenterX, mCenterY, mViewLength;

    ArrayList<OverWatchViewItem> items = new ArrayList<>();

    public OverWatchLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (OverWatchViewItem item : items) {
            item.drawViewItem(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;
        mViewLength = w / 3;
        initOverWatchLoadingView();
    }

    private void initOverWatchLoadingView() {
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
